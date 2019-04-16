package com.example.ygz.dm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ygz.dm.base.BaseActivity;

import java.util.List;

import butterknife.BindViews;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class ForgetPasswordActivity extends BaseActivity {

    @BindViews({
            R.id.forpass_et_phoneNumber,
            R.id.forpass_et_vercode
    })List<EditText>editTextList;

    @BindViews({
            R.id.forpass_btn_getcode,
            R.id.forpass_btn_next
    })List<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //TODO 获取验证码按钮
        buttonList.get(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /*按下操作*/
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    /**
                     * 背景
                     * 设置字体颜色
                     * 等
                     * */
                }

                /*抬起操作*/
                if (event.getAction() == MotionEvent.ACTION_UP){
                    //TODO 使用活动收集器销毁
                    BmobSMS.requestSMSCode("","", new QueryListener<Integer>() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null){
                                Toast.makeText(ForgetPasswordActivity.this,"发送成功",Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }

                return false;
            }
        });

        //TODO 下一步按钮
        buttonList.get(1).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                /*按下操作*/
                if (event.getAction() == MotionEvent.ACTION_DOWN){

                    /**
                     * 背景
                     * 设置字体颜色
                     * 等
                     * */
                }

                /*抬起操作*/
                if (event.getAction() == MotionEvent.ACTION_UP){
                    //TODO 使用活动收集器销毁
                    startActivity(ForgetRePasswordActivity.class,null,false);
                }

                return false;
            }
        });

    }
}
