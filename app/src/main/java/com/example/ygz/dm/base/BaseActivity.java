package com.example.ygz.dm.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import butterknife.ButterKnife;

public class BaseActivity extends AppCompatActivity {



    private IntentFilter filter = new IntentFilter();
    private NetworkChangeReceiver networkCR = new NetworkChangeReceiver();
    Boolean notWorkState = true;
    private Toast toast;
    public static ActivityCollector activityCollector = new ActivityCollector();

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCollector.addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//禁止横屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        /*supportRequestWindowFeature(Window.FEATURE_NO_TITLE);*///无标题栏

        /*监听网络开关*/
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(networkCR,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.bind(this).unbind();
        activityCollector.removeActivity(this);
        unregisterReceiver(networkCR);
    }
    /**
     * 获取当前日期
     * */
    public String getCurrDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String currDate = df.format(new Date());
        return currDate;
    }

    /**
     * 活动跳转
     * */
    public void startActivity(Class<? extends Activity> target,Bundle bundle,boolean finish){
        Intent intent = new Intent();
        intent.setClass(this,target);
        if (bundle != null){
            intent.putExtra(getPackageName(),bundle);
        }
        startActivity(intent);
        if (finish){
            finish();
        }
    }
    public Bundle getBundle(){
        if (getIntent() != null && getIntent().hasExtra(getPackageName())){
            return getIntent().getBundleExtra(getPackageName());
        }else {
            return null;
        }
    }

    /**
     * 弹出 Toast
     * */
    protected void runOnMain(Runnable runnable){
        runOnUiThread(runnable);
    }
    public void toast(final Object object){
        try {
            runOnMain(new Runnable() {
                @Override
                public void run() {
                    if (toast == null){
                        toast = Toast.makeText(BaseActivity.this,"",Toast.LENGTH_SHORT);
                    }
                    toast.setText(object.toString());
                    toast.show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * BaseActivity__获取EditText的值
     * */
    public String getEditText(EditText et){
        String str = et.getText().toString().trim();
        return str;
    }
    /**
     * BaseActivity__[字符串型日期]转[日历型]
     * */
    public Calendar stringToCalendar(String pattern,String date){
        DateFormat dateFormat = new SimpleDateFormat(pattern);
        Date d = null;
        Calendar c = Calendar.getInstance();
        try {
            d = dateFormat.parse(date);
            c.setTime(d);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
    }
    /**
     * BaseActivity__[字符串型日期]转[星期几]
     * */
    public static String stringToWeekday(String pattern,String date) {//必须yyyy-MM-dd
        SimpleDateFormat sd = new SimpleDateFormat(pattern);
        SimpleDateFormat sdw = new SimpleDateFormat("EEEE");
        Date d = null;
        try {
            d = sd.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdw.format(d);
    }
    /**
     * 自定义网络变化监听
     * */
    public class NetworkChangeReceiver extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent){
            ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isAvailable()){
                notWorkState = true;
                /*setVisibility(View.GONE);*/
                 /*Toast.makeText(context,"您打开了网络开关",Toast.LENGTH_SHORT).show();*/
            }else {
                notWorkState = false;
                Toast.makeText(context,"网络连接断开",Toast.LENGTH_SHORT).show();
                /*setVisibility(View.VISIBLE);*/
            }
        }
    }
}
