package com.sinch.apptoappcall;

import android.Manifest;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Window;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class CallActivity extends AppCompatActivity {

    private static final String APP_KEY = "17e6c001-6385-4a9c-b4a1-4f583e706e1f";
    private static final String APP_SECRET = "aA8GAyzT9EGTJX7XUPkjlA==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";

    private Call call;
    private TextView callState;
    private SinchClient sinchClient;
    private Button button;
    private String callerId;
    private String recipientId;
    private PowerManager.WakeLock wl;
    private Socket mSocket;
    private static final String URL = "http://192.168.77.39:3000";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_PHONE_STATE},
                1);
        PowerManager pm = (PowerManager) getSystemService(service.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "My Tag");
        wl.acquire();
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.call);

        Intent intent = getIntent();
//        callerId = intent.getStringExtra("callerId");
//        recipientId = intent.getStringExtra("recipientId");

//        sinchClient = Sinch.getSinchClientBuilder()
//                .context(this)
//                .userId("a")
//                .applicationKey(APP_KEY)
//                .applicationSecret(APP_SECRET)
//                .environmentHost(ENVIRONMENT)
//                .build();
//
//        sinchClient.setSupportCalling(true);
//        sinchClient.startListeningOnActiveConnection();
//        sinchClient.start();
////        bo cai nay khi call tren man hinh khong bi nhay nhay do call service 2 lan
//        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
//        try {
//            mSocket = IO.socket(URL);
//        } catch (URISyntaxException e) {
//            throw new RuntimeException(e);
//        }
//        ChatApplication app = (ChatApplication)getApplication();
//        mSocket = app.getmSocket();
//        mSocket.connect();
//        if (mSocket.connected()){
//            Toast.makeText(getApplicationContext(), "Connected!!",Toast.LENGTH_SHORT).show();
//        }else {
//            Toast.makeText(getApplicationContext(), "not Connect!",Toast.LENGTH_SHORT).show();
//        }
//        mSocket.emit("join","dungna1");
//        mSocket.on("message", new Emitter.Listener() {
//            @Override
//            public void call(Object... args) {
//                JSONObject data = (JSONObject)args[0];
//here the data is in JSON Format
//                Toast.makeText(getApplicationContext(), data.toString(), Toast.LENGTH_SHORT).show();
//                String message = "chay app khi detect comming call";
//                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                Intent dialogIntent = new Intent(getApplicationContext(), CallActivity.class);
//                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                startActivity(dialogIntent);
//                runOnUiThread(new Runnable() {
//                    @Override public void run() {
//                        String message = "chay app tu call comming detected";
//                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//                        Intent dialogIntent = new Intent(getApplicationContext(), CallActivity.class);
//                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        startActivity(dialogIntent);
//                    }
//                });
//            }
//        });

        button = (Button) findViewById(R.id.button);
        callState = (TextView) findViewById(R.id.callState);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startServiceIntent = new Intent(getApplicationContext(), service.class);
                getApplicationContext().startService(startServiceIntent);
//                mSocket.emit("messagedetection","dungna1","detected comming call");
//                if (call == null) {
//
//                    call = sinchClient.getCallClient().callUser("b");
//                    call.addCallListener(new SinchCallListener());
//                    button.setText("Hang Up");
//                } else {
//                    call.hangup();
//                }
            }
        });
    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
            button.setText("Call");
            callState.setText("");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            callState.setText("connected");
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            callState.setText("ringing");
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            Toast.makeText(CallActivity.this, "incoming call", Toast.LENGTH_SHORT).show();
            call.answer();
            call.addCallListener(new SinchCallListener());
            button.setText("Hang Up");
        }
    }
}

