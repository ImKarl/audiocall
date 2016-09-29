package com.imkarl.call.audio.core;

import com.imkarl.call.audio.CallEvent;
import com.imkarl.call.audio.EventType;
import com.yuntongxun.ecsdk.ECVoIPCallManager;
import com.yuntongxun.ecsdk.VideoRatio;
import com.yuntongxun.ecsdk.VoipMediaChangedInfo;

/**
 * 监听Voip的各种状态
 * @author imkarl 2016-09
 */
public abstract class SimpleOnVoipListener implements ECVoIPCallManager.OnVoIPListener {
    @Override
    public final void onVideoRatioChanged(VideoRatio videoRatio) {
    }
    @Override
    public final void onSwitchCallMediaTypeRequest(String s, ECVoIPCallManager.CallType callType) {
    }
    @Override
    public final void onSwitchCallMediaTypeResponse(String s, ECVoIPCallManager.CallType callType) {
    }
    @Override
    public final void onDtmfReceived(String s, char c) {
    }
    @Override
    public final void onMediaDestinationChanged(VoipMediaChangedInfo voipMediaChangedInfo) {
    }
    @Override
    public final void onCallEvents(ECVoIPCallManager.VoIPCall voipCall) {
        // 处理呼叫事件回调
        if(voipCall == null) {
            return ;
        }

        CallEvent event = new CallEvent();
        event.setCallId(voipCall.callId);
        event.setCode(voipCall.reason);
        if (voipCall.direct == ECVoIPCallManager.ECCallDirect.EC_OUTGOING) {
            event.setDirection(CallEvent.Direction.DIAL);
            event.setUserId(voipCall.called);
        } else {
            event.setDirection(CallEvent.Direction.INCOMING);
            event.setUserId(voipCall.caller);
        }

        // 根据不同的事件通知类型来处理不同的业务
        ECVoIPCallManager.ECCallState callState = voipCall.callState;
        EventType state = null;
        switch (callState) {
            case ECCALL_PROCEEDING:
                // 正在连接服务器处理呼叫请求
                state = EventType.CALLING;
                break;
            case ECCALL_ALERTING:
                // 呼叫到达对方客户端，对方正在振铃
                state = EventType.ALERTING;
                break;
            case ECCALL_ANSWERED:
                // 对方接听本次呼叫
                state = EventType.ANSWERED;
                break;
            case ECCALL_PAUSED:
                // 暂停
                state = EventType.PAUSED;
                break;
            case ECCALL_PAUSED_BY_REMOTE:
                // 暂停
                state = EventType.PAUSED_BY_REMOTE;
                break;
            case ECCALL_FAILED:
                // 本次呼叫失败，根据失败原因播放提示音
                state = EventType.FAILED;
                break;
            case ECCALL_RELEASED:
                // 通话释放[完成一次呼叫]
                state = EventType.FINISH;
                break;
        }
        event.setType(state);

        // 发送通知
        onCallEvents(event);
    }

    abstract void onCallEvents(CallEvent event);
}
