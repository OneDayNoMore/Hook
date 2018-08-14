package cn.sanlicun.pay.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import cn.sanlicun.pay.App;

/**
 * Created by Administrator on 2017/6/21.
 */
public class PreUtils {
    public static Context ctx;


    public static final String PRE_SPACE_BEAN = "bean";//对象存储空间
    public static final String PRE_ASYN = "asyn";//架构
    public static final String PRE_SYNC="sync";
    public static final String PRE_SIGN="sign";
    public static final String PRE_ACCOUNT="account";

    private static SharedPreferences sp;// 单例
    private static PreUtils instance;// 单例
    public PreUtils() {
        ctx = App.getCtx();
        sp = ctx.getSharedPreferences(PRE_SPACE_BEAN,
                Context.MODE_PRIVATE);
    }

    public static synchronized void init() {
        if (instance == null) {
            instance = new PreUtils();
        }
    }

    public static PreUtils getInstance() {
        if (instance == null) {
            throw new RuntimeException("class should init!");
        }
        return instance;
    }

    /*
    * 存取对象
    * */

    public static void SaveBean(String key, Object obj) {
        if (obj instanceof Serializable) {// obj必须实现Serializable接口，否则会出问题
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                String string64 = new String(Base64.encode(baos.toByteArray(),
                        0));
                SharedPreferences.Editor editor = sp.edit();
                editor.putString(key, string64).commit();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            throw new IllegalArgumentException(
                    "the obj must implement Serializble");
        }

    }


    public static Object getBean(String key) {
        Object obj = null;
        try {
            String base64 = sp.getString(key, "");
            if (base64.equals("")) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(base64.getBytes(), 1);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            obj = ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }


    public static void setInt(String key, int value) {
        sp.edit().putInt(key, value).commit();
    }

    public static int getInt(String key, int defValue) {
        return sp.getInt(key, defValue);
    }


    public static void setString(String key, String value) {
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        return sp.getString(key, defValue);
    }

    public static void setBoolean(String key, Boolean bool) {
        sp.edit().putBoolean(key, bool).commit();
    }


    public static Boolean getBoolean(String key, Boolean defbool) {
        return sp.getBoolean(key, defbool);
    }


    public static Map<Integer, String> getMap(String key) {
        Map<Integer, String> mage2nameMap = new HashMap<>();
        String age2name = sp.getString(key, null);
        if (age2name.length() > 0) {
            JSONTokener jsonTokener = new JSONTokener(age2name);
            try {
                JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    mage2nameMap.put(jsonObject.getInt("age"), jsonObject.getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mage2nameMap;
    }

    public static void putMap(String key, Map<Integer, String> age2nameMap) {
        if (age2nameMap != null) {
            JSONStringer jsonStringer = new JSONStringer();
            try {
                jsonStringer.array();
                for (Integer integer : age2nameMap.keySet()) {
                    jsonStringer.object();
                    jsonStringer.key("age");
                    jsonStringer.value(integer);
                    jsonStringer.key("name");
                    jsonStringer.value(age2nameMap.get(integer));
                    jsonStringer.endObject();
                }
                jsonStringer.endArray();
            } catch (JSONException e) {
                e.printStackTrace();
            }
           sp.edit().putString(key, jsonStringer.toString()).commit();
        }
    }




}
