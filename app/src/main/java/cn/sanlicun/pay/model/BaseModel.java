package cn.sanlicun.pay.model;

import android.util.Base64;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by 小饭 on 2018/7/6.
 */

public class BaseModel implements Serializable{
    private int status = 0;
    private String msg = "";
    private String data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData(Class clazz) {

        try {
            String encode =  URLDecoder.decode(data, "utf-8");


            String decode = new String(Base64.decode(encode, Base64.DEFAULT), "utf-8");
            if (decode.startsWith("[")) {
                return JSONObject.parseArray(decode.substring(0,decode.lastIndexOf("]")+1), clazz);
            } else {
                return JSONObject.parseObject(decode.substring(0,decode.lastIndexOf("}")+1), clazz);
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
