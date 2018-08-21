package com.sinch.apptoappcall;

/**
 * Created by tuan on 2018/08/20.
 */

import android.app.Application;
import io.socket.client.IO;
import io.socket.client.Socket;
import java.net.URISyntaxException;
public class ChatApplication extends Application {
    private Socket mSocket;
    private static final String URL = "http://127.0.0.1:3000";
    @Override
    public void onCreate() {
        super.onCreate();
        try {
            mSocket = IO.socket(URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
    public Socket getmSocket(){
        return mSocket;
    }
}