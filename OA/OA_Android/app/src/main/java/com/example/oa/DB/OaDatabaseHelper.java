package com.example.oa.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.oa.bean.Oa;

import java.util.ArrayList;
import java.util.List;

public class OaDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "oa_database.db";
    private static final int DATABASE_VERSION = 1;

    // Lesson table
    // Add other user columns here

    // Approval table
    private static final String TABLE_APPROVAL = "oa";
    private static final String COLUMN_APPROVAL_ID = "id";
    private static final String COLUMN_APPROVAL_LESSON_ID = "lesson_id";
    private static final String COLUMN_APPROVAL_TEACHER = "teacher";
    private static final String COLUMN_APPROVAL_USER_ID = "user_id";
    private static final String COLUMN_APPROVAL_STATUS = "status";
    // Add other approval columns here

    public OaDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String createApprovalTableQuery = "CREATE TABLE " + TABLE_APPROVAL + "(" +
                COLUMN_APPROVAL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_APPROVAL_LESSON_ID + " TEXT, " +
                COLUMN_APPROVAL_USER_ID + " TEXT, " +
                COLUMN_APPROVAL_STATUS + " TEXT, " +
                COLUMN_APPROVAL_TEACHER + " TEXT " +")"
                ;
        db.execSQL(createApprovalTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Handle database upgrade if needed
    }
    // Insert a new approval record
    public long insertApproval(String lessonId, String userId, String status, String teacher) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APPROVAL_LESSON_ID, lessonId);
        values.put(COLUMN_APPROVAL_USER_ID, userId);
        values.put(COLUMN_APPROVAL_STATUS, status);
        values.put(COLUMN_APPROVAL_TEACHER, teacher);
        long approvalId = db.insert(TABLE_APPROVAL, null, values);
        db.close();
        return approvalId;
    }

    // Update an existing approval record
    public int updateApproval(long approvalId, String lessonId, String userId, String status, String teacher) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_APPROVAL_LESSON_ID, lessonId);
        values.put(COLUMN_APPROVAL_USER_ID, userId);
        values.put(COLUMN_APPROVAL_STATUS, status);
        values.put(COLUMN_APPROVAL_TEACHER, teacher);
        int rowsAffected = db.update(TABLE_APPROVAL, values, COLUMN_APPROVAL_ID + " = ?", new String[]{String.valueOf(approvalId)});
        db.close();
        return rowsAffected;
    }

    // Delete an approval record
    public int deleteApproval(long approvalId) {
        SQLiteDatabase db = getWritableDatabase();
        int rowsAffected = db.delete(TABLE_APPROVAL, COLUMN_APPROVAL_ID + " = ?", new String[]{String.valueOf(approvalId)});
        db.close();
        return rowsAffected;
    }

    // Query all approval records
    @SuppressLint("Range")
    public List<Oa> getAllApprovals() {
        List<Oa> approvals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPROVAL, null);
        if (cursor.moveToFirst()) {
            do {
                Oa oa = new Oa();
                oa.setId(String.valueOf(cursor.getLong(cursor.getColumnIndex(COLUMN_APPROVAL_ID))));
                oa.setLessonId(cursor.getString(cursor.getColumnIndex(COLUMN_APPROVAL_LESSON_ID)));
                oa.setUserid(cursor.getString(cursor.getColumnIndex(COLUMN_APPROVAL_USER_ID)));
                oa.setStatus(cursor.getString(cursor.getColumnIndex(COLUMN_APPROVAL_STATUS)));
                oa.setTeacher(cursor.getString(cursor.getColumnIndex(COLUMN_APPROVAL_TEACHER)));

                // Add other attributes if needed

                approvals.add(oa);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return approvals;
    }

}
