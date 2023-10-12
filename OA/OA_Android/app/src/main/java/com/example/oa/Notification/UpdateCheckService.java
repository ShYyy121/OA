package com.example.oa.Notification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.oa.App;
import com.example.oa.DB.OaDatabaseHelper;
import com.example.oa.bean.Oa;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class UpdateCheckService extends Service {


    private Timer timer;
    private OaDatabaseHelper oaDatabaseHelper=new OaDatabaseHelper(this);

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopTimer();
        super.onDestroy();
    }

    private void startTimer() {
        timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                // 在这里执行实时检测的逻辑，检查是否有更新内容
               hasUpdate();
            }
        };
        // 每隔一段时间执行一次任务，例如每分钟检测一次
        timer.schedule(timerTask, 0, 60 * 1000);
    }

    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private boolean hasUpdate() {
        if (App.isManager()){
            HashMap<String, Object> params = new HashMap<>();
            params.put("userid", App.user.username);
            HttpUtil.getList(Api.OA_GET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
                @Override
                public void success(List<Oa> data, String message) {
                    // 比对数据库数据和网络请求返回的数据
                    List<Oa> databaseOas=oaDatabaseHelper.getAllApprovals();
                    int res = findDiffOas(databaseOas, data);
                    if (res==1){
                        NotificationHelper.createNotificationChannel(UpdateCheckService.this);
                        NotificationHelper.sendNotification(UpdateCheckService.this, "亲爱的老师", "您有一个新的内容待审批");
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
                    // 比对数据库数据和网络请求返回的数据
                    List<Oa> databaseOas=oaDatabaseHelper.getAllApprovals();
                    List<Oa> Oas=new ArrayList<>();
                    for (Oa databaseOa : databaseOas) {
                        if (databaseOa.teacher.equals(App.user.username)){
                            Oas.add(databaseOa);
                        }
                    }
                    int res = findDiffOas(Oas, data);
                    if (res==1){
                        NotificationHelper.createNotificationChannel(UpdateCheckService.this);
                        NotificationHelper.sendNotification(UpdateCheckService.this, "亲爱的老师", "您有一个新的内容待审批");
                    }
                }
                @Override
                public void error(String message) {

                }
            });
        } else  {
            HashMap<String, Object> params = new HashMap<>();
            params.put("userid", App.user.id);
            HttpUtil.getList(Api.OA_StuGET, params, Oa.class, new HttpUtil.OnNetListListener<Oa>() {
                @Override
                public void success(List<Oa> data, String message) {
                    List<Oa> databaseOas=oaDatabaseHelper.getAllApprovals();
                    List<Oa> Oas=new ArrayList<>();
                    for (Oa databaseOa : databaseOas) {
                        if (databaseOa.userid.equals(App.user.id)){
                            Oas.add(databaseOa);
                        }
                    }
                    int res = findDiffOas(Oas, data);
                    if (res==2){
                        NotificationHelper.createNotificationChannel(UpdateCheckService.this);
                        NotificationHelper.sendNotification(UpdateCheckService.this, "您收到一条消息", "您的审批申请有更新");
                    }
                }

                @Override
                public void error(String message) {

                }
            });


        }


        // 实时检测逻辑，判断是否有更新内容
        // 返回 true 表示有更新，返回 false 表示无更新
        // 根据你的业务逻辑进行判断
        return false;
    }

    private int findDiffOas(List<Oa> databaseOas, List<Oa> networkOas) {
        List<Oa> diffOas = new ArrayList<>();

        // 遍历网络请求返回的数据
        for (Oa networkOa : networkOas) {
            boolean found = false;
            boolean status_modify=found;
            // 遍历数据库中的数据
            for (Oa databaseOa : databaseOas) {
                if (networkOa.getId().equals(databaseOa.getId())) {
                    found = true;
                    break;
                }
                if (!networkOa.status.equals(databaseOa.status)){
                    status_modify=true;
                    break;
                }
            }

            // 如果在数据库中找不到对应的数据，则表示有更新
            if (!found) {
                return 1;//数据新增
            }else {
                if (!status_modify){
                    return 2;//审批状态
                }
            }


        }

        return 0;
    }

    private void sendNotification() {
        // 创建并发送通知的逻辑
        // 使用 NotificationManager 创建通知，并使用 NotificationCompat.Builder 构建通知的内容、样式等
        // 参考前面提供的代码示例来创建和发送通知
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
