package com.imkarl.call.audio;

/**
 * 监听Voip的各种状态
 * @author imkarl 2016-09
 */
public interface OnCallListener {
    void onCallEvents(CallEvent event);
}
