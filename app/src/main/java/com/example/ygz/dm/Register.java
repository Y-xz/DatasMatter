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
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class Register extends BaseActivity {
    @BindViews({
            R.id.reg_et_phoneNumber,
            R.id.reg_et_vercode
    })List<EditText> editTextList;

    @BindViews({
            R.id.reg_btn_getcode,
            R.id.reg_btn_next
    })List<Button> buttonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
                    final String phoneNum = getEditText(editTextList.get(0));
                    if (phoneNum.equals("")){
                        toast("请输入手机号");
                        return false;
                    }
                    if (phoneNum.length() != 11){
                        toast("请输入正确的手机号");
                        return false;
                    }
                    BmobQuery<User> queryPhoneNum = new BmobQuery<>();
                    queryPhoneNum.addWhereEqualTo("mobilePhoneNumber",phoneNum);
                    queryPhoneNum.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null){
                                String tem = "";
                                for (User num:list){
                                    tem = num.getMobilePhoneNumber();
                                }
                                if (!tem.equals("")){
                                    toast("您的号码已注册，请尝试找回密码");
                                    return;
                                }
                                //TODO 获取短信验证码
                                BmobSMS.requestSMSCode(phoneNum,"DayCloud", new QueryListener<Integer>() {
                                    @Override
                                    public void done(Integer integer, BmobException e) {
                                        if (e == null){
                                            toast("发送成功,请在十分钟内使用");
                                            return;
                                        }
                                        toast("获取失败");
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
                    final String phoneNum = getEditText(editTextList.get(0));
                    String smsCode = getEditText(editTextList.get(1));
                    //TODO 使用活动收集器销毁
                    //TODO 判断逻辑 抬起直接给出反应
                    BmobSMS.verifySmsCode(phoneNum, smsCode, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                toast("验证成功");
                                Log.i("Bmob","验证码验证成功");
                                //TODO 跳转界面  登录验证成功
                                Bundle bundle = new Bundle();
                                bundle.putString("phoneNum",phoneNum);
                                startActivity(Register2Activity.class,bundle,false);
                            }else {
                                Log.e("Bmob",e.getMessage()+"错误码"+e.getErrorCode());
                                toast("验证失败");
                            }
                        }
                    });
                    /**
                     * 无验证调试
                     * */
           /*         Bundle bundle = new Bundle();
                    bundle.putString("phoneNum",phoneNum);
                    startActivity(Register2Activity.class,bundle,false);*/
                }

                return false;
            }
        });

    }

}
