package com.example.oa;

import android.annotation.SuppressLint;
import android.view.View;

import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.Exemption;
import com.example.oa.bean.Oa;
import com.example.oa.databinding.ActivityExemptionDetailBinding;
import com.example.oa.databinding.ActivityOadetailBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExemptionDetailActivity extends BaseBindingActivity<ActivityExemptionDetailBinding> {
    List<Exemption> oas=null;
    private String oaid;


    @Override
    protected void initListener() {
        viewBinder.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void initData() {
        oaid = (String) getIntent().getSerializableExtra("eid");
        HashMap<String, Object> params = new HashMap<>();
        if (App.isTeacher()){
            params.put("userid", App.user.username);
            HttpUtil.getList(Api.EXEMPTION_TeacherGET, params, Exemption.class, new HttpUtil.OnNetListListener<Exemption>() {
                @Override
                public void success(List<Exemption> data, String message) {
                    display$Toast(message);
                    oas=data;
                    init();
                }
                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });
        } else if (App.isManager()){
            params.put("userid", App.user.id);
            HttpUtil.getList(Api.EXEMPTION_GET, params, Exemption.class, new HttpUtil.OnNetListListener<Exemption>() {
                @Override
                public void success(List<Exemption> data, String message) {
                    display$Toast(message);
                    oas=data;
                    init();
                }
                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });
        }
        else{
            params.put("userid", App.user.id);
            HttpUtil.getList(Api.EXEMPTION_StuGET, params, Exemption.class, new HttpUtil.OnNetListListener<Exemption>() {
                @Override
                public void success(List<Exemption> data, String message) {
                    display$Toast(message);
                    oas=data;
                    init();
                }
                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });
        }
//        HttpUtil.getList(Api.OA_GET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
//            @Override
//            public void success(List<Oa> data, String message) {
//                display$Toast(message);
//                oas=data;
//                init();
//            }
//            @Override
//            public void error(String message) {
//                display$Toast(message);
//            }
//        });
    }
    @SuppressLint("SetTextI18n")
    public void init(){
        Exemption oa1=null;
        for (Exemption oa : oas) {
           if (Objects.equals(oa.id, oaid)){
               oa1=oa;
           }
        }
        viewBinder.courseName.setText(oa1.lesson.name);
        viewBinder.applicationStatus.setText(oa1.status);
        viewBinder.approverName.setText("审批人：授课老师-"+oa1.lesson.teacher);
        loadImage(oa1.pic, viewBinder.exemption);
        if (oa1.status.equals("审核中")){
            viewBinder.teacherImg.setImageResource(R.drawable.chahao);
            viewBinder.approvalStatus.setText("状态：待审批");
            viewBinder.adminImg.setImageResource(R.drawable.chahao);
            viewBinder.approvalStatus0.setText("状态：待审批");
        }else if (oa1.status.equals("讲课教师审核通过")){
            viewBinder.teacherImg.setImageResource(R.drawable.ic_action_name);
            viewBinder.approvalStatus.setText("状态：已审批");
            viewBinder.adminImg.setImageResource(R.drawable.chahao);
            viewBinder.approvalStatus0.setText("状态：待审批");
        }else if (oa1.status.equals("审核通过")){
            viewBinder.teacherImg.setImageResource(R.drawable.ic_action_name);
            viewBinder.approvalStatus.setText("状态：已审批");
            viewBinder.adminImg.setImageResource(R.drawable.ic_action_name);
            viewBinder.approvalStatus0.setText("状态：已审批");
        }else{
            viewBinder.teacherImg.setImageResource(R.drawable.chahao);
            viewBinder.approvalStatus.setText("状态：拒绝");
            viewBinder.adminImg.setImageResource(R.drawable.chahao);
            viewBinder.approvalStatus0.setText("状态：拒绝");
        }
        viewBinder.applicationTime.setText(oa1.time);
        viewBinder.applicationReason.setText(oa1.reason);
        viewBinder.courseDescription.setText(oa1.lesson.des);
        if (oa1.rejectreason!=null){
            viewBinder.teacherComment.setText(oa1.rejectreason);
        }else{
            viewBinder.teacherComment.setText("暂无");
        }

    }
}
