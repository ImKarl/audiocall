package com.imkarl.call.audio.core;

import android.app.Activity;
import android.os.Bundle;

import com.imkarl.call.audio.CallEvent;
import com.imkarl.call.audio.EventType;
import com.yuntongxun.ecsdk.ECDevice;

/**
 * @author imkarl 2016-09
 */
public class InnerAudioCallActivity extends Activity {

    public static final String EXTRA_OUTGOING_CALL = "EXTRA_OUTGOING_CALL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras =  getIntent().getExtras();
        if (extras != null) {
            //获取是否是呼入还是呼出
            boolean mIncomingCall = !(getIntent().getBooleanExtra(EXTRA_OUTGOING_CALL, false));
            L.e("mIncomingCall="+mIncomingCall);

            //获取当前的callid
            final String mCallId = getIntent().getStringExtra(ECDevice.CALLID);

            //获取对方的号码
            final String mCallNumber = getIntent().getStringExtra(ECDevice.CALLER);
            L.e("mCallNumber="+mCallNumber);

            CallEvent event = new CallEvent();
            event.setCallId(mCallId);
            event.setUserId(mCallNumber);
            event.setCode(0);
            event.setDirection(mIncomingCall ? CallEvent.Direction.INCOMING : CallEvent.Direction.DIAL);
            event.setType(EventType.CALLING);
            CoreSDKHelper.get().doCallEvents(event);
        }

        finish();
    }

}
