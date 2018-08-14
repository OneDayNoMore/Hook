package cn.sanlicun.pay.util;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/8/7
 * 描述 ：
 * ============================================
 */

public class StringUtils {

    public static String getTextCenter(String paramString1, String paramString2, String paramString3)
    {
        try
        {
            int i = paramString1.indexOf(paramString2) + paramString2.length();
            paramString1 = paramString1.substring(i, paramString1.indexOf(paramString3, i));
            return paramString1;
        }
        catch (Exception p)
        {
            p.printStackTrace();
        }
        return "error";
    }

}
