package com.example.ygz.dm;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ygz.dm.base.BaseActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindViews;

public class EventDetailsActivity extends BaseActivity {
    private String EVENTID;
    private int TYPE;
    private int GRADE;
    private String HAPPENDATE;
    public static int REQUEST_CODE = 1;

    @BindViews({
            R.id.event_details_tv_title,
            R.id.event_details_tv_date,
            R.id.event_details_tv_remarks
    })List<TextView> textViewList;
    @BindViews({
            R.id.event_details_btn_return,
            R.id.event_details_btn_update
    })List<ImageButton> imageButtonList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);
        init();
        buttonListener();

    }

    private void buttonListener() {
        //TODO 返回
        imageButtonList.get(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //TODO 修改
        imageButtonList.get(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventDetailsActivity.this,EventUpdateActivity.class);
                intent.putExtra("eventID",EVENTID);
                intent.putExtra("title",textViewList.get(0).getText().toString());
                intent.putExtra("type",TYPE);
                intent.putExtra("grade",GRADE);
                intent.putExtra("happenDate",HAPPENDATE);
                intent.putExtra("remarks",textViewList.get(2).getText());
                startActivityForResult(intent,REQUEST_CODE);
            }
        });
    }

    private void init() {
        Bundle bundle = getIntent().getExtras();
        EVENTID = bundle.getString("eventID");
        String title = bundle.getString("title");
        TYPE = bundle.getInt("type");
        GRADE = bundle.getInt("grade");
        String countDate = bundle.getString("countDown");
        String remarks = bundle.getString("remarks");
        HAPPENDATE = bundle.getString("happenDate");

        textViewList.get(0).setText(title);
        textViewList.get(1).setText(countDate);
        textViewList.get(2).setText(remarks);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == EventUpdateActivity.RESULT_CODE){
            textViewList.get(0).setText(data.getStringExtra("title"));
            textViewList.get(2).setText(data.getStringExtra("remarks"));
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String countDate = null;
            String happenDate = data.getStringExtra("happenDate");
            try {
                Date dateCurr = dateFormat.parse(getCurrDate());
                Date Date = dateFormat.parse(happenDate);
                countDate = ""+((Date.getTime() - dateCurr.getTime()) / (60 * 60 * 1000 * 24));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            textViewList.get(1).setText(countDate);
        }
    }
}
