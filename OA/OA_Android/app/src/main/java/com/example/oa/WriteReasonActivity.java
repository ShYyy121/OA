package com.example.oa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.Exemption;
import com.example.oa.bean.Lesson;
import com.example.oa.databinding.ActivityWriteReasonBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class WriteReasonActivity extends BaseBindingActivity<ActivityWriteReasonBinding> {

    String currentDateTime =null;
    private Lesson lesson;

    @Override
    protected void initListener() {
        viewBinder.tvMianxiushenqing.setOnClickListener(v -> Submit());
        viewBinder.ivProof.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 100));
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
    private void Submit() {
        getDate();
        String reason=viewBinder.etReason.getText().toString().trim();
        HashMap<String,Object> params=new HashMap<>();
        params.put("userid",App.user.id);
        params.put("reason",reason);
        params.put("lessonId",lesson.id);
        params.put("teacher",lesson.teacher);
        params.put("pic",pic);
        params.put("time",currentDateTime);
        HttpUtil.postModel(Api.EXEMPTION_ADD, params, Exemption.class, new HttpUtil.OnNetModelListener<Exemption>() {
            @Override
            public void success(Exemption data, String message) {
                dismissLoading();
                toast(message);
                finish();
            }

            @Override
            public void error(String message) {
                dismissLoading();
                toast(message);
            }
        });
    }

    @Override
    protected void initData() {
        lesson = (Lesson) getIntent().getSerializableExtra("lesson");
    }

    private String pic;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String imagePath = getImagePath(this, data.getData());
            Glide.with(this).load(imagePath).into(viewBinder.ivProof);
            showLoading();
            HttpUtil.upload("common/upload", new File(imagePath), new HttpUtil.OnNetStringListener() {
                @Override
                public void success(String data, String message) {
                    dismissLoading();
                    pic = data;
                    toast(message);
                }

                @Override
                public void error(String error) {
                    dismissLoading();
                    toast(error);
                }
            });
        }
    }

    public static String getImagePath(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            File file = new File(context.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            inputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}