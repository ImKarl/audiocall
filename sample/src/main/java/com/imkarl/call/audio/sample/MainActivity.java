package com.imkarl.call.audio.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.imkarl.call.audio.AudioCall;
import com.imkarl.call.audio.CallConfig;
import com.imkarl.call.audio.CallEvent;
import com.imkarl.call.audio.OnCallListener;
import com.imkarl.call.audio.OnConnectListener;
import com.imkarl.call.audio.core.L;

public class MainActivity extends AppCompatActivity {

    // 用户ID，唯一标识符
    private String userId = "2";
    private CallConfig mConfig = new CallConfig("you_key", "you_token");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AudioCall.init(this, mConfig);
        AudioCall.addOnCallListener(new OnCallListener() {
            @Override
            public void onCallEvents(CallEvent event) {
                L.e(event);

                switch (event.getType()) {
                    case CALLING:
                        startActivity(AnduioCallActivity.intent(MainActivity.this, event));
                        break;
                    case FAILED:
                        Toast.makeText(MainActivity.this, "通话失败", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
        AudioCall.setOnConnectListener(new OnConnectListener() {
            @Override
            public void onConnecting() {
                L.e("onConnecting");
            }
            @Override
            public void onConnected() {
                L.e("onConnected");
            }
            @Override
            public void onLogout() {
                L.e("onLogout");
            }
            @Override
            public void onError(Exception e) {
                L.e("onError");
                L.e(e);
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        AudioCall.login(userId);

        ((TextView)findViewById(R.id.tv_id)).setText("你的ID："+userId);

        // 登陆成功
        findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String toCallId = ((EditText)findViewById(R.id.et_call)).getText().toString();
                if (TextUtils.isEmpty(toCallId)) {
                    Toast.makeText(MainActivity.this, "对方ID不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                L.e("toCallId="+toCallId);
                AudioCall.requestCall(toCallId);
            }
        });
    }

}
