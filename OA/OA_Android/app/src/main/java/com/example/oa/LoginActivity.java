package com.example.oa;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.oa.DB.DBHelper;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.User;
import com.example.oa.databinding.ActivityLoginBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;
import com.example.oa.util.PasswordUtils;

import java.util.HashMap;


/**
 * 登录界面
 */
public class LoginActivity extends BaseBindingActivity<ActivityLoginBinding> {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public void initListener() {
        viewBinder.goRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        viewBinder.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });


    }

    @Override
    protected void initData() {
        dbHelper = new DBHelper(this);
        db = dbHelper.getWritableDatabase();
    }

    private void login() {
        String username = viewBinder.etUsername.getText().toString().trim();
        String password = viewBinder.etPassword.getText().toString().trim();
        if (username.isEmpty()) {
            viewBinder.etUsername.setError("请输入账号");
            return;
        }
        if (password.isEmpty()) {
            viewBinder.etPassword.setError("请输入密码");
            return;
        }
        showLoading();
        HashMap<String, Object> params = new HashMap<>();
        String pwd=password;
        password=PasswordUtils.encryptPassword(password);
        params.put("username", username);
        params.put("password", password);
        Log.d("pwd",password);


        String finalPassword = password;
        HttpUtil.getModel(Api.LOGIN, params, User.class, new HttpUtil.OnNetModelListener<User>() {
            @Override
            public void success(User data, String message) {
                display$Toast(message);
                App.login(data);
                ContentValues values = new ContentValues();
                values.put("username", username);
                Log.d("pwdpwd", finalPassword);
                values.put("password", finalPassword);


                long result = dbHelper.insertOrUpdateUser(username,finalPassword,data.id);
                if (result != -1) {
                    // 数据插入成功
                    // 进行其他操作或显示成功消息
                    startActivity(MainActivity.class,intent -> intent.putExtra("username",username));
                    finish();
                } else {
                    // 数据插入失败
                    // 进行其他操作或显示错误消息
                    Toast.makeText(LoginActivity.this, "数据库登陆失败", Toast.LENGTH_SHORT).show();
                }
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
        db.close();
    }
}