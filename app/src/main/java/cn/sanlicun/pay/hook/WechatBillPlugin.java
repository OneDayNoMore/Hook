package cn.sanlicun.pay.hook;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import cn.sanlicun.pay.Constans;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 小饭 on 2018/7/10.
 */

public class WechatBillPlugin implements ISubPlugin {
    @Override
    public void load(XC_LoadPackage.LoadPackageParam param, final Context ctx) {


        Object[] arrayOfObject1 = new Object[4];
        arrayOfObject1[0] = String.class;
        arrayOfObject1[1] = String.class;
        arrayOfObject1[2] = ContentValues.class;
        arrayOfObject1[3] = new XC_MethodHook() {
            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
            }

            protected void beforeHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
                Log.i("tag", "w3");
                try {
                    ContentValues localContentValues = (ContentValues) methodHookParam.args[2];
                    String str1 = (String) methodHookParam.args[0];

                    if (!TextUtils.isEmpty(str1)) {
                        if (!str1.equals("message"))
                            return;
                        Integer localInteger = localContentValues.getAsInteger("type");
                        if ((localInteger != null) && (localInteger.intValue() == 318767153)) {
                            String content = localContentValues.getAsString("content");
                            Log.i("tag","content:"+content);
                            JSONObject localJSONObject = JSONObject.parseObject(content).getJSONObject("msg");
                            Log.i("tag", "w4");
                            String str2 = localJSONObject.getJSONObject("appmsg").getJSONObject("mmreader").getJSONObject("template_detail").getJSONObject("line_content").getJSONObject("topline").getJSONObject("value").getString("word").replace("￥", "");
                            Log.i("tag", "w5" + str2);
                            String str3 = localJSONObject.getJSONObject("appmsg").getJSONObject("mmreader").getJSONObject("template_detail").getJSONObject("line_content").getJSONObject("lines").getJSONArray("line").getJSONObject(0).getJSONObject("value").getString("word");
                            Log.i("tag", "w6" + str3);
                            String str4 = localJSONObject.getJSONObject("appmsg").getString("template_id");
                            Log.i("tag", "w7" + str3);
                            //   g.a("收到微信支付订单：" + str4 + "==" + str2 + "==" + str3);
                            Intent localIntent = new Intent();
                            localIntent.putExtra(Constans.BILL_NO, str4);
                            localIntent.putExtra(Constans.BILL_MONEY, str2);
                            localIntent.putExtra(Constans.BILL_MARK, str3);
                            localIntent.putExtra(Constans.BILL_TYPE, Constans.WECHAT);
                            localIntent.setAction(Constans.ACTION_PAY_SUCCESS);
                            Log.i("tag", "w" + str1);
                            ctx.sendBroadcast(localIntent);

                            return;
                        }
                    }
                } catch (Exception localException) {

                }
            }
        };
        XposedHelpers.findAndHookMethod(Constans.WECHAT_SQL, param.classLoader, "insert", arrayOfObject1);

    }
}
