package com.example.ygz.dm;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.ygz.dm.adapter.MainListAdapter;
import com.example.ygz.dm.adapter.TrashListAdapter;
import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.LifeEvents;
import com.example.ygz.dm.javaBeans.User;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TrashActivity extends BaseActivity implements View.OnClickListener{

    private List<LifeEvents> lifeEventsList = new ArrayList<>();

    private TrashListAdapter adapter;

    User userInfo = BmobUser.getCurrentUser(User.class);
    BmobQuery<LifeEvents> query = new BmobQuery<>();
    @BindView(R.id.trash_layout_ref)RefreshLayout refreshLayout;
    @BindView(R.id.trash_view_recycler)RecyclerView recyclerView;
    @BindView(R.id.trash_iv_list_null)ImageView listNull;
    @BindViews({
            R.id.trash_btn_return,
            R.id.trash_btn_clear
    })List<ImageButton>imageButtonList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        GridLayoutManager layoutManager = new GridLayoutManager(this,1);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new TrashListAdapter(lifeEventsList);
        recyclerView.setAdapter(adapter);
        initButtonOnClickListener();
        initTrashEvents();

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                /*添加震动*/
                /*修改逻辑*/
                initTrashEvents();
                refreshLayout.finishRefresh();
            }
        });
    }

    private void initTrashEvents() {
        query.addWhereEqualTo("userID",userInfo.getObjectId());
        query.order("happenDate");
        query.addWhereEqualTo("trash",true);
        query.findObjects(new FindListener<LifeEvents>() {
            @Override
            public void done(List<LifeEvents> list, BmobException e) {
                if (e == null){
                    lifeEventsList.clear();
                    adapter.notifyDataSetChanged();
                    int sign = 0;
                    for (LifeEvents l:list){
                        lifeEventsList.add(sign,l);
                        adapter.notifyItemInserted(sign++);
                        recyclerView.scrollToPosition(0);
                    }
                    isListNull();
                }else {

                }
            }
        });
    }

    private void initButtonOnClickListener() {
        imageButtonList.get(0).setOnClickListener(this);
        imageButtonList.get(1).setOnClickListener(this);
    }
    private void isListNull() {
        if (lifeEventsList.size() == 0){
            /*出现列表为空的标志*/
            listNull.setVisibility(View.VISIBLE);
        }else {
            listNull.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int btnID = v.getId();
        switch (btnID){
            case R.id.trash_btn_return:
                finish();
                break;
            case R.id.trash_btn_clear:
                finish();
                break;
        }
    }
}
