package com.example.oa;

import android.app.Application;

import com.baidu.location.LocationClient;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.example.oa.bean.User;
import com.example.oa.util.PersistentCookieJar;
import com.example.oa.util.PreferenceUtil;


public class App extends Application {
    static App context;
    static int theme=0;
    public static User user;


    @Override
    public void onCreate() {
        super.onCreate();
        context = this;


        //        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
//        SDKInitializer.initialize(this);
//        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
//        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
//        SDKInitializer.setCoordType(CoordType.BD09LL);
        SDKInitializer.setAgreePrivacy(getApplicationContext(),true);
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.BD09LL);
        LocationClient.setAgreePrivacy(true);

        boolean first = PreferenceUtil.getInstance().get("first", true);
        if (first) {
            PreferenceUtil.getInstance().save("first", false);
        }
        user = PreferenceUtil.getInstance().get("logger", User.class);
    }

    public static App getContext() {
        return context;
    }


    public static boolean isLogin() {
        return user != null;
    }

    public static boolean isManager() {
        if (!isLogin()) {
            return false;
        }
        return user.username.equals("admin");
    }

    public static boolean isTeacher(){
        if (!isLogin()) {
            return false;
        }
        return user.username.startsWith("teacher");
    }

    public static void login(User user_) {
        user = user_;
        PreferenceUtil.getInstance().save("logger", user_);
    }

    public static void logout() {
        user = null;
        PreferenceUtil.getInstance().remove("logger");
    }
}
