package com.imkarl.call.audio.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.imkarl.call.audio.AudioCall;
import com.imkarl.call.audio.CallEvent;
import com.imkarl.call.audio.OnCallListener;
import com.imkarl.call.audio.core.L;

/**
 * 语音通话
 * @author imkarl 2016-09
 */
public class AnduioCallActivity extends Activity {

    public static CallEvent callEvent;

    public static Intent intent(Context context, CallEvent event) {
        callEvent = event;
        Intent intent = new Intent(context, AnduioCallActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Button button = new Button(this);
        button.setText("点击接听");

        final Button button2 = new Button(this);
        button2.setTextColor(Color.RED);
        button2.setText("取消");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(button);
        layout.addView(button2);
        setContentView(layout);

        L.e("callEvent="+callEvent);
        if (callEvent == null) {
            finish();
            return;
        }

        button.setText("来电类型："+callEvent.getDirection()+"\n对方号码："+callEvent.getUserId()+"\n点击接听");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AudioCall.acceptCall(callEvent.getCallId());
                button.setVisibility(View.INVISIBLE);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    AudioCall.hangCall(callEvent.getCallId());
                } catch (Exception e) { }
                finish();
            }
        });

        AudioCall.addOnCallListener(new OnCallListener() {
            @Override
            public void onCallEvents(CallEvent event) {
                L.e(event);
                switch (event.getType()) {
                    case FINISH:
                        finish();
                        break;
                }
            }
        });
    }

}
