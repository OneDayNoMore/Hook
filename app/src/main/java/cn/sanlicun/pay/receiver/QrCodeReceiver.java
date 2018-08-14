package cn.sanlicun.pay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import cn.sanlicun.pay.Constans;
import cn.sanlicun.pay.param.PushParam;
import cn.sanlicun.pay.sqlite.DBManager;

public class QrCodeReceiver extends BroadcastReceiver  {

    @Override
    public void onReceive(Context context, Intent intent) {

        String money = intent.getStringExtra(Constans.MONEY);
        String mark = intent.getStringExtra(Constans.MARK);
        String type = intent.getStringExtra(Constans.TYPE);
        String payUrl = intent.getStringExtra(Constans.PAY_URL);
        if(mark.contains("test")) return;
        PushParam pushParam = new PushParam();
        pushParam.setMark(mark);
        pushParam.setMoney(money);
        pushParam.setPay_url(payUrl);
        pushParam.setType(type);
        pushParam.setState(0);
        Log.i("tag", "pay: "+payUrl);

        DBManager dbManager=new DBManager(context);
        dbManager.addPay(pushParam);
    }


}
