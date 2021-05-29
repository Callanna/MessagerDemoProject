package com.callanna.communication.otherapp;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.callanna.communication.otherapp.databinding.FragmentFirstBinding;

import static android.content.Context.BIND_AUTO_CREATE;

public class FirstFragment extends Fragment {

    private static final String TAG = "FirstFragment";
    private FragmentFirstBinding binding;
    public Messenger messenger ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        connectService();
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(messenger != null) {
                    Message message = Message.obtain(null, 1);
                    //把自己的Messenger带进去 服务器拿到之后 可以直接通过这个对象发数据过来
                    message.replyTo = replyMessenger;
                    Bundle bundle = new Bundle();
                    bundle.putString("data", "服务器你好");
                    message.setData(bundle);
                    try {
                        //发送消息的方法
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
    /**
     * 和服务器建立连接 连接成功之后  messenger 就是发消息的对象。
     **/
    private void connectService(){
        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                Log.e(TAG, "链接成功！");
                messenger = new Messenger(service);
            }
            @Override
            public void onServiceDisconnected(ComponentName name) {
                Log.e(TAG, "链接断开！");
            }
        };
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.callanna.communication.demo",   "com.callanna.communication.demo.MessengerService"));
        getContext().bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }
    //这个Service的Messenger
    Messenger replyMessenger = new Messenger(new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            String data =  msg.getData().getString("data");
            Log.d(TAG,data);
            super.handleMessage(msg);
            binding.textviewFirst.setText(data);
        }
    });
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}