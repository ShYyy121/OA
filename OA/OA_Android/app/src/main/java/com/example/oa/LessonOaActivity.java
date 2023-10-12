package com.example.oa;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.Oa;
import com.example.oa.databinding.ActivityLessonOaBinding;
import com.example.oa.databinding.ItemUserBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LessonOaActivity extends BaseBindingActivity<ActivityLessonOaBinding> {
    private static final int REQUEST_CODE_REJECT_REASON = 1;
    private String selectedOption = "";
    private List<Oa> oas;
    int n=0;
    Oa oa=null;
    private String username;
    private boolean isManager;
    public static String resultValue=null;
    private BindAdapter<ItemUserBinding, Oa> adapter = new BindAdapter<ItemUserBinding, Oa>() {
        @Override
        public ItemUserBinding createHolder(ViewGroup parent) {
            return ItemUserBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemUserBinding item, Oa data, int position) {
            oa=data;
            item.tvLesson.setText("申请选修：  " + data.lesson.name);
            item.name.setText("姓名：" + data.user.nickname);
            item.tvSex.setText("性别：" + data.user.sex);
            item.tvTeacher.setText("授课老师："+data.lesson.teacher);
            item.phone.setText("账号：" + data.user.username);
            loadImage(data.user.face, item.face);
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
                    startActivity(OADetailActivity.class,intent -> intent.putExtra("oaid", data.id));
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
                    Intent intent = new Intent(LessonOaActivity.this, RejectReasonActivity.class);
                    intent.putExtra("oa", oa);
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


    private void doUpdate(Oa oa) {
        showLoading();
        if (App.isTeacher()){
            HttpUtil.post(Api.OA_TeacherUPDATE, HttpUtil.toJSON(oa), new HttpUtil.OnNetStringListener() {
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
            HttpUtil.post(Api.OA_UPDATE, HttpUtil.toJSON(oa), new HttpUtil.OnNetStringListener() {
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
            HttpUtil.getList(Api.OA_GET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
                @Override
                public void success(List<Oa> data, String message) {
                    display$Toast(message);
                    adapter.getData().clear();
                    adapter.getData().addAll(data);
                    oas=data;
                    select();
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
            HttpUtil.getList(Api.OA_TeacherGET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
                @Override
                public void success(List<Oa> data, String message) {
                    display$Toast(message);
                    adapter.getData().clear();
                    adapter.getData().addAll(data);
                    oas=data;
                    select();
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


    public void select(){
        // 创建适配器并设置选项数据
        ArrayAdapter<String> adapter0 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        adapter0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter0.addAll("无","审核中", "主讲教师审核通过", "审核通过", "审核拒绝");

// 设置适配器
        viewBinder.spinner.setAdapter(adapter0);
        viewBinder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                switch (position) {
                    case 0:
                        adapter.getData().clear();
                        adapter.getData().addAll(oas);
                        adapter.notifyDataSetChanged();
                        break;
                    case 1:
                        selectedOption = "审核中";
                        List<Oa> filteredOas = new ArrayList<>();

                        for (Oa oa : oas) {
                            if (oa.getStatus().equals(selectedOption)) {
                                filteredOas.add(oa);
                            }
                        }
                        adapter.getData().clear();
                        adapter.getData().addAll(filteredOas);
                        adapter.notifyDataSetChanged();
                        break;
                    case 2:
                        selectedOption = "讲师审核通过";
                        List<Oa> filteredOas1 = new ArrayList<>();

                        for (Oa oa : oas) {
                            if (oa.getStatus().equals(selectedOption)) {
                                filteredOas1.add(oa);
                            }
                        }
                        adapter.getData().clear();
                        adapter.getData().addAll(filteredOas1);
                        adapter.notifyDataSetChanged();
                        break;
                    case 3:
                        selectedOption = "审核通过";
                        List<Oa> filteredOas2 = new ArrayList<>();

                        for (Oa oa : oas) {
                            if (oa.getStatus().equals(selectedOption)) {
                                filteredOas2.add(oa);
                            }
                        }
                        adapter.getData().clear();
                        adapter.getData().addAll(filteredOas2);
                        adapter.notifyDataSetChanged();
                        break;
                    case 4:
                        selectedOption = "审核拒绝";
                        List<Oa> filteredOas3 = new ArrayList<>();

                        for (Oa oa : oas) {
                            if (oa.getStatus().equals(selectedOption)) {
                                filteredOas3.add(oa);
                            }
                        }
                        adapter.getData().clear();
                        adapter.getData().addAll(filteredOas3);
                        adapter.notifyDataSetChanged();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.getData().clear();
                adapter.getData().addAll(oas);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}