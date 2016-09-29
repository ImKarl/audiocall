package com.imkarl.call.audio;

import android.net.Uri;

/**
 * 配置
 * @author imkarl 2016-09
 */
public class CallConfig {
    /**
     * appkey
     */
    private String appKey;
    /**
     * Token
     */
    private String appToken;

    //目前支持下面四种资源查找方式(假如资源名称为phonering.mp3)
    // 1、如果是assets目录则设置为[assets://phonering.mp3]
    // 2、如果是raw目录则设置为[raw://+ R.raw.phonering]
    // 3、如果是SDCard目录则设置为[file:///mnt/sdcard/phonering.mp3]
    // 4、如果是content资源则设置为[content://media/internal/audio/media/5712-31]（系统来电铃声）

    /**
     * 来电铃音
     */
    private Uri incomingRing;
    /**
     * 去电(呼叫)铃音
     */
    private Uri dialRing;
    /**
     * 占线铃音
     */
    private Uri busyRing;



    public CallConfig() {
        init();
    }
    public CallConfig(String appKey, String appToken) {
        this.appKey = appKey;
        this.appToken = appToken;
        init();
    }
    public CallConfig(String appKey, String appToken, Uri incomingRing, Uri dialRing, Uri busyRing) {
        this();
        this.appKey = appKey;
        this.appToken = appToken;
        this.incomingRing = incomingRing;
        this.dialRing = dialRing;
        this.busyRing = busyRing;
        init();
    }
    private void init() {
        if (incomingRing == null) {
            this.incomingRing = Uri.parse("assets://incoming.ogg");
        }
        if (dialRing == null) {
            this.dialRing = Uri.parse("assets://outgoing.ogg");
        }
        if (busyRing == null) {
            this.busyRing = Uri.parse("assets://busy.ogg");
        }
    }



    @Override
    public String toString() {
        return "CallConfig{" +
                "appKey='" + appKey + '\'' +
                ", appToken='" + appToken + '\'' +
                ", incomingRing=" + incomingRing +
                ", dialRing=" + dialRing +
                ", busyRing=" + busyRing +
                '}';
    }



    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public Uri getIncomingRing() {
        return incomingRing;
    }

    public void setIncomingRing(Uri incomingRing) {
        this.incomingRing = incomingRing;
    }

    public Uri getDialRing() {
        return dialRing;
    }

    public void setDialRing(Uri dialRing) {
        this.dialRing = dialRing;
    }

    public Uri getBusyRing() {
        return busyRing;
    }

    public void setBusyRing(Uri busyRing) {
        this.busyRing = busyRing;
    }
}
