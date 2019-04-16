package com.example.ygz.dm;

import android.os.Handler;
import android.os.Looper;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.User;

import butterknife.BindView;
import cn.bmob.v3.BmobUser;

/**
 * 启动页
 * Created 2018‎年‎9‎月‎13‎日 9:46:40 on Ygz
 * */
public class StartActivity extends BaseActivity {

    @BindView(R.id.start_img)ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        imageView.setImageResource(R.drawable.start);

        final User user = BmobUser.getCurrentUser(User.class);
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (user != null){
                    startActivity(MainActivity.class,null,true);
                    finish();
                }else{
                    startActivity(LoginActivity.class,null,true);
                    finish();
                }
            }
        },3000);
    }

}
