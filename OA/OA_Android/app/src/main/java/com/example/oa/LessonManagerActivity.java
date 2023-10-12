package com.example.oa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.example.oa.DB.LessonDatabaseHelper;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.Lesson;
import com.example.oa.databinding.ActivityLessonManagerBinding;
import com.example.oa.databinding.ItemLessonBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.HashMap;
import java.util.List;

public class LessonManagerActivity extends BaseBindingActivity<ActivityLessonManagerBinding> {
    private LessonDatabaseHelper databaseHelper;
    private BindAdapter<ItemLessonBinding, Lesson> adapter = new BindAdapter<ItemLessonBinding, Lesson>() {
        @Override
        public ItemLessonBinding createHolder(ViewGroup parent) {
            return ItemLessonBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemLessonBinding item, Lesson lesson, int position) {
            item.tvName.setText(lesson.name);
            item.tvTeacher.setText("任课教师：" + lesson.teacher);
            item.tvStartTime.setText("开课时间：" + lesson.starTime+" "+lesson.dayOfweek);
            item.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (App.isManager()||App.isTeacher()) {
                        new AlertDialog.Builder(LessonManagerActivity.this)
                                .setTitle("注意")
                                .setItems(new CharSequence[]{"编辑", "删除"}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (which == 0) {
                                            startActivity(AddLessonActivity.class, intent -> intent.putExtra("lesson", lesson));

                                        } else {
                                            doDelete(lesson);
                                        }
                                    }
                                }).show();
                    }

                }
            });

        }
    };

    private void doDelete(Lesson notice) {
        showLoading();
        HashMap<String, Object> params = new HashMap<>();
        params.put("id", notice.id);
        HttpUtil.delete(Api.LESSON_DELETE, params, new HttpUtil.OnNetStringListener() {
            @Override
            public void success(String data, String message) {
                display$Toast(message);
                adapter.getData().remove(notice);
                adapter.notifyDataSetChanged();
                databaseHelper.deleteLesson(Long.parseLong(notice.id));
            }

            @Override
            public void error(String message) {
                display$Toast(message);
            }
        });
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initData() {
        databaseHelper=new LessonDatabaseHelper(this);
        viewBinder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LessonManagerActivity.this, AddLessonActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        viewBinder.postRecycler.setAdapter(adapter);
        getPostData();
    }

    private void getPostData() {
        showLoading();
        HashMap<String, Object> params = new HashMap<>();
        if (App.isTeacher()){
            params.put("teacher",App.user.username);
            HttpUtil.getList(Api.LESSON_GETBYTeacher, params, Lesson.class, new HttpUtil.OnNetListListener<Lesson>() {
                @Override
                public void success(List<Lesson> data, String message) {
                    dismissLoading();
                    adapter.getData().clear();
                    adapter.getData().addAll(data);
                    if (data.size()!=0){
                        viewBinder.tvSry.setVisibility(View.GONE);
                        viewBinder.imgSry.setVisibility(View.GONE);
                    }
                    List<Lesson> allLessons = databaseHelper.getAllLessons();
                    for (Lesson datum : data) {
                        for (Lesson allLesson : allLessons) {
                            if (datum.teacher.equals(allLesson.getTeacher())&&datum.name.equals(allLesson.getName())&&datum.des.equals(allLesson.getDes())&&datum.starTime.equals(allLesson.starTime)){
                                allLesson.id=datum.id;
                                databaseHelper.updateLesson(allLesson);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                    toast(message);
                }

                @Override
                public void error(String error) {
                    toast(error);
                    dismissLoading();
                }
            });
        }else if (App.isManager()){
            HttpUtil.getList(Api.LESSON_GET, params, Lesson.class, new HttpUtil.OnNetListListener<Lesson>() {
                @Override
                public void success(List<Lesson> data, String message) {
                    dismissLoading();
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
                public void error(String error) {
                    toast(error);
                    dismissLoading();
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getPostData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}
