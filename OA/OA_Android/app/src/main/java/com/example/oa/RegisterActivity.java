package com.example.oa;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.bean.User;
import com.example.oa.databinding.ActivityRegisterBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;
import com.example.oa.util.PasswordUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class RegisterActivity extends BaseBindingActivity<ActivityRegisterBinding> {
    public void initListener() {
        viewBinder.tvRegister.setOnClickListener(v -> register());
        viewBinder.ivImage.setOnClickListener(v -> startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 100));
    }
private int isedit=0;
    private User user;

    @Override
    protected void initData() {
        user = (User) getIntent().getSerializableExtra("user");
        if (user != null) {
            viewBinder.titleTv.setText("用户编辑");
            viewBinder.etPhone.setText(user.username);
            viewBinder.etPhone.setEnabled(false);
            viewBinder.etNickname.setText(user.nickname);

//            viewBinder.etPassword.setText(user.password);
            face = user.face;
            viewBinder.tvRegister.setText("保存");
            loadImage(user.face, viewBinder.ivImage);
            isedit=1;
        } else {
            viewBinder.titleTv.setText("注册");
        }

    }


    private void register() {
        String[] sex = getResources().getStringArray(R.array.sex);
        String nickname = viewBinder.etNickname.getText().toString().trim();
        String username = viewBinder.etPhone.getText().toString().trim();
        String password = viewBinder.etPassword.getText().toString().trim();
        if (username.isEmpty()) {
            viewBinder.etPhone.setError("请输入账号");
            return;
        }
        if (nickname.isEmpty()) {
            viewBinder.etPhone.setError("请输入昵称");
            return;
        }
        if (password.isEmpty()) {
            viewBinder.etPassword.setError("请输入密码");
            return;
        }
        if (face == null) {
            toast("上传头像");
            return;
        }
        showLoading();
        HashMap<String, Object> params = new HashMap<>();
        if (user != null) {
            params.put("id", user.id);
        }
        params.put("nickname", nickname);
        params.put("username", username);
        params.put("sex", sex[viewBinder.spSex.getSelectedItemPosition()]);
        password= PasswordUtils.encryptPassword(password);
        params.put("password", password);

        params.put("face", face);
        HttpUtil.postModel(user == null ? Api.REGISTER : Api.UPDATE_USER, params, User.class, new HttpUtil.OnNetModelListener<User>() {
            @Override
            public void success(User data, String message) {
                dismissLoading();
                toast(message);
                finish();
                if (isedit==1){
                    startActivity(LoginActivity.class);
                }
            }

            @Override
            public void error(String error) {
                dismissLoading();
                toast(error);
            }
        });


    }


    private String face;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String imagePath = getImagePath(this, data.getData());
            Glide.with(this).load(imagePath).into(viewBinder.ivImage);
            showLoading();
            HttpUtil.upload("common/upload", new File(imagePath), new HttpUtil.OnNetStringListener() {
                @Override
                public void success(String data, String message) {
                    dismissLoading();
                    face = data;
                    toast(message);
                }

                @Override
                public void error(String error) {
                    dismissLoading();
                    toast(error);
                }
            });
        }
    }

    public static String getImagePath(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            File file = new File(context.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            inputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


}