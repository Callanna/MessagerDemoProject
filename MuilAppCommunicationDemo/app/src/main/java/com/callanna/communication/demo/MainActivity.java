package com.callanna.communication.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText editMsg;
    private Button btnSend;
    private TextView tvreceiveMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editMsg = findViewById(R.id.edittext);
        tvreceiveMsg = findViewById(R.id.tv_msg);
        btnSend = findViewById(R.id.btn_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editMsg.getText().toString();
                if(!TextUtils.isEmpty(msg)) {
                   sendMssage(msg);
                }else{
                    Toast.makeText(getBaseContext(),"发送内容不能为空",Toast.LENGTH_SHORT);
                }
            }

        });
        MessengerService.addMsgReceiverListener(new MessengerService.IMsgReceiverListener() {
            @Override
            public void onReceive(String msg) {
                tvreceiveMsg.setText(msg);
            }
        });
    }

    private void sendMssage(String msg) {
        if(MessengerService.getMessengerService() != null){
            MessengerService.getMessengerService().sendMsg(msg);
        }else{
            Toast.makeText(getBaseContext(),"没有人可以联系",Toast.LENGTH_SHORT);
        }
    }

    @Override
    protected void onDestroy(){

        super.onDestroy();
    }
}