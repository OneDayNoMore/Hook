package cn.sanlicun.pay.hook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;

import cn.sanlicun.pay.Constans;
import cn.sanlicun.pay.util.PayHelperUtils;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 小饭 on 2018/7/11.
 * <p>
 * 只支持6.6.7
 */

public class WechatPayeePlugin implements ISubPlugin {
    @Override
    public void load(XC_LoadPackage.LoadPackageParam loadPackageParam, final Context context) {
        final ClassLoader classLoader = loadPackageParam.classLoader;
        try {
            XposedBridge.hookAllMethods(XposedHelpers.findClass("com.tencent.mm.plugin.collect.b.s", classLoader), "a", new XC_MethodHook() {
                protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                    Log.i("tag","w1");
                    if (PayHelperUtils.getVerName(context).equals("6.6.7")) {
                        double d = ((Double) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "hUL").get(paramAnonymousMethodHookParam.thisObject)).doubleValue();
                        String str = (String) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "desc").get(paramAnonymousMethodHookParam.thisObject);
                        String URL = (String) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "hUK").get(paramAnonymousMethodHookParam.thisObject);
                        XposedBridge.log(d + "  " + str + "  " + paramAnonymousMethodHookParam);
                        XposedBridge.log("调用增加数据方法==>微信");
                        Intent localIntent = new Intent();
                        localIntent.putExtra("money", d);
                        localIntent.putExtra("mark", str);
                        localIntent.putExtra("type", "wechat");
                        localIntent.putExtra("payurl", URL);
                        localIntent.setAction(Constans.QRCODE_RESULT);
                        context.sendBroadcast(localIntent);
                    }
                    while (!PayHelperUtils.getVerName(context).equals("6.6.6")) {
                        return;
                    }
                    double d = ((Double) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "llG").get(paramAnonymousMethodHookParam.thisObject)).doubleValue();
                    String str = (String) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "desc").get(paramAnonymousMethodHookParam.thisObject);
                    String pay = (String) XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "llF").get(paramAnonymousMethodHookParam.thisObject);
                    XposedBridge.log(d + "  " + str + "  " + paramAnonymousMethodHookParam);
                    XposedBridge.log("调用增加数据方法==>微信");
                    Intent localIntent = new Intent();
                    localIntent.putExtra("money", d);
                    localIntent.putExtra("mark", str);
                    localIntent.putExtra("type", "wechat");
                    localIntent.putExtra("payurl", pay);
                    Log.i("tag", "wPay: "+pay);
                    localIntent.setAction(Constans.QRCODE_RESULT);
                    context.sendBroadcast(localIntent);
                }

                protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                        throws Throwable {
                }
            });
            try {
                XposedHelpers.findAndHookMethod("com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI", classLoader, "initView", new Object[]{new XC_MethodHook() {
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                        Log.i("tag","w2");
                        XposedBridge.log("Hook微信开始......");
                        XposedBridge.log("Hook微信开始......");
                        if (PayHelperUtils.getVerName(context).equals("6.6.7")) {
                            Intent localObject1 = ((Activity) paramAnonymousMethodHookParam.thisObject).getIntent();
                            String mark = ((Intent) localObject1).getStringExtra("mark");
                            String money = ((Intent) localObject1).getStringExtra("money");
                            Object localObject2 = XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "hXD").get(paramAnonymousMethodHookParam.thisObject);
                            XposedHelpers.callMethod(XposedHelpers.findField(XposedHelpers.findClass("com.tencent.mm.wallet_core.ui.formview.WalletFormView", classLoader), "uZy").get(localObject2), "setText", new Object[]{localObject1});
                            XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI", classLoader), "a", new Object[]{paramAnonymousMethodHookParam.thisObject, mark});
                            XposedHelpers.callStaticMethod(XposedHelpers.findClass("com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI", classLoader), "c", new Object[]{paramAnonymousMethodHookParam.thisObject});
                            ((Button) XposedHelpers.callMethod(paramAnonymousMethodHookParam.thisObject, "findViewById", new Object[]{Integer.valueOf(2131756838)})).performClick();
                        }
                        while (!PayHelperUtils.getVerName(context).equals("6.6.6")) {
                            return;
                        }
                        Object localObject1 = ((Activity) paramAnonymousMethodHookParam.thisObject).getIntent();
                        String str = ((Intent) localObject1).getStringExtra("mark");
                        localObject1 = ((Intent) localObject1).getStringExtra("money");
                        Object localObject2 = XposedHelpers.findField(paramAnonymousMethodHookParam.thisObject.getClass(), "loz").get(paramAnonymousMethodHookParam.thisObject);
                        XposedHelpers.callMethod(XposedHelpers.findField(XposedHelpers.findClass("com.tencent.mm.wallet_core.ui.formview.WalletFormView", classLoader), "Aef").get(localObject2), "setText", new Object[]{localObject1});
                        localObject1 = XposedHelpers.findClass("com.tencent.mm.plugin.collect.ui.CollectCreateQRCodeUI", classLoader);
                        XposedHelpers.callStaticMethod((Class) localObject1, "a", new Object[]{paramAnonymousMethodHookParam.thisObject, str});
                        XposedHelpers.callStaticMethod((Class) localObject1, "c", new Object[]{paramAnonymousMethodHookParam.thisObject});
                        ((Button) XposedHelpers.callMethod(paramAnonymousMethodHookParam.thisObject, "findViewById", new Object[]{Integer.valueOf(2131756780)})).performClick();
                    }

                    protected void beforeHookedMethod(XC_MethodHook.MethodHookParam paramAnonymousMethodHookParam)
                            throws Throwable {
                    }
                }});
                return;
            } catch (Exception paramClassLoader) {
            }
        } catch (Exception localException) {
        }

//        final ClassLoader classLoader = loadPackageParam.classLoader;
//        Object[] arrayOfObject2 = new Object[1];
//        arrayOfObject2[0] = new XC_MethodHook() {
//            protected void afterHookedMethod(XC_MethodHook.MethodHookParam methodHookParam) {
//                Activity ctx = (Activity) methodHookParam.thisObject;
//                try {
//
//                    Intent localIntent2 = (ctx).getIntent();
//                    String str3 = localIntent2.getStringExtra("mark");
//                    String str4 = localIntent2.getStringExtra("money");
//
//                    Object localObject3 = XposedHelpers.findField(ctx.getClass(), "hXD").get(methodHookParam.thisObject);
//                    XposedHelpers.callMethod(XposedHelpers.findField(XposedHelpers.findClass(Constans.WECHAT_WALLET, classLoader), "uZy").get(localObject3), "setText", str4);
//                    Class localClass2 = XposedHelpers.findClass(Constans.WECHAT_QRCODE, classLoader);
//                    Object[] arrayOfObject4 = new Object[2];
//                    arrayOfObject4[0] = ctx;
//                    arrayOfObject4[1] = str3;
//                    XposedHelpers.callStaticMethod(localClass2, "a", arrayOfObject4);
//                    Object[] arrayOfObject5 = new Object[1];
//                    arrayOfObject5[0] = ctx;
//                    XposedHelpers.callStaticMethod(localClass2, "c", arrayOfObject5);
//                    Object localObject4 = ctx;
//                    Object[] arrayOfObject6 = new Object[1];
//                    arrayOfObject6[0] = Integer.valueOf(2131756838);
//                    ((Button) XposedHelpers.callMethod(localObject4, "findViewById", arrayOfObject6)).performClick();
//
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//
//            }
//
//        };
//        XposedHelpers.findAndHookMethod(Constans.WECHAT_QRCODE, loadPackageParam.classLoader, "initView", arrayOfObject2);
//
//
//        XposedBridge.hookAllMethods(XposedHelpers.findClass(Constans.WECHAT_QRCODE, classLoader), "d", new XC_MethodHook() {
//            @Override
//            protected void afterHookedMethod(MethodHookParam methodHookParam) throws Throwable {
//                super.afterHookedMethod(methodHookParam);
//                Activity ctx = (Activity) methodHookParam.thisObject;
//                Object[] args = methodHookParam.args;
//
//
//                Object o = args[3];
//                String mark =  String.valueOf( XposedHelpers.findField(o.getClass(), "desc").get(o));
//                String pay_url = String.valueOf(XposedHelpers.findField(o.getClass(), "hUK").get(o));
//                String money = String.valueOf(XposedHelpers.findField(o.getClass(), "hUL").get(o));
//
//                String bJg =  String.valueOf( XposedHelpers.findField(o.getClass(), "bJg").get(o));
//                XposedBridge.log("starting" + bJg);
//                Intent localIntent2 = new Intent();
//                localIntent2.putExtra(Constans.MONEY, money);
//                localIntent2.putExtra(Constans.MARK, mark);
//                localIntent2.putExtra(Constans.TYPE, Constans.WECHAT);
//                localIntent2.putExtra(Constans.PAY_URL, pay_url);
//                localIntent2.setAction(Constans.QRCODE_RESULT);
//                ctx.sendBroadcast(localIntent2);
//
//            }
//        });


    }
}
