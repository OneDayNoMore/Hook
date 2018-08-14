package cn.sanlicun.pay.hook;

import android.content.Context;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by 小饭 on 2018/7/11.
 */

public interface ISubPlugin {
      void load(XC_LoadPackage.LoadPackageParam loadPackageParam, Context ctx) ;
}
