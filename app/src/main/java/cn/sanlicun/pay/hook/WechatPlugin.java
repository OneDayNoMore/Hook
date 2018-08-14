package cn.sanlicun.pay.hook;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import cn.sanlicun.pay.Constans;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static cn.sanlicun.pay.Constans.ACTION_LAUNCH_WECHAT_WALLET;

/**
 * Created by 小饭 on 2018/7/6.
 */

public class WechatPlugin implements IPlugin {
    public int wechat = 0;

    @Override
    public void load(final XC_LoadPackage.LoadPackageParam loadPackageParam) {

        final ClassLoader classLoader = loadPackageParam.classLoader;

        if (loadPackageParam.packageName.contains(Constans.WECHAT_PACKAGE)) {

            XposedHelpers.findAndHookMethod(Application.class, "onCreate", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {

                    Context ctx = (Context) param.thisObject;

                    Toast.makeText(ctx, "receive action:1 " + getProcessName((Context) param.thisObject) + ":" + wechat, Toast.LENGTH_LONG).show();


                    if (Constans.WECHAT_PACKAGE.equals(getProcessName((Context) param.thisObject)) && (wechat < 1)) {

                        wechat++;
                        registerLaunchReceiver((Context) param.thisObject, loadPackageParam.classLoader);
                        new WechatPayeePlugin().load(loadPackageParam,ctx);
                         new WechatBillPlugin().load(loadPackageParam,ctx);
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


    private void registerLaunchReceiver(Context context, final ClassLoader classLoader) {


        IntentFilter intentFilter = new IntentFilter(ACTION_LAUNCH_WECHAT_WALLET);


        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                final Class<?> walletActivityClass = XposedHelpers.findClass(Constans.WECHAT_QRCODE, classLoader);
                launchWallet(context, walletActivityClass, intent.getStringExtra(Constans.MARK), intent.getStringExtra(Constans.MONEY));
            }
        };

        context.registerReceiver(receiver, intentFilter);


    }

    private void launchWallet(Context context, Class<?> walletActivityClass, String mark, String money) {


        Intent localIntent = new Intent(context, walletActivityClass);
        localIntent.putExtra(Constans.MARK, mark);
        localIntent.putExtra(Constans.MONEY, money);
        localIntent.addFlags(268435456);
        context.startActivity(localIntent);
    }
}
