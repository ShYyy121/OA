package com.example.oa;

import android.app.DatePickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.oa.DB.LessonDatabaseHelper;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.Lesson;
import com.example.oa.databinding.ActivityAddLessonBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AddLessonActivity extends BaseBindingActivity<ActivityAddLessonBinding> {
    private LessonDatabaseHelper databaseHelper;
    private String selectedText;
    private String weekDay = "";
    @Override
    protected void initListener() {

    }

    private Lesson lesson;
    private boolean isModify=false;

    @Override
    protected void initData() {

        databaseHelper = new LessonDatabaseHelper(this);
        lesson = (Lesson) getIntent().getSerializableExtra("lesson");
        Spinner spinner = viewBinder.spinner;

// 创建数据源，例如一个字符串数组
        String[] items = {"第一节（8：30-10：20）", "第二节（10：40-12：30）", "第三节（14：00-15：50）","第四节（16：10-18：00）","第五节（18：30-20：20）","第六节（20：40-22：20）"};

// 创建适配器
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

// 设置适配器
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 获取选中项的文本
                selectedText = parent.getItemAtPosition(position).toString();

//                // 在这里进行处理，例如显示选中项的文本
//                Toast.makeText(getApplicationContext(), "选择了：" + selectedText, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // 当没有选中项时的回调方法
            }
        });

        if (lesson != null) {
            int index = -1;

            for (int i = 0; i < items.length; i++) {
                if (lesson.lessontime.equals(items[i])) {
                    index = i ; // 将索引值加1，以获得1-6的结果
                    break;
                }
            }
            isModify=true;
            viewBinder.tvTitle.setText("编辑选修课");
            viewBinder.etTitle.setText(lesson.name);
            viewBinder.etTeahcer.setText(lesson.teacher);
            viewBinder.etTime.setText(lesson.starTime);
            viewBinder.etDesc.setText(lesson.des);
            viewBinder.spinner.setSelection(index);
        }else {
            viewBinder.etTeahcer.setText(App.user.username);
            viewBinder.etTeahcer.setEnabled(false);

            viewBinder.tvTitle.setText("添加选修课");
        }
        viewBinder.etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar instance = Calendar.getInstance();
                DatePickerDialog dialog = new DatePickerDialog(AddLessonActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        // 设置选择的日期到Calendar对象中
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year, month, dayOfMonth);

                        // 获取星期几，1 表示星期天，2 表示星期一，依此类推
                        int dayOfWeek = selectedDate.get(Calendar.DAY_OF_WEEK);

                        // 将数字转换为相应的星期几字符串
                        switch (dayOfWeek) {
                            case Calendar.SUNDAY:
                                weekDay = "星期日";
                                break;
                            case Calendar.MONDAY:
                                weekDay = "星期一";
                                break;
                            case Calendar.TUESDAY:
                                weekDay = "星期二";
                                break;
                            case Calendar.WEDNESDAY:
                                weekDay = "星期三";
                                break;
                            case Calendar.THURSDAY:
                                weekDay = "星期四";
                                break;
                            case Calendar.FRIDAY:
                                weekDay = "星期五";
                                break;
                            case Calendar.SATURDAY:
                                weekDay = "星期六";
                                break;
                        }

                        // 格式化日期字符串
                        String dateString = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);

                        // 显示日期和星期几
                        String displayString = dateString ;
                        viewBinder.etTime.setText(displayString);

                    }
                }, instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
                dialog.show();
            }
        });

        viewBinder.tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoading();
                String name = viewBinder.etTitle.getText().toString();
                String teacher = viewBinder.etTeahcer.getText().toString();
                String starTime = viewBinder.etTime.getText().toString();
                String desc=viewBinder.etDesc.getText().toString();
                String location=viewBinder.etLocation.getText().toString();
                Lesson newLesson = new Lesson();
                newLesson.setName(name);
                newLesson.setTeacher(teacher);
                newLesson.setStarTime(starTime);
                newLesson.setDes(desc);
                newLesson.setLocation(location);

                if (name.isEmpty()) {
                    return;
                }
                if (teacher.isEmpty()) {
                    return;
                }
                if (starTime.isEmpty()) {
                    return;
                }
                if (desc.isEmpty()){
                    return;
                }
                if (location.isEmpty()){
                    return;
                }
                if (selectedText.isEmpty()){return;}
                HashMap<String, Object> params = new HashMap<>();
                if (lesson != null) {
                    params.put("id", lesson.id);
                }
                params.put("starTime", starTime);
                params.put("name", name);
                params.put("teacher", teacher);
                params.put("des",desc);
                params.put("location",location);
                params.put("lessontime",selectedText);
                if (!isModify){
                    params.put("dayOfweek",weekDay);
                }
                HttpUtil.post(Api.LESSON_UPDATE, params,new HttpUtil.OnNetStringListener() {
                    @Override
                    public void success(String data, String message) {
                        display$Toast(message);
                        if (isModify) {
                            newLesson.setId(data);
                            databaseHelper.updateLesson(newLesson);
                        } else {
                            newLesson.setId(data);
                            databaseHelper.addLesson(newLesson);
                        }

                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void error(String message) {
                        display$Toast(message);
                    }
                });
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}