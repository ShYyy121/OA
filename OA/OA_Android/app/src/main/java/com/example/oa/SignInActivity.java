package com.example.oa;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.SpatialRelationUtil;
import com.example.oa.bean.Lesson;
import com.example.oa.bean.Signin;
import com.example.oa.util.Api;
import com.example.oa.util.HttpUtil;
import com.example.oa.util.ToastUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;
    private LocationClient mLocationClient = null;
    private TextView mtextView;
    private FloatingActionButton qiandao_btn;
    // 是否是第一次定位
    private boolean isFirstLocate = true;
    boolean isin;
    // 当前定位模式
    private MyLocationConfiguration.LocationMode locationMode;
    private ProgressDialog dialog;
    List<Lesson> lessons=new ArrayList<>();
    private int lessonNumber=-1;
    private String[]  items = new String[]{"第一节（8：30-10：20）", "第二节（10：40-12：30）", "第三节（14：00-15：50）", "第四节（16：10-18：00）", "第五节（18：30-20：20）", "第六节（20：40-22：20）"};
    private String weekDay;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sign_in);
        dialog=new ProgressDialog(this);;
        //获取地图控件引用
        mMapView = findViewById(R.id.bmapView);
        mtextView=findViewById(R.id.qiandaotv);
        qiandao_btn=findViewById(R.id.qiandao_button);
        qiandao_btn.setOnClickListener(this);
        //获取文本显示控件
        // 得到地图
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        try {
            mLocationClient = new LocationClient(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mBaiduMap.setMyLocationEnabled(true);
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f);
        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        mLocationClient.setAgreePrivacy(true);
        //定位初始化
        try {
            mLocationClient = new LocationClient(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //通过LocationClientOption设置LocationClient相关参数
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        // 可选，设置地址信息
        option.setIsNeedAddress(true);
        //可选，设置是否需要地址描述
        option.setIsNeedLocationDescribe(true);

        //设置locationClientOption
        mLocationClient.setLocOption(option);

        //注册LocationListener监听器
        MyLocationListener myLocationListener = new MyLocationListener();
        mLocationClient.registerLocationListener(myLocationListener);
        //开启地图定位图层
        mLocationClient.start();
    }
    protected void showLoading() {
        dialog.show();
    }
    protected void toast(String msg) {
        ToastUtils.showToast(this, msg);
    }
    protected void dismissLoading() {
        dialog.dismiss();
    }
    protected void display$Toast(String msg) {
        ToastUtils.showToast(this, msg);
        dialog.dismiss();
    }

    private void performSignIn() {
        Calendar calendar = Calendar.getInstance();

        // 获取当前时间
        Calendar currentTime = Calendar.getInstance();
        int currentHour = currentTime.get(Calendar.HOUR_OF_DAY);  // 当前小时
        int currentMinute = currentTime.get(Calendar.MINUTE);     // 当前分钟
        int currentDayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);

        Log.d("day_minute", String.valueOf(currentHour)+String.valueOf(currentMinute));
        // 将数字转换为相应的星期几字符串
        switch (currentDayOfWeek) {
            case Calendar.SUNDAY:
                weekDay = "星期日";
                break;
            case Calendar.MONDAY:
                weekDay = "星期一";
                break;
            case Calendar.TUESDAY:
                weekDay = "星期二";
                break;
            case Calendar.WEDNESDAY:
                weekDay = "星期三";
                break;
            case Calendar.THURSDAY:
                weekDay = "星期四";
                break;
            case Calendar.FRIDAY:
                weekDay = "星期五";
                break;
            case Calendar.SATURDAY:
                weekDay = "星期六";
                break;
        }
        // 比较当前时间与给定的时间段
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            Log.d("item",item);
            String[] timeRange = getTimeRange(item);

            Log.d("timerange","--");
            if (timeRange != null && timeRange.length == 4) {
                int startHour = getHourFromTime(timeRange[0]);
                int startMinute = getMinuteFromTime(timeRange[1]);
                int endHour = getHourFromTime(timeRange[2]);
                int endMinute = getMinuteFromTime(timeRange[3]);
                Log.d("starthour", String.valueOf(startHour));
                Log.d("endhour", String.valueOf(endHour));

                if (isTimeWithinRange(currentHour, currentMinute, startHour, startMinute, endHour, endMinute)) {
                    lessonNumber = i + 1;
                    System.out.println("当前时间位于第 " + lessonNumber + " 节课的时间范围内");
                    // 返回 lessonNumber（1-6）
                    break;
                }
            }
        }

        // 给定的时间段
//        showLoading();
        HashMap<String, Object> params1 = new HashMap<>();
        String item=items[lessonNumber-1];
        params1.put("lessontime",item);
        params1.put("dayOfweek",weekDay);
        params1.put("userid",App.user.id);
        HttpUtil.getList(Api.LESSON_GETBYTIME, params1, Lesson.class, new HttpUtil.OnNetListListener<Lesson>() {
            @Override
            public void success(List<Lesson> data, String message) {
                dismissLoading();
                lessons = data;
//                toast(message);
                // 执行签到操作
                 if (data.size()==0){
                            toast("当前没有需要签到的课程");
                            return ;
                        }
                doSignIn();

            }

            @Override
            public void error(String error) {
                toast(error);
                dismissLoading();
            }
        });
    }
    private void doSignIn() {
        final boolean[] res = {false};
        if (lessons != null) {
            Calendar calendar = Calendar.getInstance();

            // 获取当前时间
            Calendar currentTime = Calendar.getInstance();



            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);
            int second = calendar.get(Calendar.SECOND);
            showLoading();
            String time = String.format("%02d:%02d:%02d", hour, minute, second);
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1; // 注意月份是从0开始的，所以要加1
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            String date = String.format("%04d-%02d-%02d", year, month, day);
            for (Lesson lesson : lessons) {
                HashMap<String, Object> params = new HashMap<>();
                params.put("student", App.user.username);
                params.put("teacher", lesson.teacher);
                params.put("time",time);
                params.put("studentid",App.user.id);
                params.put("issigned","true");
                params.put("lessonid",lesson.id);
                params.put("date",date);
                HttpUtil.post(Api.Sign_ADD, params,new HttpUtil.OnNetStringListener() {

                    @Override
                    public void success(String data, String message) {
                        display$Toast(message);
                        res[0] =true;

                    }

                    @Override
                    public void error(String message) {
                        display$Toast(message);
                    }
                });
            }
//            if (res[0]){
//                Toast.makeText(this, "签到成功", Toast.LENGTH_SHORT).show();
//            }

        } else {
            Toast.makeText(this, "获取课程信息失败，请重试", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onClick(View v) {
        if (isin) {
            // 执行获取课程列表操作
            performSignIn();
        } else {
            Toast.makeText(this, "您不在签到范围内请重新签到", Toast.LENGTH_SHORT).show();
        }
    }

    // 从时间字符串中提取小时
    private static int getHourFromTime(String time) {
//        String[] parts = time.split(":");
//        if (parts.length == 1) {
            try {
                return Integer.parseInt(time);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
//        }
//        return -1;
    }

    // 从时间字符串中提取分钟
    private static int getMinuteFromTime(String time) {
//        String[] parts = time.split(":");
//        if (parts.length == 1) {
            try {
                return Integer.parseInt(time);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
//        }
//        return -1;
    }

    // 判断当前时间是否在给定的时间范围内
    private static boolean isTimeWithinRange(int currentHour, int currentMinute, int startHour, int startMinute, int endHour, int endMinute) {
        if (currentHour > startHour && currentHour < endHour) {
            return true;
        } else if (currentHour == startHour && currentMinute >= startMinute) {
            return true;
        } else return currentHour == endHour && currentMinute <= endMinute;
    }


    // 从时间范围字符串中提取开始时间和结束时间
    private static String[] getTimeRange(String item) {
        // 时间范围字符串格式为：第X节（开始时间-结束时间）
        int startIndex = item.indexOf("（") + 1;
        int endIndex = item.indexOf("）");
        if (startIndex > 0 && endIndex > startIndex) {
            String timeRange = item.substring(startIndex, endIndex);
            return timeRange.split("[-：]"); // 使用"-"或"："分割开始时间和结束时间
        }
        return null;
    }



    // 继承抽象类BDAbstractListener并重写其onReceieveLocation方法来获取定位数据，并将其传给MapView
    public class MyLocationListener extends BDAbstractLocationListener {
        @SuppressLint("SetTextI18n")
        @Override
        public void onReceiveLocation(BDLocation location) {
            //mapView 销毁后不在处理新接收的位置
            if (location == null || mMapView == null){
                return;
            }

            // 如果是第一次定位
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            if (isFirstLocate) {
                isFirstLocate = false;
                //给地图设置状态
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLng(ll));
            }

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection()).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);

            LatLng mine=new LatLng(location.getLatitude(),location.getLongitude());
            LatLng center=new LatLng(41.653109,123.424257);
            int radius=1000;
            isin= SpatialRelationUtil.isCircleContainsPoint(center,radius,mine);

            // ------------------  以下是可选部分 ------------------
            // 自定义地图样式，可选
            // 更换定位图标，这里的图片是放在 drawble 文件下的
            BitmapDescriptor mCurrentMarker = BitmapDescriptorFactory.fromResource(R.drawable.icon_geo);
            // 定位模式 地图SDK支持三种定位模式：NORMAL（普通态）, FOLLOWING（跟随态）, COMPASS（罗盘态）
            locationMode = MyLocationConfiguration.LocationMode.FOLLOWING;
            // 定位模式、是否开启方向、设置自定义定位图标、精度圈填充颜色以及精度圈边框颜色5个属性（此处只设置了前三个）。
            MyLocationConfiguration mLocationConfiguration = new MyLocationConfiguration(locationMode,true,mCurrentMarker);
            // 使自定义的配置生效
//            mBaiduMap.setMyLocationConfiguration(mLocationConfiguration);
            // ------------------  可选部分结束 ------------------

            // 显示当前信息
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append( location.getAddrStr());
            mtextView.setText("您目前所在位置： "+stringBuilder);
        }
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }
}