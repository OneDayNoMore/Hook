package cn.sanlicun.pay;

import android.app.Application;
import android.content.Context;


/**
 * Created by 小饭 on 2018/7/6.
 */

public class App extends Application {
    public static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        ctx=getApplicationContext();
    }

    public static Context getCtx() {
        return ctx;
    }

}
