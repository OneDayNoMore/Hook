package cn.sanlicun.pay.hook;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import cn.sanlicun.pay.Constans;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 小饭 on 2018/7/11.
 */

public class AliBillPlugin implements ISubPlugin {
    @Override
    public void load(XC_LoadPackage.LoadPackageParam loadPackageParam, final Context ctx) {
        Log.i("tag","AliBillPlugin");


        XposedBridge.hookAllMethods(XposedHelpers.findClass(Constans.ALI_SQL, loadPackageParam.classLoader), "insertMessageInfo", new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                Log.i("tag","123");
                String str1 = (String) XposedHelpers.callMethod(param.args[0], "toString");

                String str2 = str1.replace("content='", "'");
                if ((str2.contains("二维码收款")) || (str2.contains("收到一笔转账"))) {
                    JSONObject localJSONObject = JSONObject.parseObject(str2);
                    String str3 = localJSONObject.getString("content").replace("￥", "");
                    String str4 = localJSONObject.getString("assistMsg2");
                    String str5 = str1.replace("tradeNO=", "&");
                    // g.a("收到支付宝支付订单：" + str5 + "==" + str3 + "==" + str4);
                    Intent localIntent = new Intent();
                    localIntent.putExtra(Constans.BILL_NO, str5);
                    localIntent.putExtra(Constans.BILL_MONEY, str3);
                    localIntent.putExtra(Constans.BILL_MARK, str4);
                    localIntent.putExtra(Constans.BILL_TYPE, Constans.ALIPAY);
                    localIntent.setAction(Constans.ACTION_PAY_SUCCESS);
                    ctx.sendBroadcast(localIntent);
                }

            }
        });





    }

}
