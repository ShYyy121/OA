package com.example.oa;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.base.BindAdapter1;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.Signin;
import com.example.oa.databinding.ActivitySignManageBinding;
import com.example.oa.databinding.ItemLessonBinding;
import com.example.oa.databinding.ItemSignManageBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.HashMap;
import java.util.List;

public class SignManageActivity extends BaseBindingActivity<ActivitySignManageBinding> {

    private BindAdapter1<ItemSignManageBinding, Signin> adapter = new BindAdapter1<ItemSignManageBinding, Signin>() {
        @Override
        public ItemSignManageBinding createHolder(ViewGroup parent) {
            return ItemSignManageBinding.inflate(getLayoutInflater(), parent, false);
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void bind(ItemSignManageBinding item, Signin signin, int position) {
            item.name.setText("学生姓名："+signin.student);
            item.tvSex.setText("性别："+signin.user.sex);
            item.phone.setText("签到时间："+signin.time);
            item.tvDate.setText("日期:"+ signin.date);
            item.tvLessonname.setText("课程："+signin.lesson.name);
            loadImage(signin.user.face, item.face);
            if (signin.issigned.equals("true")){
                item.tvLesson.setText("签到情况： "+"成功 在指定位置签到");
            }else{
                item.tvLesson.setText("签到情况： "+"失败 未在指定位置签到");
            }

//            item.getRoot().setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if (App.isManager()) {
//                        new AlertDialog.Builder(SignManageActivity.this)
//                                .setTitle("注意")
//                                .setItems(new CharSequence[]{"编辑", "删除"}, new DialogInterface.OnClickListener() {
//                                    @Override
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        if (which == 0) {
//                                            startActivity(AddLessonActivity.class, intent -> intent.putExtra("lesson", lesson));
//                                        } else {
//                                            doDelete(lesson);
//                                        }
//                                    }
//                                }).show();
//                    }
//
//                }
//            });

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
        params.put("userid",App.user.username);
        HttpUtil.getList(Api.Sign_GET, params, Signin.class, new HttpUtil.OnNetListListener<Signin>() {
            @Override
            public void success(List<Signin> data, String message) {
                dismissLoading();

                adapter.getData().clear();
                adapter.getData().addAll(data);
                if (data.size()!=0){
                    viewBinder.tvSry.setVisibility(View.GONE);
                    viewBinder.imgSry.setVisibility(View.GONE);
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPostData();
    }
}