package cn.sanlicun.pay.hook;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import cn.sanlicun.pay.Constans;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static cn.sanlicun.pay.Constans.ACTION_LAUNCH_ALIPAY_WALLET;

/**
 * Created by 小饭 on 2018/7/6.
 */

public class AliPlugin implements IPlugin {
    private int isAlipay = 0;

    @Override
    public void load(final XC_LoadPackage.LoadPackageParam loadPackageParam) {
        ClassLoader clazzLoader = loadPackageParam.classLoader;

        if (loadPackageParam.packageName.contains(Constans.ALIPAYNAME)) {


            XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    Context ctx = (Context) param.thisObject;
                    if (Constans.ALIPAYNAME.equals(getProcessName((Context) param.thisObject)) && (isAlipay < 1)) {
                        isAlipay++;
                        registerLaunchReceiver((Context) param.thisObject, loadPackageParam.classLoader);
                        Log.i("tag","========init=========");
//                        new AliBillPlugin().load(loadPackageParam, ctx);
                        new AliPayeePlugin().load(loadPackageParam, ctx);

                    }


                }
            });






            XposedHelpers.findAndHookMethod(Activity.class, "onDestroy", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final Activity activity = (Activity) param.thisObject;
                    String name = activity.getClass().getName();

                }
            });




        }
    }

    private void registerLaunchReceiver(Context context, final ClassLoader classLoader) {

        IntentFilter intentFilter = new IntentFilter(ACTION_LAUNCH_ALIPAY_WALLET);


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                XposedBridge.log("receive action: 接受到广播");
                XposedBridge.log("receive action: " + intent.getAction());
                final Class<?> clazz = XposedHelpers.findClass(Constans.SETMONEYACTIVITY, classLoader);

                String money = intent.getStringExtra(Constans.MONEY);
                String mark = intent.getStringExtra(Constans.MARK);
                launchWallet(context, clazz, money, mark);
            }
        };

        context.registerReceiver(receiver, intentFilter);


    }

    private void launchWallet(Context context, Class clazz, String money, String mark) {

        Intent intent = new Intent(context, clazz);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(Constans.MARK, mark);
        intent.putExtra(Constans.MONEY, money);
        context.startActivity(intent);
    }

    public String getProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
            if (processInfo.pid == pid) {
                return processInfo.processName;
            }
        }
        return null;
    }


}
