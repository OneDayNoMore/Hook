package cn.sanlicun.pay;

import cn.sanlicun.pay.hook.AliPlugin;
import cn.sanlicun.pay.hook.WechatPlugin;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


/**
 * Created by qzj_ on 2016/5/9.
 */
public class PluginMain implements IXposedHookLoadPackage {

    public PluginMain() {
        XposedBridge.log("Now Loading HOHOalipay plugin...");
    }


    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        new AliPlugin().load(lpparam);
        new WechatPlugin().load(lpparam);
    }


}
