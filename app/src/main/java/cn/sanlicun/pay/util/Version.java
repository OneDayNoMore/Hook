package cn.sanlicun.pay.util;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Created by 小饭 on 2018/7/6.
 */

public class Version {
    /**
     *
     * @param paramContext
     * @return
     */
    public static String getVersion(Context paramContext)
    {
        try
        {
            String str = paramContext.getPackageManager().getPackageInfo(paramContext.getPackageName(), 0).versionName;
            return str;
        }
        catch (PackageManager.NameNotFoundException localNameNotFoundException)
        {

        }
        return "";
    }

}
