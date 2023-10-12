package com.example.oa;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oa.DB.DBHelper;
import com.example.oa.base.BaseBindingActivity;
import com.example.oa.base.BindAdapter;
import com.example.oa.bean.Account;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.User;
import com.example.oa.databinding.ActivitySwitchAccountBinding;
import com.example.oa.databinding.ItemGetLessonBinding;
import com.example.oa.databinding.ItemSwitchBinding;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SwitchAccountActivity extends BaseBindingActivity<ActivitySwitchAccountBinding> {

    private DBHelper dbHelper=new DBHelper(this);
    private SQLiteDatabase db;

    private BindAdapter<ItemSwitchBinding, Account> adapter = new BindAdapter<ItemSwitchBinding, Account>() {
        @Override
        public ItemSwitchBinding createHolder(ViewGroup parent) {
            return ItemSwitchBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemSwitchBinding itemSwitchBinding, Account account, int position) {
            Log.d("accdb",account.getUsername());
            itemSwitchBinding.accountId.setText(account.getId());
            itemSwitchBinding.accountName.setText(account.getUsername());
            if (App.user.username.equals(account.getUsername())){
                itemSwitchBinding.accountStatus.setVisibility(View.VISIBLE);
            }
            itemSwitchBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (App.user.username.equals(account.getUsername())){
                        Toast.makeText(SwitchAccountActivity.this, "您当前已经是该帐号了", Toast.LENGTH_SHORT).show();
                    }else{
                        HashMap<String,Object> params=new HashMap<>();
                        params.put("username",account.getUsername());
                        params.put("password",account.getPassword());
                        HttpUtil.getModel(Api.LOGIN, params, User.class, new HttpUtil.OnNetModelListener<User>() {
                            @Override
                            public void success(User data, String message) {
                                display$Toast(message);
                                App.login(data);
                                startActivity(MainActivity.class,intent -> intent.putExtra("username",data.username));
                                finish();
                            }

                            @Override
                            public void error(String message) {
                                display$Toast(message);
                                startActivity(LoginActivity.class);
                                finish();
                            }
                        });
                    }
                }
            });
        }
    };


    @Override
    protected void initListener() {

    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    protected void initData() {
        db= dbHelper.getReadableDatabase();
        viewBinder.accountRecyclerView.setAdapter(adapter);
//        getAccountList();
        List<Account> accounts =dbHelper.getAllUserData();
                for (Account account : accounts) {
            Log.d("accdb",account.getId());
            Log.d("accdb",account.getUsername());
        }
        adapter.getData().clear();
        boolean b = adapter.getData().addAll(accounts);
        Log.d("accdb", String.valueOf(b));
        adapter.notifyDataSetChanged();
    }

//    private void getAccountList() {
//        // 返回之前登录过的账号列表数据（示例数据）
//
//        for (Account account : accounts) {
//            Log.d("accdb",account.getId());
//            Log.d("accdb",account.getUsername());
//        }
////        Log.d("accdb", String.valueOf(accounts));
//        adapter.notifyDataSetChanged();
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        dbHelper.close();
    }

    //    private void switchAccount(Account account) {
//        // 执行账号切换操作
//        // 这里只是演示，实际应根据账号信息执行相应的切换逻辑
//
//        // 设置新的账号信息
//        App.setCurrentAccount(account);
//
//        // 显示切换成功消息
//        Toast.makeText(this, "Switched to " + account.getName(), Toast.LENGTH_SHORT).show();
//
//        // 返回到主界面
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
//    }
}
