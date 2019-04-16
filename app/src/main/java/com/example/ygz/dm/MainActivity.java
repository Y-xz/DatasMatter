package com.example.ygz.dm;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ygz.dm.adapter.MainListAdapter;
import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.LifeEvents;
import com.example.ygz.dm.javaBeans.User;
import com.scwang.smartrefresh.header.BezierCircleHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.BindViews;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private IntentFilter intentFilter = new IntentFilter();
    private NotifyAdapter notifyAdapter = new NotifyAdapter();
    private LocalBroadcastManager localBroadcastManager;//本地广播管理员

    private List<LifeEvents> lifeEventsList = new ArrayList<>();
    private List<LifeEvents> baseList = new ArrayList<>();

    private MainListAdapter adapter;

    User userInfo = BmobUser.getCurrentUser(User.class);
    BmobQuery<LifeEvents> query = new BmobQuery<>();

    @BindView(R.id.main_layout_ref)RefreshLayout refreshLayout;
    @BindView(R.id.main_layout_drawer)DrawerLayout drawerLayout;
    @BindView(R.id.main_view_recycler)RecyclerView recyclerView;
    @BindViews({
            R.id.main_left_btn_trash,
            R.id.main_left_btn_my_info,
            R.id.main_left_btn_about,
            R.id.main_left_btn_logout
    })List<Button> buttonList;

    @BindViews({
            R.id.main_ibtn_0,
            R.id.main_ibtn_1,
            R.id.main_ibtn_2,
            R.id.main_ibtn_3,
            R.id.main_ibtn_4,
            R.id.main_ibtn_5,
            R.id.main_btn_menu,
            R.id.main_btn_add,
    })List<ImageButton>imageButtonList;
    @BindView(R.id.main_iv_list_null)ImageView listNull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        localBroadcastManager = LocalBroadcastManager.getInstance(this);//获取本地广播实例

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MainListAdapter(lifeEventsList);
        //TODO 设置为静态
        /*adapter.setHasStableIds(true);*/
        recyclerView.setAdapter(adapter);
        initButtonOnClickListener();
        initLifeEvents();

        intentFilter.addAction("up");
        localBroadcastManager.registerReceiver(notifyAdapter,intentFilter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                /*添加震动*/
                /*修改逻辑*/
                initLifeEvents();
                refreshLayout.finishRefresh();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        localBroadcastManager.unregisterReceiver(notifyAdapter);
    }

    @Override
    public void onClick(View v) {
        int btnID = v.getId();
        switch (btnID){
            case R.id.main_btn_add:
                startActivity(AddEventActivity.class,null,false);
                break;
            case R.id.main_left_btn_logout:
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("确定退出当前账号？");
                dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        BmobUser.logOut();
                        startActivity(LoginActivity.class,null,true);
                    }
                });
                dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.main_btn_menu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.main_left_btn_trash:
                startActivity(TrashActivity.class,null,false);
                break;
            case R.id.main_left_btn_my_info:
                startActivity(MyInfoActivity.class,null,false);
                break;
            case R.id.main_left_btn_about:
                startActivity(AboutActivity.class,null,false);
                break;
            case R.id.main_ibtn_0:
                toast("全部");
                notifyItemUpdate(0);
                break;
            case R.id.main_ibtn_1:
                toast("学习");
                notifyItemUpdate(1);
                break;
            case R.id.main_ibtn_2:
                toast("工作");
                notifyItemUpdate(2);
                break;
            case R.id.main_ibtn_3:
                toast("考试");
                notifyItemUpdate(3);
                break;
            case R.id.main_ibtn_4:
                toast("纪念");
                notifyItemUpdate(4);
                break;
            case R.id.main_ibtn_5:
                toast("出行");
                notifyItemUpdate(5);
                break;
        }
    }

    private void initButtonOnClickListener() {
        buttonList.get(0).setOnClickListener(this);
        buttonList.get(1).setOnClickListener(this);
        buttonList.get(2).setOnClickListener(this);
        buttonList.get(3).setOnClickListener(this);
        imageButtonList.get(0).setOnClickListener(this);
        imageButtonList.get(1).setOnClickListener(this);
        imageButtonList.get(2).setOnClickListener(this);
        imageButtonList.get(3).setOnClickListener(this);
        imageButtonList.get(4).setOnClickListener(this);
        imageButtonList.get(5).setOnClickListener(this);
        imageButtonList.get(6).setOnClickListener(this);
        imageButtonList.get(7).setOnClickListener(this);

    }
    private void initLifeEvents() {
        query.addWhereEqualTo("userID",userInfo.getObjectId());
        query.order("happenDate");
        query.addWhereEqualTo("trash",false);
        query.findObjects(new FindListener<LifeEvents>() {
            @Override
            public void done(List<LifeEvents> list, BmobException e) {
                if (e == null){
                    baseList.clear();
                    lifeEventsList.clear();
                    adapter.notifyDataSetChanged();
                    int sign = 0;
                    for (LifeEvents l:list){
                        baseList.add(sign,l);
                        lifeEventsList.add(sign,l);
                        adapter.notifyItemInserted(sign++);
                        recyclerView.scrollToPosition(0);
                    }
                    if (lifeEventsList.size() == 0){
                        listNull.setVisibility(View.VISIBLE);
                    }else {
                        listNull.setVisibility(View.GONE);
                    }
                    /*int i = 0;
                    for (LifeEvents l:list){
                        i++;
                        if (l.getHappenDate().equals(getCurrDate())){
                            recyclerView.scrollToPosition(i-2);
                            break;
                        }
                    }*/
                }else {

                }
            }
        });
    }


    private void notifyItemUpdate(int t) {

        while (lifeEventsList.size()>0){
            lifeEventsList.remove(0);
            adapter.notifyItemRemoved(0);
        }
        if (t == 0){
            for (int i = 0,sign = 0;i<baseList.size();i++){
                lifeEventsList.add(sign,baseList.get(i));
                adapter.notifyItemInserted(sign++);
                recyclerView.scrollToPosition(0);
            }
            isListNull();
            return;
        }
        for (int i = 0,sign = 0;i<baseList.size();i++){
            if (baseList.get(i).getType() == t){
                lifeEventsList.add(sign,baseList.get(i));
                adapter.notifyItemInserted(sign++);
                recyclerView.scrollToPosition(0);
            }
        }

        isListNull();

    }

    private void isListNull() {
        if (lifeEventsList.size() == 0){
            /*出现列表为空的标志*/
            listNull.setVisibility(View.VISIBLE);
        }else {
            listNull.setVisibility(View.GONE);
        }
    }

    /*自定义广播，通知适配器更新列表*/
    private class NotifyAdapter extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            initLifeEvents();
        }
    }


}
