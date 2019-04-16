package com.example.ygz.dm;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.User;

import java.util.List;

import butterknife.BindViews;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Register2Activity extends BaseActivity {
    @BindViews({
            R.id.reg2_et_nickname,
            R.id.reg2_et_password,
            R.id.reg2_et_password_con
    })List<EditText> editTextList;
    @BindViews({
            R.id.reg2_btn_reg
    })List<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);


        //TODO 注册按钮
        buttonList.get(0).setOnTouchListener(new View.OnTouchListener() {
            @Override



            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){

                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    String phoneNum = getBundle().getString("phoneNum");
                    String nickname = getEditText(editTextList.get(0));
                    String pass = getEditText(editTextList.get(1));
                    String passCon = getEditText(editTextList.get(2));
                    if (nickname.isEmpty()){
                        toast("取一个用户名呀");
                        return false;
                    }
                    if (pass.isEmpty()){
                        toast("还没有输入密码哦~");
                        return false;
                    }
                    if (passCon.isEmpty()){
                        toast("确认密码也得输呀~");
                        return false;
                    }
                    if (!(pass.equals(passCon))){
                        toast("两次密码输入不一致");
                        return false;
                    }
                    User user = new User();
                    user.setNickname(nickname);
                    user.setMobilePhoneNumber(phoneNum);
                    user.setUsername(phoneNum);
                    user.setPassword(pass);
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (e == null){
                                toast("注册成功");
                                startActivity(MainActivity.class,null,false);
                                activityCollector.finishAllActivity();
                            }else {
                                Log.e("Bmob","注册失败"+"错误信息："+e.getMessage()+"错误码："+e.getErrorCode());
                                if (e.getErrorCode() == 202){
                                    toast("您的用户名已经有人注册了");
                                    return;
                                }
                                toast("注册失败");
                            }
                        }
                    });

                }

                return false;
            }
        });



    }
}
