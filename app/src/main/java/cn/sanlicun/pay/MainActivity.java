package cn.sanlicun.pay;


import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import cn.sanlicun.pay.web.WebService;


public class MainActivity extends AppCompatActivity {


        private static TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        tv= (TextView) findViewById(R.id.info);
        Intent intent = new Intent(this, WebService.class);
        startService(intent);


        registerMessageReceiver();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startAlipay();

            }
        });

        findViewById(R.id.btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startWechat();

            }
        });
        findViewById(R.id.btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
            }
        });

        tv= (TextView) findViewById(R.id.info);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }


    private void startWechat() {
        Intent intent = new Intent(Constans.ACTION_LAUNCH_WECHAT_WALLET);
        intent.putExtra(Constans.MARK, UUID.randomUUID().toString());
        intent.putExtra(Constans.MONEY, "0.01");
        sendBroadcast(intent);
    }

    void startAlipay() {
        isRunning();
        Intent intent = new Intent(Constans.ACTION_LAUNCH_ALIPAY_WALLET);
        intent.putExtra(Constans.MARK, "我是一个测试demo");
        intent.putExtra(Constans.MONEY, "0.01");
        sendBroadcast(intent);

    }


    public boolean isRunning() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(Integer.MAX_VALUE)) {
            String pkgName = rsi.service.getPackageName();
            if (pkgName.equals(Constans.ALIPAY_PACKAGE)) {
                if (rsi.process.equals(Constans.ALIPAY_PACKAGE)) {
                    return true;
                }
            }
        }
        return false;
    }



    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Constans.MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("tag", "MessageReceiver");
            try {
                if (Constans.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra("msg");
                    Log.i("tag", "msg: "+messge);
                    setCostomMsg(messge);
                }
            } catch (Exception e) {
            }
        }
    }



    public static void setCostomMsg(String paramString)
    {
        Message message = new Message();
        message.what = 1;
        Bundle localBundle = new Bundle();
        Date localDate = new Date(System.currentTimeMillis());
        localBundle.putString("log", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(localDate) + ":" + "  结果:" + paramString);
        message.setData(localBundle);
        try
        {
            Log.i("tag","setCostomMsg");
            handler.sendMessage(message);
            return;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static Handler handler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            Log.i("tag", "handleMessage: ");
            String str =msg.getData().getString("log");
                super.handleMessage(msg);
                if(str==null) return;
                tv.setText(tv.getText().toString()+"\r\n"+str);
//            if(str.length()>2000){
//                tv.setText("");
//                str="";
//            }
//            tv.setText(str+"\r\n"+msg.getData().getString("log"));
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent intent = new Intent(this, WebService.class);
        stopService(intent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(false);
//        super.onBackPressed(); //注释super,拦截返回键功能
    }
}
