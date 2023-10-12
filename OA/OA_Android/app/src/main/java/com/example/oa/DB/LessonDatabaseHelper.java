package com.example.oa.DB;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.oa.bean.Lesson;

import java.util.ArrayList;
import java.util.List;

public class LessonDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "lesson_database";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "lessons";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_TEACHER = "teacher";
    private static final String COLUMN_START_TIME = "start_time";
    private static final String COLUMN_DESCRIPTION = "description";

    public LessonDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " TEXT PRIMARY KEY, " +
                COLUMN_NAME + " TEXT, " +
                COLUMN_TEACHER + " TEXT, " +
                COLUMN_START_TIME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT)";
        db.execSQL(createTableQuery);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addLesson(Lesson lesson) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, lesson.getName());
        values.put(COLUMN_ID, lesson.id);
        values.put(COLUMN_TEACHER, lesson.getTeacher());
        values.put(COLUMN_START_TIME, lesson.getStarTime());
        values.put(COLUMN_DESCRIPTION, lesson.getDes());
        return db.insert(TABLE_NAME, null, values);
    }

    public void updateLesson(Lesson lesson) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, lesson.getName());
        values.put(COLUMN_ID, lesson.getId());
        values.put(COLUMN_TEACHER, lesson.getTeacher());
        values.put(COLUMN_START_TIME, lesson.getStarTime());
        values.put(COLUMN_DESCRIPTION, lesson.getDes());
        String selection = COLUMN_NAME + " = ? AND " +
                COLUMN_TEACHER + " = ? AND " +
                COLUMN_DESCRIPTION + " = ? AND " +
                COLUMN_START_TIME + " = ?";
        String[] selectionArgs = new String[]{
                lesson.getName(),
                lesson.getTeacher(),
                lesson.getDes(),
                lesson.getStarTime()
        };

        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public void deleteLesson(long lessonId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME, COLUMN_ID + " = ?", new String[]{String.valueOf(lessonId)});
    }

    @SuppressLint("Range")
    public List<Lesson> getAllLessons() {
        List<Lesson> lessons = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        if (cursor.moveToFirst()) {
            do {
                Lesson lesson = new Lesson();
                lesson.setId(String.valueOf(cursor.getLong(cursor.getColumnIndex(COLUMN_ID))));
                lesson.setName(cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                lesson.setTeacher(cursor.getString(cursor.getColumnIndex(COLUMN_TEACHER)));
                lesson.setStarTime(cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME)));
                lesson.setDes(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));
                lessons.add(lesson);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lessons;
    }
}
