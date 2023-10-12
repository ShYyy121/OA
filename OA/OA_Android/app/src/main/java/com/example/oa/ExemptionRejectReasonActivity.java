package com.example.oa;

import android.view.View;


import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.Exemption;

import com.example.oa.databinding.ActivityExemptionRejectReasonBinding;

import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

public class ExemptionRejectReasonActivity extends BaseBindingActivity<ActivityExemptionRejectReasonBinding> {

    String rejectreason=null;

    private Exemption exemption;

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
               exemption.rejectreason=rejectreason;
               exemption.status="审核拒绝";
               doUpdate(exemption);
               finish();
            }
        });
    }

    private void doUpdate(Exemption exemption) {
        showLoading();
        if (App.isManager()){
            HttpUtil.post(Api.EXEMPTION_UPDATE, HttpUtil.toJSON(exemption), new HttpUtil.OnNetStringListener() {
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
            HttpUtil.post(Api.EXEMPTION_TeacherUPDATE, HttpUtil.toJSON(exemption), new HttpUtil.OnNetStringListener() {
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
        exemption = (Exemption) getIntent().getSerializableExtra("exemption");
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