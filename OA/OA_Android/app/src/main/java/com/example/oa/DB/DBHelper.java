package com.example.oa.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.oa.bean.Account;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "your_database.db";
    private static final int DATABASE_VERSION = 1;

    // 构造方法
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 创建数据库
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建表的SQL语句
        String createTableQuery = "CREATE TABLE IF NOT EXISTS users (id TEXT PRIMARY KEY, username TEXT, password TEXT)";
        db.execSQL(createTableQuery);
    }

    // 更新数据库
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果需要更新表结构，可以在此处添加相应的逻辑
        // 注意：更新数据库时需要保留原有的数据，可以通过备份数据、创建新表、导入数据等方式处理
    }

    // 插入用户数据
// 插入或更新用户数据
    public long insertOrUpdateUser(String username, String password,String id) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", password);

        // 查询是否存在相同的用户名
        Cursor cursor = db.query("users", null, "username = ?", new String[]{username}, null, null, null);
        long result;
        if (cursor.moveToFirst()) {
            // 存在相同的用户名，执行更新操作
            Log.d("pwdpwd","111111");
            result = db.update("users", values, "username = ?", new String[]{username});

        } else {
            // 不存在相同的用户名，执行插入操作
            values.put("username", username);
            values.put("id",id);
            result = db.insert("users", null, values);
        }
        cursor.close();
//        db.close();
        return result;
    }

    @SuppressLint("Range")
    public String getPasswordByUsername(String username) {
        SQLiteDatabase db = getReadableDatabase();
        String password = null;

        Cursor cursor = db.query("users", new String[]{"password"}, "username = ?", new String[]{username}, null, null, null);
        if (cursor.moveToFirst()) {
            password = cursor.getString(cursor.getColumnIndex("password"));
        }
        cursor.close();
//        db.close();

        return password;
    }
    public void deleteTable(String tableName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(db); // 重新创建表
        db.close();
    }

    public List<Account> getAllUserData() {
        List<Account> userDataList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query("users", null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex("id"));
            @SuppressLint("Range") String username = cursor.getString(cursor.getColumnIndex("username"));
            @SuppressLint("Range") String password = cursor.getString(cursor.getColumnIndex("password"));
            Log.d("dbid",id);
            Log.d("dbid",username);
            Account userData = new Account(id, username, password);
            userDataList.add(userData);
        }
        cursor.close();
        db.close();
        return userDataList;
    }

}
