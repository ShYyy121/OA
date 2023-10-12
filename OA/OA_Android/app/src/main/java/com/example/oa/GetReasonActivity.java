package com.example.oa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.oa.DB.OaDatabaseHelper;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.Lesson;
import com.example.oa.databinding.ActivityGetReasonBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.Calendar;
import java.util.HashMap;

public class GetReasonActivity extends BaseBindingActivity<ActivityGetReasonBinding> {
    Lesson lesson=null;
    String currentDateTime =null;
    private OaDatabaseHelper databaseHelper = new OaDatabaseHelper(this);
    private SQLiteDatabase db;

    @Override
    protected void initListener() {
        viewBinder.tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                HashMap<String, Object> params = new HashMap<>();
                params.put("lessonId", lesson.id);
                params.put("userid", App.user.id);
                params.put("teacher",lesson.teacher);
                getDate();
                params.put("time",currentDateTime);
                String reason=viewBinder.tvReason.getText().toString();
                params.put("reason",reason);
                HttpUtil.post(Api.OA_ADD, params, new HttpUtil.OnNetStringListener() {
                    @Override
                    public void success(String data, String message) {
                        display$Toast(message);

                        ContentValues approvalValues = new ContentValues();
                        approvalValues.put("lesson_id", lesson.id);
                        approvalValues.put("teacher", lesson.teacher);
                        approvalValues.put("user_id", App.user.id);  // Set other approval values
                        databaseHelper.insertApproval(lesson.id,App.user.id,"审核中", lesson.teacher);
                        Intent resultIntent = new Intent();
                        setResult(RESULT_OK, resultIntent);

                        finish();
                    }

                    @Override
                    public void error(String message) {
                        display$Toast(message);
                    }
                });
            }
        });
        viewBinder.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {

        lesson= (Lesson) getIntent().getSerializableExtra("lesson");
        db = databaseHelper.getWritableDatabase();
    }
    public  void getDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // 注意，月份是从0开始计数的，所以需要加1
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

// 将获取到的日期和时间格式化为字符串
         currentDateTime = String.format("%04d-%02d-%02d %02d:%02d:%02d", year, month, day, hour, minute, second);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
        db.close();
    }
}