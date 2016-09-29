package com.imkarl.call.audio;

/**
 * 连接监听器
 * @author imkarl 2016-09
 */
public interface OnConnectListener {

    void onConnecting();
    void onConnected();
    void onLogout();
    void onError(Exception e);

}
