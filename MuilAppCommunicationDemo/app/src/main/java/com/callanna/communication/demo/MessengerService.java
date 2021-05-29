package com.callanna.communication.demo;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     author : callannaduan
 *     e-mail : rulan.duan@tuya.com
 *     time   : 2021/05/28
 *     desc   :
 *     version: 1.0
 * </pre>
 */
public class MessengerService extends Service {

    private static MessengerService mMessengerService;
    private static List<IMsgReceiverListener> msgReceiverListenerlist = new ArrayList<>();
    //记录下客户端发过来的Messenger
    private Messenger sendMessenger ;
    //接受客户端发过来的消息
    private Handler MessengerHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            //消息处理.......
            String message = msg.getData().getString("data");
            Log.d(MessengerService.class.getName(), message);
            for(IMsgReceiverListener msgReceiverListener:msgReceiverListenerlist) {
                if (msgReceiverListener != null) {
                    msgReceiverListener.onReceive(message);
                }
            }
            sendMessenger = msg.replyTo;
            //回复
            sendMsg("你好，我收到了客户端的信息");
            super.handleMessage(msg);
        }
    };
    public void sendMsg(String str){
        if(sendMessenger != null) {
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putString("data", str);
            message.setData(bundle);
            try {
                sendMessenger.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    //创建服务端Messenger
    private final Messenger mMessenger = new Messenger(MessengerHandler);

    @Override
    public IBinder onBind(Intent intent) {
        //向客户端返回Ibinder对象，客户端利用该对象访问服务端

        return mMessenger.getBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMessengerService = this;
    }

    public static void addMsgReceiverListener(IMsgReceiverListener msgReceiverListener) {
        msgReceiverListenerlist.add(msgReceiverListener);
    }

    public static MessengerService getMessengerService() {
        return mMessengerService;
    }
    interface IMsgReceiverListener{
        void onReceive(String msg);
    }
}
