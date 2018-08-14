package cn.sanlicun.pay.web.servlet;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.sanlicun.pay.App;
import cn.sanlicun.pay.param.PushParam;
import cn.sanlicun.pay.sqlite.DBManager;
import cn.sanlicun.pay.util.PayHelperUtils;
import cn.sanlicun.pay.util.PreUtils;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/8/9
 * 描述 ：
 * ============================================
 */

public class PayServlet extends BaseServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setStatus(HttpServletResponse.SC_OK);
        Log.i("tag","PayServlet");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=utf-8");
        PushParam pushParam = new PushParam();
        String money = req.getParameter("money");
        String mark = req.getParameter("mark");
        String type = req.getParameter("type");
        Log.i("tag","PayServlet:money"+money+"mark:"+mark+"type:"+type);

        PrintWriter out = null;
        try {
            out = resp.getWriter();
            if (isEmpty(money, mark, type)) {
                Log.i("tag","PayServlet1");
                pushParam.setMessage("参数为空");
                out.write(JSON.toJSONString(pushParam));
                return;
            }
            double dMoney = Double.parseDouble(money);
            if (type.equals("alipay")) {
                Log.i("tag","PayServlet2");
                if (dMoney > 50000) {
                    Log.i("tag","PayServlet3");
                    pushParam.setMessage("单笔现金转账不能超过50000");
                    out.write(JSON.toJSONString(pushParam));
                    return;
                }
                Log.i("tag","PayServlet4");
                PayHelperUtils.getPay(money, mark, type, App.getCtx());
                Log.i("tag","PayServlet5");
                Thread.sleep(2000);
                Log.i("tag","PayServlet6");
                DBManager dbManager = new DBManager(App.getCtx());
                List<PushParam> pushParams = dbManager.queryPay(mark, money);
                if (pushParams.size() == 0) {
                    Log.i("tag","PayServlet7");
                    pushParam.setMessage("请求超时");
                    out.write(JSON.toJSONString(pushParam));
                    return;
                } else {
                    Log.i("tag","PayServlet8");
                    PushParam respPP = pushParams.get(0);
                    String account= PreUtils.getString(PreUtils.PRE_ACCOUNT,"");
                    if(account!=null&&!account.equals("")){
                        respPP.setAccount(account);
                    }
                    out.write(JSON.toJSONString(respPP));
                    return;
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                out.close();
            }

        }
    }

    private boolean isEmpty(String... params) {
        for (String s : params) {
            if (TextUtils.isEmpty(s)) {
                return true;
            }
        }
        return false;
    }
}
