package cn.sanlicun.pay.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.sanlicun.pay.param.PushParam;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/2/6
 * 描述 ：
 * ============================================
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {
        helper = new DBHelper(context);
        db = helper.getWritableDatabase();
    }

//    "(_id INTEGER PRIMARY KEY AUTOINCREMENT,pay_url VARCHAR,mark VARCHAR,money VARCHAR,type VARCHAR,billNo VARCHAR,time VARCHAR)");

    public void addPay(PushParam bean) {
        db.beginTransaction();  //开始事务
        try {

            db.execSQL("INSERT INTO pay VALUES(null,?,?,?,?,?,?,?)", new Object[]{bean.getPay_url(), bean.getMark(), bean.getMoney(), bean.getType(), bean.getBillNo(), bean.getTime(),bean.getState()});
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }


    public void updatePay(PushParam bean) {
        ContentValues cv = new ContentValues();
        cv.put("pay_url", bean.getPay_url());
        cv.put("mark", bean.getMark());
        cv.put("type", bean.getType());
        cv.put("billNo", bean.getBillNo());
        cv.put("time", bean.getTime());
        cv.put("state",bean.getState());
        db.update("pay", cv, "pay_url = ?", new String[]{String.valueOf(bean.getPay_url())});
    }

    public void deletePay(PushParam bean) {
        db.delete("pay", "_id = ?", new String[]{String.valueOf(bean.getId())});
    }

    /**
     * "(_id INTEGER PRIMARY KEY AUTOINCREMENT,pay_url VARCHAR,
     * mark VARCHAR,
     * money VARCHAR,
     * type VARCHAR,
     * billNo VARCHAR,
     * time VARCHAR)");
     */

    public List<PushParam> queryPay(String mark,String money){
        List<PushParam> pushBeens = new ArrayList<>();
        Cursor c = db.rawQuery("SELECT * FROM pay where mark= ? and money= ? ", new String[]{mark, money});
        while (c.moveToNext()) {
            PushParam bean = new PushParam();
            bean.setId(c.getInt(c.getColumnIndex("_id")));
            bean.setPay_url(c.getString(c.getColumnIndex("pay_url")));
            bean.setMark(c.getString(c.getColumnIndex("mark")));
            bean.setMoney(c.getString(c.getColumnIndex("money")));
            bean.setType(c.getString(c.getColumnIndex("type")));
            bean.setTime(c.getString(c.getColumnIndex("time")));
            bean.setBillNo(c.getString(c.getColumnIndex("billNo")));
            bean.setState(c.getInt(c.getColumnIndex("state")));
            pushBeens.add(bean);
        }
        c.close();
        return pushBeens;

    }


    public void closeDB() {
        db.close();
    }


}
