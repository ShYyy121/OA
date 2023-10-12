package com.example.oa;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.Oa;
import com.example.oa.databinding.ActivityMyCourseBinding;
import com.example.oa.databinding.ItemLessonBinding;
import com.example.oa.databinding.ListItemCourseBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyCourseActivity extends BaseBindingActivity<ActivityMyCourseBinding> {
    private List<Oa> oas=new ArrayList<>();
    private List<Lesson> lessons=new ArrayList<>();

    private BindAdapter<ListItemCourseBinding, Lesson> adapter = new BindAdapter<ListItemCourseBinding, Lesson>() {
        @Override
        public ListItemCourseBinding createHolder(ViewGroup parent) {
            return ListItemCourseBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ListItemCourseBinding item, Lesson lesson, int position) {
            item.tvCourseName.setText(lesson.name);
            item.tvDayOfWeek.setText(lesson.dayOfweek);
            item.tvTime.setText(lesson.starTime);


//            // 假设您有一个存储了课程和位置的数组
//            String[] courses = {"语文", "数学", "英语", "物理", "化学", "生物", "历史"};
//            String[] locations = {"教室A", "教室B", "教室C", "教室D", "教室E", "教室F", "教室G"};


//            for (int i = 0; i < 7; i++) {
//                for (int j = 0; j < 6; j++) {
//                    String courseIdString = "tv_" + (i + 1) + (j + 1);
//                    String locationIdString = "tv1_" + (i + 1) + (j + 1);
//
//                    int courseId = getResources().getIdentifier(courseIdString, "id", getPackageName());
//                    int locationId = getResources().getIdentifier(locationIdString, "id", getPackageName());
//
//
//
//                    TextView courseTextView = viewBinder.getRoot().findViewById(courseId);
//                    TextView locationTextView = viewBinder.getRoot().findViewById(locationId);
//
//                    courseTextView.setVisibility(View.VISIBLE);
//                    if (transformdate(lesson.dayOfweek)==(i+1)&&transformtime(lesson.lessontime)==(j+1)){
//                        courseTextView.setVisibility(View.VISIBLE);
//                        locationTextView.setVisibility(View.VISIBLE);
//                        courseTextView.setText(lesson.name);
//                        locationTextView.setText(lesson.location);
//                    }
//
////                        // 设置课程和位置
////                        courseTextView.setText(courses[i]);
////                    locationTextView.setText(locations[i]);
//                }
//            }





        }

    };
    public int transformtime(String lessontime){
        int lessonTimeNumber;

        switch (lessontime) {
            case "第一节（8：30-10：20）":
                lessonTimeNumber = 1;
                break;
            case "第二节（10：40-12：30）":
                lessonTimeNumber = 2;
                break;
            case "第三节（14：00-15：50）":
                lessonTimeNumber = 3;
                break;
            case "第四节（16：10-18：00）":
                lessonTimeNumber = 4;
                break;
            case "第五节（18：30-20：20）":
                lessonTimeNumber = 5;
                break;
            case "第六节（20：40-22：20）":
                lessonTimeNumber = 6;
                break;
            default:
                // 如果lesson.lessontime不匹配任何一个值，则设置一个默认值，例如-1
                lessonTimeNumber = -1;
                break;
        }

// 输出转换后的结果
//        System.out.println("lessonTimeNumber: " + lessonTimeNumber);
        return lessonTimeNumber;
    }


    public int transformdate(String dayOfweek){
        int dayOfWeekNumber;
        switch (dayOfweek) {
            case "星期一":
                dayOfWeekNumber = 1;
                break;
            case "星期二":
                dayOfWeekNumber = 2;
                break;
            case "星期三":
                dayOfWeekNumber = 3;
                break;
            case "星期四":
                dayOfWeekNumber = 4;
                break;
            case "星期五":
                dayOfWeekNumber = 5;
                break;
            case "星期六":
                dayOfWeekNumber = 6;
                break;
            case "星期日":
                dayOfWeekNumber = 7;
                break;
            default:
                // 如果lesson.dayOfweek不匹配任何一个值，则设置一个默认值，例如-1
                dayOfWeekNumber = -1;
                break;
        }

// 输出转换后的结果
//        System.out.println("dayOfWeekNumber: " + dayOfWeekNumber);
        return dayOfWeekNumber;
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        viewBinder.postRecycler.setAdapter(adapter);
        getPostData();




    }

    public void init(){
        for (Lesson lesson : lessons) {
            for (int i = 0; i < 7; i++) {
                for (int j = 0; j < 6; j++) {
                    String courseIdString = "tv_" + (i + 1) + (j + 1);
                    String locationIdString = "tv1_" + (i + 1) + (j + 1);

                    int courseId = getResources().getIdentifier(courseIdString, "id", getPackageName());
                    int locationId = getResources().getIdentifier(locationIdString, "id", getPackageName());



                    TextView courseTextView = viewBinder.getRoot().findViewById(courseId);
                    TextView locationTextView = viewBinder.getRoot().findViewById(locationId);


                    if (transformdate(lesson.dayOfweek)==(i+1)&&transformtime(lesson.lessontime)==(j+1)){
                        courseTextView.setVisibility(View.VISIBLE);
                        locationTextView.setVisibility(View.VISIBLE);
                        courseTextView.setText(lesson.name);
                        int backgroundColor = Color.parseColor("#7fffd4");
                        courseTextView.setBackgroundColor(backgroundColor);
                        locationTextView.setBackgroundColor(backgroundColor);
                        locationTextView.setText(lesson.location);
                        courseTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLessonDialog(lesson);
                            }
                        });
                       locationTextView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showLessonDialog(lesson);
                            }
                        });

                    }

//                        // 设置课程和位置
//                        courseTextView.setText(courses[i]);
//                    locationTextView.setText(locations[i]);
                }
            }
        }
    }
    private void showLessonDialog(Lesson lesson) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("课程详情");

        StringBuilder message = new StringBuilder();
        message.append("课程名称：").append(lesson.name).append("\n")
                .append("上课时间：").append(lesson.dayOfweek).append(" ").append(lesson.starTime).append("\n")
                .append("讲课教师：").append(lesson.teacher).append("\n")
                .append("上课地点：").append(lesson.location);

        builder.setMessage(message.toString());

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setNegativeButton("申请免修", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openWriteReasonActivity(lesson);
            }
        });

        AlertDialog dialog = builder.create();

        // 设置对话框背景色
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_background);

        // 设置对话框文本颜色
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button positiveButton = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                positiveButton.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

        dialog.show();
    }
    private void openWriteReasonActivity(Lesson lesson) {
        Intent intent = new Intent(this, WriteReasonActivity.class);
        intent.putExtra("lesson", lesson);
        startActivity(intent);
    }

    private void getPostData() {
//        showLoading();
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid",App.user.id);
        HttpUtil.getList(Api.OA_StuGET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
            @Override
            public void success(List<Oa> data, String message) {
                dismissLoading();
                oas=data;
                get();
//                toast(message);
            }

            @Override
            public void error(String error) {
                toast(error);
                dismissLoading();
            }
        });
    }

    public void get(){
        for (Oa oa : oas) {
            showLoading();
            if (oa.status.equals("审核通过")){
                HashMap<String, Object> params1 = new HashMap<>();
                params1.put("id",oa.lesson.id);
                params1.put("userid",App.user.id);
                HttpUtil.getList(Api.LESSON_GETBYID, params1, Lesson.class, new HttpUtil.OnNetListListener<Lesson>() {
                    @Override
                    public void success(List<Lesson> data, String message) {
                        dismissLoading();
                        lessons=data;
                        init();
                        adapter.getData().clear();
                        adapter.getData().addAll(data);
                        adapter.notifyDataSetChanged();
//                        toast(message);
                    }

                    @Override
                    public void error(String error) {
                        toast(error);
                        dismissLoading();
                    }
                });
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getPostData();
        }
    }
}