package com.example.bitcointicker.Socket;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.util.Log;


import com.example.bitcointicker.Model.Block;
import com.example.bitcointicker.Model.MessageEvent;
import com.example.bitcointicker.Model.Transaction;
import com.example.bitcointicker.Utils.NaiveSSLContext;
import com.example.bitcointicker.Utils.Params;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.neovisionaries.ws.client.WebSocketState;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javax.net.ssl.SSLContext;



public class SocketConnection {


//    ArrayList<TargetCurrencyStats> targetCurrencyStatsAL = new ArrayList<>();

    //static
    public boolean wssConnection = false;
    public boolean socketConnected = false;

    public static WebSocket ws;

    public void connectSocket(final Context applicationContext) {
        try {
            EventBus.getDefault().post(new MessageEvent(WebSocketEvents.SOCKET_INFO,Params.SOCKET_CONNECTING));
            WebSocketFactory factory = new WebSocketFactory();
            // Create a custom SSL context.
            SSLContext context = null;
            context = NaiveSSLContext.getInstance("TLS");
            // Set the custom SSL context.
            factory.setSSLContext(context);

            // Disable manual hostname verification for NaiveSSLContext.
            // Manual hostname verification has been enabled since the
            // version 2.1. Because the verification is executed manually
            // after Socket.connect(SocketAddress, int) succeeds, the
            // hostname verification is always executed even if you has
            // passed an SSLContext which naively accepts any server
            // certificate. However, this behavior is not desirable in
            // some cases and you may want to disable the hostname
            // verification. You can disable the hostname verification
            // by calling WebSocketFactory.setVerifyHostname(false).
            factory.setVerifyHostname(false);
            if (ws == null) {
//                ws = factory.createSocket(applicationContext.getResources().getString(R.string.PUSHER_URL));
                ws = factory.createSocket("wss://ws.blockchain.info/inv");
                // Send a ping per 60 seconds.
//            ws.setPingInterval(5 * 1000);

                // Stop the periodical sending.
                //ws.setPingInterval(0);
                // Register a listener to receive WebSocket events.
                ws.addListener(new WebSocketAdapter() {
                    @Override
                    public void onTextMessage(WebSocket websocket, String message) throws Exception {
                        // Received a text message.
//                    showDisplay(message);
                        Log.d(Params.TAG, "onTextMessage: >>>>>>" + message);
                        final JSONObject data = new JSONObject(message);
                        try {
                            if (data.has("op")) {
                                if (data.getString("op").equalsIgnoreCase("pong")) {
                                    wssConnection = true;
                                }else if(data.getString("op").equalsIgnoreCase("block")){
                                    JSONObject actualData = data.getJSONObject("x");
                                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                                    Block block = gson.fromJson(actualData.toString(),Block.class);
                                    EventBus.getDefault().post(new MessageEvent(WebSocketEvents.BLOCK_SUB,block,null));
                                }else if(data.getString("op").equalsIgnoreCase("utx")){
                                    Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).create();
                                    Transaction transaction = gson.fromJson(data.toString(), Transaction.class);
                                    EventBus.getDefault().post(new MessageEvent(WebSocketEvents.UNCONFIRMED_TSUB,null,transaction));
                                }

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                        super.onConnected(websocket, headers);
//                    showDisplay("connected");
                        Log.d(Params.TAG, "onConnected: >>>>>connected");
                        EventBus.getDefault().post(new MessageEvent(WebSocketEvents.SOCKET_INFO,Params.SOCKET_CONNECTED));
//                        subscribeTicker("inr",applicationContext);
                        checkWSConnection(applicationContext);
                        final Handler handler = new Handler();
                        socketConnected = true;
                    }

                    @Override
                    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
                        EventBus.getDefault().post(new MessageEvent(WebSocketEvents.SOCKET_INFO,Params.SOCKET_DISCONNECTED));
                        ws = ws.recreate().connect();
                        socketConnected = false;
                        connectSocket(applicationContext);
//                        subscribeTicker("inr",applicationContext);
                    }

                    @Override
                    public void onConnectError(WebSocket websocket, WebSocketException exception) throws Exception {
                        super.onConnectError(websocket, exception);
//                    showDisplay(exception.toString());
                        Log.d(Params.TAG, "onConnectError: " + exception.toString());
                        socketConnected = false;
                    }

                    @Override
                    public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                        super.onError(websocket, cause);
//                    showDisplay(cause.toString());
                        Log.d(Params.TAG, "onError: " + cause.toString());
                        socketConnected = false;
                    }


                    @Override
                    public void onFrameError(WebSocket websocket, WebSocketException cause, WebSocketFrame frame) throws Exception {
                        super.onFrameError(websocket, cause, frame);
                        Log.d(Params.TAG, "onFrameError: " + cause.toString());
                    }

                    @Override
                    public void onFrameSent(WebSocket websocket, WebSocketFrame frame) throws Exception {
                        super.onFrameSent(websocket, frame);
                        Log.d(Params.TAG, "onFrameSent: " + frame.toString());
                    }

                    @Override
                    public void onUnexpectedError(WebSocket websocket, WebSocketException cause) throws Exception {
                        super.onUnexpectedError(websocket, cause);
                        Log.d(Params.TAG, "onUnexpectedError: " + cause.toString());
                    }

                    @Override
                    public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
                        super.onTextMessageError(websocket, cause, data);
                        Log.d(Params.TAG, "onTextMessageError: " + cause);
                    }

                    @Override
                    public void onStateChanged(WebSocket websocket, WebSocketState newState) throws Exception {
                        super.onStateChanged(websocket, newState);
                        Log.d(Params.TAG, "onStateChanged: " + newState.toString());
                    }

                    @Override
                    public void onFrameUnsent(WebSocket websocket, WebSocketFrame frame) throws Exception {
                        super.onFrameUnsent(websocket, frame);
                        Log.d(Params.TAG, "onFrameUnsent: " + frame.toString());
                    }

                    @Override
                    public void onPongFrame(WebSocket websocket, WebSocketFrame frame) throws Exception {
                        super.onPongFrame(websocket, frame);
                        Log.d(Params.TAG, "onPongFrame: " + frame.toString());
                    }
                });
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            ws.connect();
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.d(Params.TAG, "onCreate: Failed to establish a WebSocket connection.");
                            // Failed to establish a WebSocket connection.
                        }
                    }
                }).start();
            }
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    public void subscribeTicker(final Context context,final String eventType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject params = new JSONObject();
                    params.put("op", eventType);
                    Log.d(Params.TAG, "run: >>>> subscribed subscribeTicker" + params.toString());
                    if (ws != null) {
                        // Get a handler that can be used to post to the main thread
                        Handler mainHandler = new Handler(context.getMainLooper());
                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                hearbeatCheck(context);
                            } // This is your code
                        };
                        mainHandler.post(myRunnable);
                        new Timer().schedule(new TimerTask() {
                            @Override
                            public void run() {
                                // this code will be executed after 101 milliseconds
                                ws.sendText(params.toString());
                            }
                        }, 101);
                    } else {
                        if (socketConnected)
                            connectSocket(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void unsubscribeTicker(final Context context,final String eventType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final JSONObject params = new JSONObject();
                    params.put("op", eventType);
                    if (ws != null) {
                        // Get a handler that can be used to post to the main thread
                        Handler mainHandler = new Handler(context.getMainLooper());

                        Runnable myRunnable = new Runnable() {
                            @Override
                            public void run() {
                                hearbeatCheck(context);
                            } // This is your code
                        };
                        mainHandler.post(myRunnable);
                        if (isSocketConnected()) {
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    // this code will be executed after 101 milliseconds
                                    ws.sendText(params.toString());
                                }
                            }, 101);
                        }
                    } else {
                        if (socketConnected)
                            connectSocket(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void checkWSConnection(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject();
//                    jsonObject.put("method", "subscribe");
                    jsonObject.put("op", "ping");
                    if (ws != null) {
                        ws.sendText(jsonObject.toString());
                    } else {
                        Log.d(Params.TAG, "checkWSConnection: NULL");
                        connectSocket(context);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void hearbeatCheck(final Context context) {
        // Create the Handler object (on the main thread by default)
        final Handler handler = new Handler();
// Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "heartbeat Called on main thread");
                if (!wssConnection) {
                    connectSocket(context);
                }
                checkWSConnection(context);
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                handler.postDelayed(this, 5000);
            }
        };
// Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }


    public boolean isSocketConnected() {
        Log.d(Params.TAG, "isSocketConnected: " + ws.getSocket().isConnected());
        return ws.getSocket().isConnected();
    }




}
