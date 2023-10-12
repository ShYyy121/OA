package com.example.oa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.oa.DB.OaDatabaseHelper;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.Lesson;
import com.example.oa.databinding.ActivityGetLessonBinding;
import com.example.oa.databinding.ItemGetLessonBinding;
import com.example.oa.databinding.ItemLessonBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.HashMap;
import java.util.List;

public class GetLessonActivity extends BaseBindingActivity<ActivityGetLessonBinding> {
    private OaDatabaseHelper oaDatabaseHelper=new OaDatabaseHelper(this);
    private static final int REQUEST_CODE_ANOTHER_ACTIVITY = 1;

    private BindAdapter<ItemGetLessonBinding, Lesson> adapter = new BindAdapter<ItemGetLessonBinding, Lesson>() {
        @Override
        public ItemGetLessonBinding createHolder(ViewGroup parent) {
            return ItemGetLessonBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemGetLessonBinding item, Lesson lesson, int position) {
            item.tvName.setText(lesson.name);
            item.tvTeacher.setText("任课教师：" + lesson.teacher);
            item.tvStartTime.setText("开课时间：" + lesson.starTime);
            item.tvLdetail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(LessonDetailActivity.class,intent -> intent.putExtra("lesson",lesson));
                }
            });

            item.tvGet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(GetLessonActivity.this,GetReasonActivity.class);
                    intent.putExtra("lesson",lesson);
                    startActivityForResult(intent, REQUEST_CODE_ANOTHER_ACTIVITY);

                    adapter.notifyDataSetChanged();
//                    showLoading();
//                    HashMap<String, Object> params = new HashMap<>();
//                    params.put("lessonId", lesson.id);
//                    params.put("userid", App.user.id);
//
//                    params.put("teacher",lesson.teacher);
//                    HttpUtil.post(Api.OA_ADD, params, new HttpUtil.OnNetStringListener() {
//                        @Override
//                        public void success(String data, String message) {
//                            display$Toast(message);
//                        }
//
//                        @Override
//                        public void error(String message) {
//                            display$Toast(message);
//                        }
//                    });
                }
            });
        }
    };

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        viewBinder.postRecycler.setAdapter(adapter);
        getPostData();
    }

    private void getPostData() {
        showLoading();
        HashMap<String, Object> params = new HashMap<>();
        params.put("userid",App.user.id);
        HttpUtil.getList(Api.LESSON_GETBYTSTU, params, Lesson.class, new HttpUtil.OnNetListListener<Lesson>() {
            @Override
            public void success(List<Lesson> data, String message) {
                dismissLoading();
                adapter.getData().clear();
                adapter.getData().addAll(data);
                adapter.notifyDataSetChanged();
                if (data.size()!=0){
                    viewBinder.tvSry.setVisibility(View.GONE);
                    viewBinder.imgSry.setVisibility(View.GONE);
                }

                toast(message);
            }

            @Override
            public void error(String error) {
                toast(error);
                dismissLoading();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from the activity you started
        if (requestCode == REQUEST_CODE_ANOTHER_ACTIVITY) {
            // Check if the result is successful
            if (resultCode == RESULT_OK) {
                // Refresh the data
                getPostData();
            }
        }
    }

}