package com.example.oa;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.oa.DB.OaDatabaseHelper;
import com.example.oa.Notification.NotificationHelper;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.Oa;
import com.example.oa.databinding.ActivityMyLessonBinding;
import com.example.oa.databinding.ItemMyLessonBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyLessonActivity extends BaseBindingActivity<ActivityMyLessonBinding> {
    private String selectedOption = "";
    private List<Oa> oas;
    OaDatabaseHelper databaseHelper=new OaDatabaseHelper(this);
        private BindAdapter<ItemMyLessonBinding, Oa> adapter = new BindAdapter<ItemMyLessonBinding, Oa>() {
        @Override
        public ItemMyLessonBinding createHolder(ViewGroup parent) {
            return ItemMyLessonBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemMyLessonBinding item, Oa oa, int position) {
            Lesson lesson = oa.lesson;
            item.tvName.setText(lesson.name);
            item.tvTeacher.setText("任课教师：" + lesson.teacher);
            item.tvStartTime.setText("开课时间：" + lesson.starTime);
            item.tvStatus.setText(oa.status);

            item.tvMydetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(OADetailActivity.class,intent -> intent.putExtra("oaid",oa.id));
                }
            });
        }
    };

    @Override
    protected void initListener() {

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
    protected void initData() {
        showLoading();
        viewBinder.postRecycler.setAdapter(adapter);
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid", App.user.id);
        HttpUtil.getList(Api.OA_StuGET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
            @Override
            public void success(List<Oa> data, String message) {
                display$Toast(message);
                adapter.getData().clear();
                adapter.getData().addAll(data);
                oas=data;
                select();
                if (data.size()!=0){
//                    NotificationHelper.createNotificationChannel(MyLessonActivity.this);
//                    for (Oa datum : data) {
//                        NotificationHelper.sendNotification(MyLessonActivity.this, "您有课程正在审批中", datum.lesson.name);
//                    }
                    List<Oa> oas = databaseHelper.getAllApprovals();
                    for (Oa datum : data) {
                        for (Oa oa : oas) {
                            if (datum.teacher.equals(oa.status)&&datum.lessonId.equals(oa.lessonId)&&datum.teacher.equals(oa.teacher)&&datum.userid.equals(oa.userid)){
                                databaseHelper.updateApproval(Long.parseLong(datum.id),oa.lessonId,oa.userid,oa.status,oa.teacher);
                            }
                        }
                    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}