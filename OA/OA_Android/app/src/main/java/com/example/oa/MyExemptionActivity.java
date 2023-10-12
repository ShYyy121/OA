package com.example.oa;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewGroup;

import com.example.oa.DB.OaDatabaseHelper;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.Exemption;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.Oa;
import com.example.oa.databinding.ActivityMyExemptionBinding;
import com.example.oa.databinding.ActivityMyLessonBinding;
import com.example.oa.databinding.ItemMyLessonBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.HashMap;
import java.util.List;

public class MyExemptionActivity extends BaseBindingActivity<ActivityMyExemptionBinding> {

    OaDatabaseHelper databaseHelper=new OaDatabaseHelper(this);
        private BindAdapter<ItemMyLessonBinding, Exemption> adapter = new BindAdapter<ItemMyLessonBinding, Exemption>() {
        @Override
        public ItemMyLessonBinding createHolder(ViewGroup parent) {
            return ItemMyLessonBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemMyLessonBinding item, Exemption exemption, int position) {
            Lesson lesson = exemption.lesson;
            item.tvName.setText(lesson.name);
            item.tvTeacher.setText("任课教师：" + lesson.teacher);
            item.tvStartTime.setText("开课时间：" + lesson.starTime);
            item.tvStatus.setText(exemption.status);

            item.tvMydetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(ExemptionDetailActivity.class,intent -> intent.putExtra("eid",exemption.id));
                }
            });
        }
    };

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        showLoading();
        viewBinder.postRecycler.setAdapter(adapter);
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid", App.user.id);
        HttpUtil.getList(Api.EXEMPTION_StuGET, params, Exemption.class, new HttpUtil.OnNetListListener<Exemption>() {
            @Override
            public void success(List<Exemption> data, String message) {
                display$Toast(message);
                adapter.getData().clear();
                adapter.getData().addAll(data);
                if (data.size()!=0){
//                    NotificationHelper.createNotificationChannel(MyLessonActivity.this);
//                    for (Oa datum : data) {
//                        NotificationHelper.sendNotification(MyLessonActivity.this, "您有课程正在审批中", datum.lesson.name);
//                    }
//                    List<Exemption> oas = databaseHelper.getAllApprovals();
//                    for (Exemption datum : data) {
//                        for (Oa oa : oas) {
//                            if (datum.teacher.equals(oa.status)&&datum.lessonId.equals(oa.lessonId)&&datum.teacher.equals(oa.teacher)&&datum.userid.equals(oa.userid)){
//                                databaseHelper.updateApproval(Long.parseLong(datum.id),oa.lessonId,oa.userid,oa.status,oa.teacher);
//                            }
//                        }
//                    }

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