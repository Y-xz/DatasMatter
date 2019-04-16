package com.example.ygz.dm;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.LifeEvents;
import com.example.ygz.dm.javaBeans.User;

import org.angmarch.views.NiceSpinner;

import java.text.DateFormat;
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
import cn.bmob.v3.listener.UpdateListener;
import es.dmoral.toasty.Toasty;

public class EventUpdateActivity extends BaseActivity {

    public static int RESULT_CODE = 1;
    private String EVENTID;
    private int TYPE;
    private int GRADE;
    private String HAPPENDATE;

    private LocalBroadcastManager localBroadcastManager;
    @BindViews({
            R.id.event_update_btn_date
    })List<Button> buttonList;

    @BindViews({
            R.id.event_update_btn_return,
            R.id.event_update_btn_update,

    })List<ImageButton> imageButtonList;

    @BindViews({
            R.id.event_update_et_title,
            R.id.event_update_et_remarks
    })List<EditText>editTextList;

    @BindView(R.id.event_update_ns_type)NiceSpinner types;
//TODO  BUG
    @BindView(R.id.event_update_rg_grade)RadioGroup gradeGroup;
    @BindViews({
            R.id.event_update_rb_0,
            R.id.event_update_rb_1,
            R.id.event_update_rb_2,
            R.id.event_update_rb_3,
            R.id.event_update_rb_4
    })List<RadioButton> radioButtonList;


    private void init() {
        //换成传过来的数据
        EVENTID = getIntent().getStringExtra("eventID");
        TYPE = getIntent().getIntExtra("type",0);
        GRADE = getIntent().getIntExtra("grade",3);
        editTextList.get(0).setText(getIntent().getStringExtra("title"));
        editTextList.get(1).setText(getIntent().getStringExtra("remarks"));
        HAPPENDATE = getIntent().getStringExtra("happenDate");
        buttonList.get(0).setText(HAPPENDATE);
        radioButtonList.get(GRADE).setChecked(true);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_update);

        localBroadcastManager = LocalBroadcastManager.getInstance(this);
        init();
        initTypesSpinner();
        buttonListener();
    }

    private void initTypesSpinner() {
        List<String> eventList = new LinkedList<>(Arrays.asList("默认", "学习", "工作","考试", "纪念", "出行"));
        String temp_i = eventList.get(TYPE);
        /*列表排序，弯道实现了设置NiceSpinner默认值的效果*/
        for (int j = TYPE;j > 0;j--){
            eventList.set(j,eventList.get(j-1));
        }
        eventList.set(0,temp_i);
        types.attachDataSource(eventList);
    }

    private void buttonListener() {
        //TODO 返回按钮
        imageButtonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //TODO EventUpdate日期选择
        buttonList.get(0).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getAction() == MotionEvent.ACTION_DOWN){

                }
                if (event.getAction() == MotionEvent.ACTION_UP){
                    TimePickerView pvTime = new TimePickerBuilder(EventUpdateActivity.this, new OnTimeSelectListener() {
                        @Override
                        public void onTimeSelect(Date date, View v) {
                            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                            String d = df.format(date);
                            buttonList.get(0).setText(d);
                        }
                    })
                            .setDate(stringToCalendar("yyyy-MM-dd",HAPPENDATE))
                            /*.setSubmitColor(Color.parseColor("#000"))*/
                            .build();
                    pvTime.show();
                }
                return false;
            }
        });

        //TODO 提交按钮
        imageButtonList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String title = editTextList.get(0).getText().toString();
                final int type = typePack(types.getText().toString());
                final String date = buttonList.get(0).getText().toString();
                final String remark = editTextList.get(1).getText().toString();
                if (title.equals("")){
                    Toasty.warning(EventUpdateActivity.this,"标题还没写呢~", Toast.LENGTH_LONG).show();
                    return;
                }
                if (type == 000){
                    Toasty.warning(EventUpdateActivity.this,"别忘了选择类型呀~",Toast.LENGTH_LONG).show();
                    return;
                }
                if (date.equals("")){
                    Toasty.warning(EventUpdateActivity.this,"别忘了选择时间呀~",Toast.LENGTH_LONG).show();
                    return;
                }
                LifeEvents lifeEvents = new LifeEvents();
                lifeEvents.setTitle(title);
                lifeEvents.setType(type);
                lifeEvents.setGrade(GRADE);
                lifeEvents.setHappenDate(date);
                lifeEvents.setRemarks(remark);
                lifeEvents.update(EVENTID,new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            Toasty.success(EventUpdateActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent("up");
                            localBroadcastManager.sendBroadcast(intent);
                            /*Intent intent1 = new Intent();*/
                            intent.putExtra("title",title);
                            intent.putExtra("grade",GRADE);
                            intent.putExtra("type",type);
                            intent.putExtra("happenDate",date);
                            intent.putExtra("remarks",remark);
                            setResult(1,intent);
                            finish();
                        }else {
                            Log.e("Bmob","添加事件失败：错误信息："+e.getMessage()+"  错误码："+e.getErrorCode());
                            Toasty.error(EventUpdateActivity.this,"出了点小问题",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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

    private int typePack(String type){
        /*学习，工作，考试，纪念，出行*/
        int i;
        switch (type){
            case "默认":
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
                i = 000;
                break;
        }
        return i;
    }

}
