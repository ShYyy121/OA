package com.example.oa;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.Oa;
import com.example.oa.databinding.ActivityRejectReasonBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.Locale;

public class RejectReasonActivity extends BaseBindingActivity<ActivityRejectReasonBinding> {

    String rejectreason=null;
    private Oa oa;

    @Override
    protected void initListener() {
        viewBinder.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        viewBinder.tvPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectreason=viewBinder.tvReason.getText().toString();
//                    setResultAndFinish();
               oa.rejectreason=rejectreason;
               oa.status="审核拒绝";
               doUpdate(oa);
               finish();
            }
        });
    }

    private void doUpdate(Oa oa) {
        showLoading();
        if (App.isManager()){
            HttpUtil.post(Api.OA_UPDATE, HttpUtil.toJSON(oa), new HttpUtil.OnNetStringListener() {
                @Override
                public void success(String data, String message) {
                    display$Toast(message);
                }

                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });
        } else if (App.isTeacher()) {
            HttpUtil.post(Api.OA_TeacherUPDATE, HttpUtil.toJSON(oa), new HttpUtil.OnNetStringListener() {
                @Override
                public void success(String data, String message) {
                    display$Toast(message);
                }

                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });

        }

    }
    @Override
    protected void initData() {
        oa = (Oa) getIntent().getSerializableExtra("oa");
    }

//    private void setResultAndFinish()  {
//        // 创建一个新的 Intent 对象，将参数放入其中
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("rejectreason", rejectreason);
//        // 将 Intent 设置为结果
//        setResult(Activity.RESULT_OK, resultIntent);
//        finish();
//    }
}