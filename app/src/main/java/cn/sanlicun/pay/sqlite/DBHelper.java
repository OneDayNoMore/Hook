package cn.sanlicun.pay.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * ===========================================
 * 作者 ：曾立强 3042938728@qq.com
 * 时间 ：2018/2/6
 * 描述 ：
 * ============================================
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "sign.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }





    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS pay" +
                "(_id INTEGER PRIMARY KEY AUTOINCREMENT,pay_url VARCHAR,mark VARCHAR,money VARCHAR,type VARCHAR,billNo VARCHAR,time VARCHAR,state INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
