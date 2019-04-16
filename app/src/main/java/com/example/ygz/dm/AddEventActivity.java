package com.example.ygz.dm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;


import com.bigkoo.pickerview.builder.TimePickerBuilder;

import com.bigkoo.pickerview.listener.OnTimeSelectListener;

import com.bigkoo.pickerview.view.TimePickerView;
import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.LifeEvents;
import com.example.ygz.dm.javaBeans.User;


import org.angmarch.views.NiceSpinner;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import es.dmoral.toasty.Toasty;
/**
 * 时间再加上权重，比如：特别重要，重要，一般……
 * */
public class AddEventActivity extends BaseActivity {

    private final int NO_TYPE = 9999;
    private LocalBroadcastManager localBroadcastManager;
    private int GRADE = 3;

    @BindViews({
            R.id.add_event_btn_date,
    })List<Button>buttonList;
    @BindViews({
            R.id.add_event_btn_upload,
            R.id.add_event_btn_return
    })List<ImageButton> imageButtonList;

    @BindViews({
            R.id.add_event_et_title,
            R.id.add_event_et_remarks
    })List<EditText>editTextList;

    @BindView(R.id.add_event_ns_type)NiceSpinner types;
    @BindView(R.id.add_event_rg_grade)RadioGroup gradeGroup;
    @BindViews({
            R.id.add_event_rb_0,
            R.id.add_event_rb_1,
            R.id.add_event_rb_2,
            R.id.add_event_rb_3,
            R.id.add_event_rb_4
    })List<RadioButton> radioButtonList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);

        init();
        /*学习，工作，考试，纪念，出行*/
        List<String> eventList = new LinkedList<>(Arrays.asList("普通", "学习", "工作","考试", "纪念", "出行"));
        types.attachDataSource(eventList);
        buttonListener();

    }

    private void buttonListener() {
        //TODO AddEvent日期选择
        buttonList.get(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){

                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                        TimePickerView pvTime = new TimePickerBuilder(AddEventActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String d = df.format(date);
                            buttonList.get(0).setText(d);
                        }
                    }).build();
                    pvTime.show();
                }
                return false;
            }
        });
        //TODO 提交按钮
        imageButtonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextList.get(0).getText().toString();
                int type = typePack(types.getText().toString());
                String date = buttonList.get(0).getText().toString();
                String remark = editTextList.get(1).getText().toString();
                if (title.equals("")){
                    Toasty.warning(AddEventActivity.this,"标题还没写呢~",Toast.LENGTH_LONG).show();
                    return;
                }
                if (type == NO_TYPE){
                    Toasty.warning(AddEventActivity.this,"别忘了选择类型呀~",Toast.LENGTH_LONG).show();
                    return;
                }
                if (date.equals("")){
                    Toasty.warning(AddEventActivity.this,"别忘了选择时间呀~",Toast.LENGTH_LONG).show();
                    return;
                }
                User userInfo = BmobUser.getCurrentUser(User.class);
                LifeEvents lifeEvents = new LifeEvents();
                lifeEvents.setUserID(userInfo.getObjectId());
                lifeEvents.setTitle(title);
                lifeEvents.setType(type);
                lifeEvents.setGrade(GRADE);
                lifeEvents.setHappenDate(date);
                lifeEvents.setRemarks(remark);
                lifeEvents.setTrash(false);
                lifeEvents.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null){
                            Toasty.success(AddEventActivity.this,"提交成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("up");
                            localBroadcastManager.sendBroadcast(intent);
                            finish();
                        }else {
                            Log.e("Bmob","添加事件失败：错误信息："+e.getMessage()+"  错误码："+e.getErrorCode());
                            Toasty.error(AddEventActivity.this,"出了点小问题",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        imageButtonList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MainActivity.class,null,true);
            }
        });

/*重要等级按钮组监听*/
        gradeGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButtonList.get(0).getId()){
                    GRADE = 0;
                    return;
                }
                if (checkedId == radioButtonList.get(1).getId()){
                    GRADE = 1;
                    return;
                }
                if (checkedId == radioButtonList.get(2).getId()){
                    GRADE = 2;
                    return;
                }
                if (checkedId == radioButtonList.get(3).getId()){
                    GRADE = 3;
                    return;
                }
                if (checkedId == radioButtonList.get(4).getId()){
                    GRADE = 4;
                    return;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void init() {
        buttonList.get(0).setText(getCurrDate());
    }


    private int typePack(String type){
        /*学习，工作，考试，纪念，出行*/
        int i;
        switch (type){
            case "普通":
                i = 0;
                break;
            case "学习":
                i = 1;
                break;
            case "工作":
                i = 2;
                break;
            case "考试":
                i = 3;
                break;
            case "纪念":
                i = 4;
                break;
            case "出行":
                i = 5;
                break;
            default:
                i = NO_TYPE;
                break;
        }
        return i;
    }
}
