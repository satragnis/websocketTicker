package com.example.bitcointicker.Application;

import android.app.Application;

import com.example.bitcointicker.Socket.SocketConnection;

public class MyApplication extends Application {
    public static SocketConnection socketConnection;

    public void connectSocket() {
        socketConnection = getDefault();
        socketConnection.connectSocket(getApplicationContext());
    }



    public void subscribeTo(String eventType) {
        socketConnection = getDefault();
        socketConnection.subscribeTicker(getApplicationContext(),eventType);
    }

    public void unsubscribeFrom(String eventType) {
        socketConnection = getDefault();
        socketConnection.unsubscribeTicker(getApplicationContext(),eventType);
    }



    public SocketConnection getDefault(){
        if(socketConnection!=null){
            return socketConnection;
        }else{
            return new SocketConnection();
        }
    }

}
