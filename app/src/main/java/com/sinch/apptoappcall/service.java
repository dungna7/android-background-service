package com.sinch.apptoappcall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import io.socket.client.IO;
import io.socket.client.Socket;
import android.media.AudioManager;
import android.view.WindowManager;
import android.os.PowerManager;
import io.socket.emitter.Emitter;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;

public class service extends Service {

    private Socket mSocket;
    private static final String TAG_BOOT_EXECUTE_SERVICE = "BOOT_BROADCAST_SERVICE";
    private static final String APP_KEY = "17e6c001-6385-4a9c-b4a1-4f583e706e1f";
    private static final String APP_SECRET = "aA8GAyzT9EGTJX7XUPkjlA==";
    private static final String ENVIRONMENT = "sandbox.sinch.com";
    private static final String URL = "http://192.168.77.39:3000";
    private Call call;
    private SinchClient sinchClient;
    public service() {
}

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {

//        sinchClient = Sinch.getSinchClientBuilder()
//                .context(this)
//                .userId("b")
//                .applicationKey(APP_KEY)
//                .applicationSecret(APP_SECRET)
//                .environmentHost(ENVIRONMENT)
//                .build();
//
//        sinchClient.setSupportCalling(true);
//        sinchClient.startListeningOnActiveConnection();
//        sinchClient.start();
//
//        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        try {
            mSocket = IO.socket(URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.connect();
        if (mSocket.connected()){
            Toast.makeText(getApplicationContext(), "Connected!!",Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "not Connect!",Toast.LENGTH_SHORT).show();
        }
        mSocket.emit("join","dungna1");
        mSocket.on("message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String message = "chay app tu call comming detected";
                mSocket.emit("join",message);
//                Toast.makeText(getApplicationContext(), message,Toast.LENGTH_SHORT).show();
                Intent dialogIntent = new Intent(service.this, CallActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(dialogIntent);
            }
        });
        super.onCreate();
        Log.d(TAG_BOOT_EXECUTE_SERVICE, "RunAfterBootService onCreate() method.");



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        String message = "RunAfterBootService onStartCommand() method.";

        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

        Log.d(TAG_BOOT_EXECUTE_SERVICE, "RunAfterBootService onStartCommand() method.");

        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onTaskRemoved(Intent rootIntent) {
//        super.onTaskRemoved(rootIntent);
//        Intent startServiceIntent = new Intent(this, service.class);
//        this.startService(startServiceIntent);
//
//    }

    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            SinchError a = endedCall.getDetails().getError();
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            String message = "ringing";
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> list) {

        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
//            call = incomingCall;


//            String message = "chay app khi detect comming call";
//            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
//            Intent dialogIntent = new Intent(service.this, CallActivity.class);
//            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//            startActivity(dialogIntent);
//            call.answer();
//            call.addCallListener(new SinchCallListener());
        }
    }
}