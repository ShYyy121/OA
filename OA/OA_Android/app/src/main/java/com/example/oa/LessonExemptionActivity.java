package com.example.oa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.Exemption;
import com.example.oa.bean.Oa;
import com.example.oa.databinding.ActivityLessonExmptionBinding;
import com.example.oa.databinding.ActivityLessonOaBinding;
import com.example.oa.databinding.ItemUserBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.HashMap;
import java.util.List;

public class LessonExemptionActivity extends BaseBindingActivity<ActivityLessonExmptionBinding> {
    private static final int REQUEST_CODE_REJECT_REASON = 1;


    int n=0;
    Exemption exemption=null;
    private String username;
    private boolean isManager;
    public static String resultValue=null;
    private BindAdapter<ItemUserBinding, Exemption> adapter = new BindAdapter<ItemUserBinding, Exemption>() {
        @Override
        public ItemUserBinding createHolder(ViewGroup parent) {
            return ItemUserBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemUserBinding item, Exemption data, int position) {
            exemption=data;
            item.tvLesson.setText("申请免修：  " + data.lesson.name);
            item.name.setText("姓名：" + data.user.nickname);
            item.tvSex.setText("性别：" + data.user.sex);
            item.tvTeacher.setText("授课老师："+data.lesson.teacher);
            item.phone.setText("账号：" + data.user.username);
            loadImage(data.pic, item.face);
            if (data.status.equals("审核中")) {

                    item.tvPass.setVisibility(View.VISIBLE);
                    item.tvReject.setVisibility(View.VISIBLE);
                    item.tvStatus.setVisibility(View.GONE);
                    item.detail.setVisibility(View.VISIBLE);

            } else if (data.status.equals("讲课教师审核通过")){
                if (App.user.username.equals("admin")){
                    item.tvPass.setVisibility(View.VISIBLE);
                    item.tvReject.setVisibility(View.VISIBLE);
                    item.tvStatus.setVisibility(View.GONE);
                    item.detail.setVisibility(View.VISIBLE);
                }else{
                    item.tvPass.setVisibility(View.GONE);
                    item.tvReject.setVisibility(View.GONE);
                    item.tvStatus.setVisibility(View.VISIBLE);
                    item.tvStatus.setText("您已审批通过\n请等待主管审批");
                    item.detail.setVisibility(View.VISIBLE);
                }
            }else{
                item.tvPass.setVisibility(View.GONE);
                item.tvReject.setVisibility(View.GONE);
                item.tvStatus.setVisibility(View.VISIBLE);
                item.tvStatus.setText(data.status);
                item.detail.setVisibility(View.VISIBLE);
            }
            item.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ExemptionDetailActivity.class,intent -> intent.putExtra("eid", data.id));
                }
            });

            item.tvPass.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isManager){
                        data.status = "审核通过";
                        doUpdate(data);
                    } else{
                        data.status="讲课教师审核通过";
                        doUpdate(data);
                    }
                }
            });

            item.tvReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(LessonExemptionActivity.this, ExemptionRejectReasonActivity.class);
                    intent.putExtra("exemption", exemption);
                    startActivityForResult(intent, REQUEST_CODE_REJECT_REASON);
                    data.status = "审核拒绝";

//                    if (resultValue!=null){
//                        data.rejectreason=resultValue;
//                        doUpdate(data);
//                        adapter.notifyDataSetChanged();
//                    }

                }
            });
        }
    };


    private void doUpdate(Exemption exemption) {
        showLoading();
        if (App.isTeacher()){
            HttpUtil.post(Api.EXEMPTION_TeacherUPDATE, HttpUtil.toJSON(exemption), new HttpUtil.OnNetStringListener() {
                @Override
                public void success(String data, String message) {
                    display$Toast(message);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });
        }else {
            HttpUtil.post(Api.EXEMPTION_UPDATE, HttpUtil.toJSON(exemption), new HttpUtil.OnNetStringListener() {
                @Override
                public void success(String data, String message) {
                    display$Toast(message);
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });
        }

    }

    @Override
    protected void initListener() {

    }
    @Override
    protected void initData() {
//        username=(String) getIntent().getSerializableExtra("username");
        username=App.user.username;
        isManager= username.equals("admin");
        showLoading();
        viewBinder.postRecycler.setAdapter(adapter);
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid", App.user.username);
        if (App.isManager()){
            HttpUtil.getList(Api.EXEMPTION_GET, params, Exemption.class, new HttpUtil.OnNetListListener<Exemption>() {
                @Override
                public void success(List<Exemption> data, String message) {
                    display$Toast(message);
                    adapter.getData().clear();
                    adapter.getData().addAll(data);
                    if (data.size()!=0){
                        viewBinder.tvSry.setVisibility(View.GONE);
                        viewBinder.imgSry.setVisibility(View.GONE);
                    }
                    toast(message);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });
        }else if (App.isTeacher()){
            HttpUtil.getList(Api.EXEMPTION_TeacherGET, params, Exemption.class, new HttpUtil.OnNetListListener<Exemption>() {
                @Override
                public void success(List<Exemption> data, String message) {
                    display$Toast(message);
                    adapter.getData().clear();
                    adapter.getData().addAll(data);
                    if (data.size()!=0){
                        viewBinder.tvSry.setVisibility(View.GONE);
                        viewBinder.imgSry.setVisibility(View.GONE);
                    }
                    toast(message);
                    adapter.notifyDataSetChanged();

                }

                @Override
                public void error(String message) {
                    display$Toast(message);
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode  == REQUEST_CODE_REJECT_REASON && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                resultValue = data.getStringExtra("rejectreason");
                // 处理返回的结果值
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}