package cn.sanlicun.pay.hook;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 小饭 on 2018/7/6.
 */

public interface IPlugin {
    void  load(final XC_LoadPackage.LoadPackageParam param)  ;
}
