package com.imkarl.call.audio;

import android.content.Context;

import com.imkarl.call.audio.core.CoreSDKHelper;

/**
 * 网络电话
 * @author imkarl 2016-09
 */
public class AudioCall {

    private AudioCall() { }

    /**
     * 仅初始化配置项，无耗时操作
     */
    public static void init(Context context, CallConfig user) {
        CoreSDKHelper.get().init(context, user);
    }

    /**
     * 登陆
     * @param userId VoIP账号（如：手机号码）
     */
    public static void login(String userId) {
        CoreSDKHelper.get().login(userId);
    }

    /**
     * 退出登陆
     */
    public static void logout() {
        CoreSDKHelper.get().logout();
    }

    /**
     * 监听连接状态改变事件
     */
    public static void setOnConnectListener(OnConnectListener listener) {
        CoreSDKHelper.get().setOnConnectListener(listener);
    }

    /**
     * 添加电话事件监听
     */
    public static void addOnCallListener(OnCallListener listener) {
        CoreSDKHelper.get().addOnCallListener(listener);
    }
    /**
     * 移除电话事件监听
     */
    public static void removeOnCallListener(OnCallListener listener) {
        CoreSDKHelper.get().removeOnCallListener(listener);
    }

    /**
     * 拨号
     * @param userId 对方的UserId
     * @return 会话唯一标识符
     */
    public static String requestCall(String userId) {
        return CoreSDKHelper.get().requestCall(userId);
    }

    /**
     * 接受来电
     * @param callId 会话唯一标识符
     */
    public static void acceptCall(String callId) {
        CoreSDKHelper.get().acceptCall(callId);
    }

    /**
     * 挂断电话
     * @param callId 会话唯一标识符
     */
    public static void hangCall(String callId) {
        CoreSDKHelper.get().hangCall(callId);
    }

    /**
     * 获取当前正在通话的唯一标识
     * @return 会话唯一标识符
     */
    public static String getCurrentCallId() {
        return CoreSDKHelper.get().getCurrentCallId();
    }

}
