package cn.sanlicun.pay.net;

import android.content.Context;
import android.util.Log;

/**
 * Created by 小饭 on 2018/7/4.
 */

public class Api extends BaseApi { 

    /**
     * 上传url
     * @param context
     * @param param
     * @param clazz
     * @param TAG
     */
    public static void PUSH_URL(Context context, Object param, final Class clazz, final int TAG) {
//        send(context, "/pay/api/5b29d566d2414", param, clazz, TAG);
    }



    /**
     * 上传支付结果
     * @param context
     * @param param
     * @param clazz
     * @param TAG
     */
    public static void PUSH_ORDER_RESULT(Context context, Object param, final Class clazz, final int TAG) {
        Log.i("tag","send_result");
        send(context, "/pay/notify/Index/notify", param, clazz, TAG);


    }
}
