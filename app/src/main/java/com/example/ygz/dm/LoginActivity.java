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
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends BaseActivity {


    @BindViews({
            R.id.login_btn_login,
            R.id.login_btn_forgetPassword,
            R.id.login_btn_register
    }) List<Button>buttonList;

    @BindViews({
            R.id.login_et_phoneNumber,
            R.id.login_et_password
    }) List<EditText>editTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //TODO 登录按钮
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
                    //登录逻辑
                    String username = getEditText(editTextList.get(0));
                    String pass = getEditText(editTextList.get(1));
                    if (username.isEmpty()){
                        toast("登录需要您的账号哦~");
                        return false;
                    }
                    if (username.length() != 11){
                        toast("手机号码格式不对");
                        return false;
                    }
                    if (pass.isEmpty()){
                        toast("没有密码怎么登录呀~");
                        return false;
                    }

                    final User user = new User();
                    user.setUsername(username);
                    user.setPassword(pass);

                    BmobQuery<User> queryPhoneNum = new BmobQuery<>();
                    queryPhoneNum.addWhereEqualTo("username",username);
                    queryPhoneNum.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null){
                                String tem = "";
                                for (User num:list){
                                    tem = num.getMobilePhoneNumber();
                                }
                                if (tem.equals("")){
                                    toast("您的号码还未注册");
                                    return;
                                }
                                user.login(new SaveListener<User>() {
                                    @Override
                                    public void done(User user, BmobException e) {
                                        if (e == null){
                                            startActivity(MainActivity.class,null,true);
                                            Log.i("Bmob","登录成功");
                                        }else {
                                            if (e.getErrorCode() == 101){
                                                toast("密码与账号不匹配");
                                            }
                                            Log.e("Bmob","登录失败"+"错误信息：+"+e.getMessage()+"  错误代码："+e.getErrorCode());
                                        }
                                    }
                                });

                            }else {
                                Log.e("Bmob",e.getMessage()+"错误代码"+e.getErrorCode());
                                toast("ERROR"+e.getErrorCode());
                            }
                        }
                    });
                }

                return false;
            }
        });
        //TODO 忘记密码按钮
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
                    startActivity(ForgetPasswordActivity.class,null,false);

                }

                return false;
            }
        });
        //TODO 注册按钮
        buttonList.get(2).setOnTouchListener(new View.OnTouchListener() {
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
                    startActivity(Register.class,null,false);

                }

                return false;
            }
        });

    }

}
