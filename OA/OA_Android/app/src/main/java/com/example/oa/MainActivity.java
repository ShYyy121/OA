package com.example.oa;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.oa.DB.DBHelper;
import com.example.oa.Notification.NotificationHelper;
import com.example.oa.Notification.UpdateCheckService;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.HomeAction;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.Oa;
import com.example.oa.bean.User;
import com.example.oa.databinding.ActivityMainBinding;
import com.example.oa.databinding.ItemHomeActionBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;
import com.example.oa.util.PasswordUtils;
import com.example.oa.util.ThemeUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //设置状态栏透明

        ThemeUtils.applyTheme(this);
        makeStatusBarTransparent(this);
        startService(new Intent(this, UpdateCheckService.class));
        //状态栏文字自适应
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);



        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();

        super.onCreate(savedInstanceState);

    }

    private String username;

    private BindAdapter<ItemHomeActionBinding, HomeAction> adapter = new BindAdapter<ItemHomeActionBinding, HomeAction>() {

        @Override
        public ItemHomeActionBinding createHolder(ViewGroup parent) {

            return ItemHomeActionBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemHomeActionBinding itemHomeActionBinding, HomeAction homeAction, int position) {
            itemHomeActionBinding.nameTv.setText(homeAction.getName());
            itemHomeActionBinding.icon.setImageResource(homeAction.getIconId());
            itemHomeActionBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (homeAction.getName()) {
                        case "选修课管理":
                            startActivity(LessonManagerActivity.class);
                            break;
                        case "选修审批":
                            startActivity(LessonOaActivity.class,intent -> intent.putExtra("username",username));
                            break;
                        case "申请选修":
                            startActivity(GetLessonActivity.class);
                            break;
                        case "我的选修":
                            startActivity(MyLessonActivity.class);
                            break;
                        case "个人信息管理":
                            startActivity(RegisterActivity.class, intent -> intent.putExtra("user", App.user));
                            finish();
                            break;
                        case "签到" :
                            startActivity(SignInActivity.class);
                            break;
                        case "我的课程表":
                            startActivity(MyCourseActivity.class);
                            break;
                        case "出勤管理":
                            startActivity(SignManageActivity.class);
                            break;
                        case "免修审批":
                            startActivity(LessonExemptionActivity.class);
                            break;
                        case  "我的免修":
                            startActivity(MyExemptionActivity.class);
                            break;
                        case "主题切换":

                            if (App.theme%2==0) {
                                // 当前是白天模式
                                // 执行相应的操作
                                ThemeUtils.setTheme(MainActivity.this, R.style.Theme_OA_Purple);
                                App.theme++;
                            } else {
                                // 当前是夜间模式
                                // 执行相应的操作
                                ThemeUtils.setTheme(MainActivity.this, R.style.Theme_OA_DAY);
                                App.theme++;
                            }


                            recreate();
                            break;
                    }
                }
            });
        }
    };

    @Override
    protected void initListener() {

    }




    @Override
    protected void initData() {
        username = (String) getIntent().getSerializableExtra("username");
        if (!App.isLogin()) {
            startActivity(LoginActivity.class);
            finish();
            return;
        }
        if (username==null){
            HashMap<String, Object> params = new HashMap<>();
            String username1 = App.user.username;
            String tableName = "users";
//            dbHelper.deleteTable(tableName);

            params.put("username", username1);
            String password = dbHelper.getPasswordByUsername(username1);
            params.put("password", password);
            HttpUtil.getModel(Api.LOGIN, params, User.class, new HttpUtil.OnNetModelListener<User>() {
                @Override
                public void success(User data, String message) {
                    display$Toast(data.username+"：欢迎回来");
                    App.login(data);
                    notification();
                }
                @Override
                public void error(String message) {
                    display$Toast(message);
                    Toast.makeText(MainActivity.this, "登录信息失效,请重新登录", Toast.LENGTH_SHORT).show();
                    startActivity(LoginActivity.class);
                    finish();
                    return;
                }
            });
        }
        refresh();



        loadImage(App.user.getFace(), viewBinder.headView);
        if (App.isManager()) {
            adapter.getData().add(new HomeAction("个人信息管理", R.drawable.ic_user));
            adapter.getData().add(new HomeAction("选修课管理", R.drawable.ic_service));
            adapter.getData().add(new HomeAction("选修审批", R.drawable.ic_callback));
            adapter.getData().add(new HomeAction("免修审批", R.drawable.exemption));
            adapter.getData().add(new HomeAction("主题切换",R.drawable.topic));
        } else if (App.isTeacher()) {
            adapter.getData().add(new HomeAction("个人信息管理", R.drawable.ic_user));
            adapter.getData().add(new HomeAction("选修课管理", R.drawable.ic_service));
            adapter.getData().add(new HomeAction("选修审批", R.drawable.ic_callback));
            adapter.getData().add(new HomeAction("出勤管理",R.drawable.signin));
            adapter.getData().add(new HomeAction("免修审批",R.drawable.exemption));
            adapter.getData().add(new HomeAction("主题切换",R.drawable.topic));
        } else {
            adapter.getData().add(new HomeAction("个人信息管理", R.drawable.ic_user));
            adapter.getData().add(new HomeAction("申请选修", R.drawable.ic_notice));
            adapter.getData().add(new HomeAction("我的选修", R.drawable.ic_service));
            adapter.getData().add(new HomeAction("签到",R.drawable.qiandao));
            adapter.getData().add(new HomeAction("我的课程表",R.drawable.course));
            adapter.getData().add(new HomeAction("我的免修",R.drawable.myexemption));
            adapter.getData().add(new HomeAction("主题切换",R.drawable.topic));
        }

        viewBinder.recycler.setAdapter(adapter);
        viewBinder.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                App.logout();
                finish();
            }
        });
        viewBinder.transfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,SwitchAccountActivity.class));
                finish();
            }
        });
    }

    private void notification(){
        if (App.isManager()){
            HashMap<String, Object> params = new HashMap<>();
            params.put("userid", App.user.username);
            HttpUtil.getList(Api.OA_GET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
                @Override
                public void success(List<Oa> data, String message) {
                    boolean isapprove=true;
                    for (Oa datum : data) {
                        if (datum.status.equals("讲课教师审核通过")){
                            isapprove=false;
                            break;
                        }
                    }
                    if(!isapprove){
                        NotificationHelper.createNotificationChannel(MainActivity.this);
                        NotificationHelper.sendNotification(MainActivity.this, "您有待审批内容","进入审批界面查看详情");
                    }
                }

                @Override
                public void error(String message) {

                }
            });
        } else if (App.isTeacher()) {
            HashMap<String, Object> params = new HashMap<>();
            params.put("userid", App.user.username);
            HttpUtil.getList(Api.OA_TeacherGET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
                @Override
                public void success(List<Oa> data, String message) {
                    boolean isapprove=true;
                    for (Oa datum : data) {
                        if (datum.status.equals("审核中")){
                            isapprove=false;
                            break;
                        }
                    }
                    if(!isapprove){
                        NotificationHelper.createNotificationChannel(MainActivity.this);
                        NotificationHelper.sendNotification(MainActivity.this, "您有待审批内容","进入审批界面查看详情");
                    }
                }
                @Override
                public void error(String message) {

                }
            });
        }else{
            HashMap<String, Object> params1 = new HashMap<>();
            params1.put("userid", App.user.id);
            HttpUtil.getList(Api.OA_StuGET, params1, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
                @Override
                public void success(List<Oa> data, String message) {
                    if (data.size()!=0){

                        for (Oa datum : data) {
                            NotificationHelper.sendNotification(MainActivity.this, "您有课程正在审批中", datum.lesson.name);
                        }

                    }
                }

                @Override
                public void error(String message) {

                    display$Toast(message);
                }
            });
            HashMap<String, Object> params = new HashMap<>();
            params.put("userid",App.user.id);
            HttpUtil.getList(Api.LESSON_GETBYTSTU, params, Lesson.class, new HttpUtil.OnNetListListener<Lesson>() {
                @Override
                public void success(List<Lesson> data, String message) {
                    if (data.size() != 0) {
                        NotificationHelper.createNotificationChannel(MainActivity.this);
                        for (Lesson datum : data) {
                            NotificationHelper.sendNotification(MainActivity.this, "您有课程可以供选择", datum.name);
                        }
                    }
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
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        viewBinder.nameTv.setText(App.user.nickname);
        viewBinder.sexTv.setText(App.user.sex);
        viewBinder.phoneTv.setText(App.user.username);
    }
    public static void makeStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }
        Window window = activity.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int option = window.getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            window.getDecorView().setSystemUiVisibility(option);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }


}