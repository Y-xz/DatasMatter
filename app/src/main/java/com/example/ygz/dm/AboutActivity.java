package com.example.ygz.dm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.ygz.dm.base.BaseActivity;

import java.util.List;

import butterknife.BindViews;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    @BindViews({
            R.id.about_btn_return
    })List<ImageButton> imageButtonList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initButtonOnClickListenner();
    }

    @Override
    public void onClick(View v) {
        int btnID = v.getId();
        switch (btnID){
            case R.id.about_btn_return:
                finish();
                break;
        }

    }
    private void initButtonOnClickListenner() {
        imageButtonList.get(0).setOnClickListener(this);
    }
}
