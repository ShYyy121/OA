package com.example.oa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.Lesson;
import com.example.oa.databinding.ActivityLessonDetailBinding;

public class LessonDetailActivity extends BaseBindingActivity<ActivityLessonDetailBinding> {
    Lesson lesson=null;

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
        lesson= (Lesson) getIntent().getSerializableExtra("lesson");
        viewBinder.etTitle.setText(lesson.name);
        viewBinder.etTeahcer.setText(lesson.teacher);
        viewBinder.etTime.setText(lesson.starTime+lesson.dayOfweek);
        viewBinder.etLocation.setText(lesson.location);
        viewBinder.etDesc.setText(lesson.des);
        viewBinder.spinner.setText(lesson.lessontime);
    }
}