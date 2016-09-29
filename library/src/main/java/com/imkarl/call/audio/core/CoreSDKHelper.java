package com.imkarl.call.audio.core;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.imkarl.call.audio.CallConfig;
import com.imkarl.call.audio.CallEvent;
import com.imkarl.call.audio.ConnectStatus;
import com.imkarl.call.audio.OnCallListener;
import com.imkarl.call.audio.OnConnectListener;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.ECVoIPSetupManager;
import com.yuntongxun.ecsdk.SdkErrorCode;

import java.util.ArrayList;
import java.util.List;

/**
 * SDK调用辅助类
 */
public class CoreSDKHelper implements ECDevice.InitListener, ECDevice.OnECDeviceConnectListener, ECDevice.OnLogoutListener {
    private static CoreSDKHelper sdkHelper;
    public static CoreSDKHelper get() {
        if (sdkHelper == null) {
            sdkHelper = new CoreSDKHelper();
        }
        return sdkHelper;
    }


    private Context mContext;
    private ECInitParams mInitParams;

    private CallConfig mConfig;
    private String mUserId;
    private ConnectStatus mConnectStatus = ConnectStatus.FAILED;
    private OnConnectListener mOnConnectListener;
    private final List<OnCallListener> mOnCallListeners = new ArrayList<>();

    private CoreSDKHelper() {
    }

    public void init(Context context, CallConfig config) {
        if (!(context instanceof Application)) {
            context = context.getApplicationContext();
        }
        this.mContext = context;
        this.mConfig = config;
    }

    public void login(String userId) {
        mUserId = userId;

        // 判断SDK是否已经初始化，没有初始化则先初始化SDK
        if(!ECDevice.isInitialized()) {
            mConnectStatus = ConnectStatus.CONNECTING;
            if (mOnConnectListener != null) {
                mOnConnectListener.onConnecting();
            }
            L.d("[init] start regist..");
            ECDevice.initial(mContext, this);
            return ;
        }
        L.d(" SDK has inited , then regist..");
        // 已经初始化成功，直接进行注册
        onInitialized();
    }

    @Override
    public void onInitialized() {
        L.d("ECSDK is ready");

        // 设置消息提醒
//        ECDevice.setNotifyOptions(mOptions);

        // 设置接收VoIP来电事件通知Intent
        // 呼入界面activity、开发者需修改该类
        Intent intent = new Intent(mContext, InnerAudioCallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        ECDevice.setPendingIntent(pendingIntent);

        // 设置SDK注册结果回调通知，当第一次初始化注册成功或者失败会通过该引用回调
        // 通知应用SDK注册状态
        // 当网络断开导致SDK断开连接或者重连成功也会通过该设置回调
        ECDevice.setOnDeviceConnectListener(this);

        // 设置VOIP 自定义铃声路径
        ECVoIPSetupManager setupManager = ECDevice.getECVoIPSetupManager();
        if(setupManager != null) {
            setupManager.setInComingRingUrl(true, mConfig.getIncomingRing().toString());
            setupManager.setOutGoingRingUrl(true, mConfig.getDialRing().toString());
            setupManager.setBusyRingTone(true, mConfig.getBusyRing().toString());
        }


        // SDK已经初始化成功
        //第二步：设置注册参数、设置通知回调监听
        // 构建注册所需要的参数信息
        if (mInitParams == null){
            mInitParams = ECInitParams.createParams();
        }
        mInitParams.reset();
        //自定义登录方式：
        // 如：VoIP账号/手机号码/..
        mInitParams.setUserid(mUserId);
        // appkey
        mInitParams.setAppKey(mConfig.getAppKey());
        // apptoken
        mInitParams.setToken(mConfig.getAppToken());
        // 1代表用户名+密码登陆（可以强制上线，踢掉已经在线的设备）
        // 2代表自动重连注册（如果账号已经在其他设备登录则会提示异地登陆）
        // 3 LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO）
        mInitParams.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
        // 设置登陆验证模式（是否验证密码/如VoIP方式登陆） NORMAL_AUTH-自定义方式
        mInitParams.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);

        //第三步：验证参数是否正确，注册SDK
        if(mInitParams.validate()) {
            // 判断注册参数是否正确
            ECDevice.login(mInitParams);
        }
    }

    @Override
    public void onConnect() {
        // Deprecated
    }

    @Override
    public void onDisconnect(ECError error) {
        // SDK与云通讯平台断开连接
        // Deprecated
    }

    @Override
    public void onConnectState(ECDevice.ECConnectState state, ECError error) {
        L.d("state="+state+",  error="+error);

        ConnectStatus connectStatus;
        switch (state) {
            case CONNECT_SUCCESS:
                connectStatus = ConnectStatus.SUCCESS;
                if (mOnConnectListener != null) {
                    mOnConnectListener.onConnected();
                }
                break;
            case CONNECTING:
                connectStatus = ConnectStatus.CONNECTING;
                if (mOnConnectListener != null) {
                    mOnConnectListener.onConnecting();
                }
                break;
            case CONNECT_FAILED:
                if(error.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                    //账号异地登陆
                } else {
                    //连接状态失败
                }
            default:
                connectStatus = ConnectStatus.FAILED;
                if (mOnConnectListener != null) {
                    mOnConnectListener.onError(new IllegalStateException("code:"+error.errorCode+", msg:"+error.errorMsg));
                }
                break;
        }
        mConnectStatus = connectStatus;

        initVoip();
    }

    private void initVoip() {
        // 注册VoIP呼叫事件回调监听
        ECVoIPCallManager callInterface = ECDevice.getECVoIPCallManager();
        L.d("callInterface="+callInterface);
        if(callInterface != null) {
            callInterface.setOnVoIPCallListener(new SimpleOnVoipListener() {
                @Override
                void onCallEvents(CallEvent event) {
                    doCallEvents(event);
                }
            });
        }
    }

    void doCallEvents(CallEvent event) {
        if (!mOnCallListeners.isEmpty()) {
            for (OnCallListener listener : mOnCallListeners) {
                listener.onCallEvents(event);
            }
        }
    }

    /**
     * 当前SDK注册状态
     */
    public ConnectStatus getConnectState() {
        return mConnectStatus;
    }

    @Override
    public void onLogout() {
        mConnectStatus = ConnectStatus.FAILED;
        if (mOnConnectListener != null) {
            mOnConnectListener.onLogout();
        }

        if(mInitParams != null && mInitParams.getInitParams() != null) {
            mInitParams.getInitParams().clear();
        }
        mInitParams = null;
    }

    @Override
    public void onError(Exception exception) {
        L.e("ECSDK couldn't start: " + exception.getMessage());
        ECDevice.unInitial();

        mConnectStatus = ConnectStatus.FAILED;
        if (mOnConnectListener != null) {
            mOnConnectListener.onError(exception);
        }
        L.e("onError", exception);
    }

    public void logout() {
        ECDevice.logout(this);
    }

    /**
     * 设置连接监听
     */
    public void setOnConnectListener(OnConnectListener listener) {
        this.mOnConnectListener = listener;
    }
    /**
     * 添加电话事件监听
     */
    public void addOnCallListener(OnCallListener listener) {
        if (!this.mOnCallListeners.contains(listener)) {
            this.mOnCallListeners.add(listener);
        }
    }
    /**
     * 移除电话事件监听
     */
    public void removeOnCallListener(OnCallListener listener) {
        this.mOnCallListeners.remove(listener);
    }

    /**
     * 拨号
     * @param userId 对方的UserId
     * @return 会话唯一标识符
     */
    public String requestCall(String userId) {
        return ECDevice.getECVoIPCallManager().makeCall(ECVoIPCallManager.CallType.VOICE, userId);
    }

    /**
     * 接受来电
     * @param callId 会话唯一标识符
     */
    public void acceptCall(String callId) {
        ECDevice.getECVoIPCallManager().acceptCall(callId);
    }

    /**
     * 挂断电话
     * @param callId 会话唯一标识符
     */
    public void hangCall(String callId) {
        ECDevice.getECVoIPCallManager().releaseCall(callId);
    }

    /**
     * 获取当前正在通话的唯一标识
     * @return 会话唯一标识符
     */
    public String getCurrentCallId() {
        return ECDevice.getECVoIPSetupManager().getCurrentCall();
    }

}
