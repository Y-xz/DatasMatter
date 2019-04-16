package com.example.ygz.dm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.User;

import java.util.List;

import butterknife.BindViews;
import cn.bmob.v3.BmobUser;
import studio.carbonylgroup.textfieldboxes.ExtendedEditText;

public class MyInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindViews({
            R.id.my_info_btn_return,
            R.id.my_info_imgbtn_username,
            R.id.my_info_imgbtn_phonenumber,
            R.id.my_info_imgbtn_email
    })List<ImageButton> imageButtonList;

    @BindViews({
            R.id.my_info_et_nickname,
            R.id.my_info_et_phonenumber,
            R.id.my_info_et_email
    })List<ExtendedEditText>extendedEditTextList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        initButtonOnClickListenner();
        initMyInfo();
    }

    private void initMyInfo() {
        User myUser = BmobUser.getCurrentUser(User.class);
        extendedEditTextList.get(0).setText(myUser.getNickname());
        extendedEditTextList.get(1).setText(myUser.getMobilePhoneNumber());
        if (myUser.getEmail() == null){
            extendedEditTextList.get(2).setText("还未绑定邮箱");
            return;
        }
        extendedEditTextList.get(2).setText(myUser.getEmail());
    }


    @Override
    public void onClick(View v) {
        int btnID = v.getId();
        switch (btnID){
            case R.id.my_info_btn_return:
                finish();
                break;
            case R.id.my_info_imgbtn_username:

                break;
            case R.id.my_info_imgbtn_phonenumber:

                break;
            case R.id.my_info_imgbtn_email:

                break;
        }
    }
    private void initButtonOnClickListenner() {
        imageButtonList.get(0).setOnClickListener(this);
        imageButtonList.get(1).setOnClickListener(this);
        imageButtonList.get(2).setOnClickListener(this);
        imageButtonList.get(3).setOnClickListener(this);

    }
}
