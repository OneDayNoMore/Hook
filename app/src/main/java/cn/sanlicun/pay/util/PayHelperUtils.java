package cn.sanlicun.pay.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.sanlicun.pay.Constans;
import de.robv.android.xposed.XposedHelpers;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/8/8
 * 描述 ：
 * ============================================
 */

public class PayHelperUtils {

    public static String getCookieStr(ClassLoader paramClassLoader) {
        XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transportext.biz.appevent.AmnetUserInfo", paramClassLoader), "getSessionid", new Object[0]);
        Context localContext = (Context) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transportext.biz.shared.ExtTransportEnv", paramClassLoader), "getAppContext", new Object[0]);
        if (localContext != null) {
            if (XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.helper.ReadSettingServerUrl", paramClassLoader), "getInstance", new Object[0]) != null) {
                return (String) XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.alipay.mobile.common.transport.http.GwCookieCacheHelper", paramClassLoader), "getCookie", new Object[]{".alipay.com"});
            }
            Log.i("tag", "异常readSettingServerUrl为空");
            return "";
        }
        Log.i("tag", "异常context为空");
        return "";
    }

    public static void getTradeInfo(final Context paramContext, final String paramString) {
        Log.i("tag", "getTradeInfo:1 ");
        long l = System.currentTimeMillis();
        String str1 = getCurrentDate();
        String str2 = "https://mbillexprod.alipay.com/enterprise/simpleTradeOrderQuery.json?beginTime=" + (l - 864000000L) + "&limitTime=" + l + "&pageSize=20&pageNum=1&channelType=ALL";
        HttpUtils localHttpUtils = new HttpUtils(15000);
        localHttpUtils.configResponseTextCharset("GBK");
        RequestParams localRequestParams = new RequestParams();
        localRequestParams.addHeader("Cookie", paramString);
        localRequestParams.addHeader("Referer", "https://render.alipay.com/p/z/merchant-mgnt/simple-order.html?beginTime=" + str1 + "&endTime=" + str1 + "&fromBill=true&channelType=ALL");
        localHttpUtils.send(HttpRequest.HttpMethod.GET, str2, localRequestParams, new RequestCallBack() {
            public void onFailure(HttpException paramAnonymousHttpException, String paramAnonymousString) {
//                PayHelperUtils.sendmsg(PayHelperUtils.this, "服务器异常" + paramAnonymousString);
                Log.i("tag", "服务器异常");
            }

            public void onSuccess(ResponseInfo paramAnonymousResponseInfo) {
                String result = (String) paramAnonymousResponseInfo.result;
                try {
                    Log.i("tag", "getTradeInfo2" + result);
                    JSONArray array = new JSONObject(result).getJSONObject("result").getJSONArray("list");
                    if ((array != null) && (array.length() > 0)) {
                        String json = array.getJSONObject(0).getString("tradeNo");
                        Log.i("tag", "getTradeInfo3" + json);
                        Intent localIntent = new Intent();
                        localIntent.putExtra("tradeno", json);
                        localIntent.putExtra("cookie", paramString);
                        localIntent.setAction(Constans.ACTION_PAY_SUCCESS);
                        paramContext.sendBroadcast(localIntent);
//                        EventBus.getDefault().post(new MessageEvent(MessageEvent.MessageType.TRADENORECEIVED_ACTION,json,paramString));
                    }
                    return;
                } catch (Exception p) {
//                    PayHelperUtils.sendmsg(PayHelperUtils.this, "getTradeInfo异常" + paramAnonymousResponseInfo.getMessage());
                    Log.i("tag", "getTradeInfo异常" + p.getMessage());
                }
            }
        });
    }

    public static String getCurrentDate() {
        Date localDate = new Date(System.currentTimeMillis());
        return new SimpleDateFormat("yyyy-MM-dd").format(localDate);
    }

    public static void getPay(String money, String mark, String type, Context context) {
        Intent intent = new Intent();
        if (type.equals("alipay")) {
            intent.setAction(Constans.ACTION_LAUNCH_ALIPAY_WALLET);
            intent.putExtra(Constans.MARK, mark);
            intent.putExtra(Constans.MONEY, money);
        }
        context.sendBroadcast(intent);


    }
    public static String getVerName(Context paramContext)
    {
        try
        {
            String str = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionName;
            return str;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {
//            sendmsg(paramContext, "getVerName异常" + localNameNotFoundException.getMessage());
        }
        return "";
    }

    public static void sendmsg(Context paramContext, String paramString)
    {
        Log.i("tag", "sendmsg: "+paramString);
        Intent localIntent = new Intent();
        localIntent.putExtra("msg", paramString);
        localIntent.setAction(Constans.MESSAGE_RECEIVED_ACTION);
        paramContext.sendBroadcast(localIntent);
    }

}
