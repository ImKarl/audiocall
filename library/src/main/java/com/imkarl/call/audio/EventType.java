package com.imkarl.call.audio;

/**
 * 事件类型
 * @author imkarl 2016-09
 */
public enum  EventType {
    /** 呼叫中 */
    CALLING,
    // 呼叫到达对方客户端，对方正在振铃
    ALERTING,
    // 对方接听本次呼叫
    ANSWERED,
    // 暂停
    PAUSED,
    // 暂停
    PAUSED_BY_REMOTE,
    // 通话完成
    FINISH,
    // 呼叫失败
    FAILED;
}
