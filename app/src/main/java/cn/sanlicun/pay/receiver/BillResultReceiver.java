package cn.sanlicun.pay.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import cn.sanlicun.pay.param.PushParam;
import cn.sanlicun.pay.sqlite.DBManager;
import cn.sanlicun.pay.util.PayHelperUtils;

public class BillResultReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
//        Log.i("tag", "BillResultReceiver");
//        String money = intent.getStringExtra(Constans.BILL_MONEY);
//        String mark = intent.getStringExtra(Constans.BILL_MARK);
//        String type = intent.getStringExtra(Constans.BILL_TYPE);
//        String billNo = intent.getStringExtra(Constans.BILL_NO);
//        PushParam pushParam = new PushParam();
//        pushParam.setMark(mark);
//        pushParam.setMoney(money);
//        pushParam.setBillNo(billNo);
//        pushParam.setType(type);
//        Log.i("tag", "BillResultReceiver: mark:"+mark+"money:"+money+"billno:"+no+"type:"+type);

        final String tradeno = intent.getStringExtra("tradeno");
        String cookie = intent.getStringExtra("cookie");
        String url = "https://tradeeportlet.alipay.com/wireless/tradeDetail.htm?tradeNo=" + tradeno + "&source=channel&_from_url=https%3A%2F%2Frender.alipay.com%2Fp%2Fz%2Fmerchant-mgnt%2Fsimple-order._h_t_m_l_%3Fsource%3Dmdb_card";
        HttpUtils localHttpUtils = new HttpUtils(15000);
        localHttpUtils.configResponseTextCharset("GBK");
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.addHeader("Cookie", cookie);
        localHttpUtils.send(HttpRequest.HttpMethod.GET, url, localRequestParams, new RequestCallBack() {
            @Override
            public void onSuccess(ResponseInfo responseInfo) {
                Log.i("tag", "onMessageEvent:" + responseInfo.result);
                Object localObject = Jsoup.parse((String) responseInfo.result).getElementsByClass("trade-info-value");
                if (((Elements) localObject).size() >= 5) {
                    String paramAnonymousResponseInfo = ((Elements) localObject).get(0).ownText();
                    String xx = ((Elements) localObject).get(1).ownText();
                    String xxx = ((Elements) localObject).get(2).ownText();
                    localObject = ((Elements) localObject).get(3).ownText();
                    PayHelperUtils.sendmsg(context,"付款成功alipay:订单号:"+ tradeno + "金额：" + paramAnonymousResponseInfo + "备注：" + (String) localObject);
                    Log.i("tag", "收到支付宝订单,订单号：" + tradeno + "金额：" + paramAnonymousResponseInfo + "备注：" + (String) localObject+"xx:"+xx+"xxx:"+xxx);
                    PushParam pushParam = new PushParam();
                    pushParam.setMark((String) localObject);
                    pushParam.setMoney(paramAnonymousResponseInfo);
                    pushParam.setBillNo(tradeno);
                    pushParam.setType("alipay");
                    pushParam.setState(1);
                    DBManager dbManager=new DBManager(context);
                    dbManager.updatePay(pushParam);
//                    Api.PUSH_ORDER_RESULT(context, pushParam, String.class, 1);
                }

            }

            @Override
            public void onFailure(HttpException error, String msg) {
                Log.i("tag", "服务器异常1");
            }
        });


    }
}
