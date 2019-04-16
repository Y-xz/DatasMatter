package com.example.ygz.dm.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ygz.dm.EventDetailsActivity;
import com.example.ygz.dm.R;
import com.example.ygz.dm.Register2Activity;
import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.LifeEvents;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.grantland.widget.AutofitTextView;

/**
 * @author:Ygz
 * @date:2018/10/31 time:18:15
 * @project:MainListAdapter
 * @remark:列表的适配器
 */
    public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder>  {

    private Context mContext;

    private List<LifeEvents> mLifeEvensList;

    private Long countDown;

    private Vibrator vibrator;
    /*构造方法*/
    public MainListAdapter(List<LifeEvents> lifeEventsList){
        mLifeEvensList = lifeEventsList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        TextView countdown,happenDate,weekday;
        AutofitTextView title;
        ImageButton delete,cancel;
        ImageView type,grade;
        //TODO 定义控件

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //TODO 实例化控件
            cardView = (CardView)itemView;
            title = (AutofitTextView) itemView.findViewById(R.id.item_main_tv_title);
            countdown = (TextView)itemView.findViewById(R.id.item_main_tv_countdown);
            happenDate = (TextView)itemView.findViewById(R.id.item_main_tv_happendate);
            weekday = (TextView)itemView.findViewById(R.id.item_main_tv_weekday);
            delete = (ImageButton)itemView.findViewById(R.id.item_main_imgbtn_delete);
            cancel = (ImageButton)itemView.findViewById(R.id.item_main_imgbtn_cancel);
            type = (ImageView)itemView.findViewById(R.id.item_main_imgv_type);
            grade= (ImageView)itemView.findViewById(R.id.item_main_imgv_grade);
        }
    }


    @NonNull
    @Override
    public MainListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_main,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        vibrator = (Vibrator)mContext.getSystemService(Service.VIBRATOR_SERVICE);//震动



        /**
         * 事件监听
         * */
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                LifeEvents lifeEvents = mLifeEvensList.get(position);

                Bundle bundle = new Bundle();
                bundle.putString("eventID",lifeEvents.getObjectId());
                bundle.putString("title",lifeEvents.getTitle());
                bundle.putInt("type",lifeEvents.getType());
                bundle.putInt("grade",lifeEvents.getGrade());
                bundle.putString("countDown",holder.countdown.getText().toString());
                bundle.putString("remarks",lifeEvents.getRemarks());
                bundle.putString("happenDate",lifeEvents.getHappenDate());
                Intent intent = new Intent(mContext, EventDetailsActivity.class);
                intent.putExtras(bundle);
                mContext.startActivity(intent);

            }
        });

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.vibrate(50);
                holder.countdown.setVisibility(View.GONE);
                holder.delete.setVisibility(View.VISIBLE);
                holder.cancel.setVisibility(View.VISIBLE);

                return true;
            }
        });
        holder.delete.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                vibrator.vibrate(20);
                holder.delete.setVisibility(View.GONE);
                holder.countdown.setVisibility(View.VISIBLE);
                return true;
            }
        });
        /**
         * 放入回收站
         * */
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LifeEvents lifeEvents = mLifeEvensList.get(holder.getAdapterPosition());
                lifeEvents.setTrash(true);
                lifeEvents.update(lifeEvents.getObjectId(),new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            mLifeEvensList.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }else {
                            Log.e("Bmob","事件放置回收站失败：位置：MainListAdapter 错误信息："+e.getMessage()+"  错误码："+e.getErrorCode());
                        }
                    }
                });

            }
        });
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.delete.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
                holder.countdown.setVisibility(View.VISIBLE);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MainListAdapter.ViewHolder holder, int position) {

        LifeEvents lifeEvents = mLifeEvensList.get(position);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date dateCurr = dateFormat.parse(getCurrDate());
            Date happenDate = dateFormat.parse(lifeEvents.getHappenDate());
            countDown = ((happenDate.getTime() - dateCurr.getTime()) / (60 * 60 * 1000 * 24));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        /**
         * 解决ReceiverView 复用机制 造成的试图错乱
         * 默认值*/
        holder.type.setImageDrawable(mContext.getDrawable(R.drawable.llun));
        holder.grade.setImageDrawable(mContext.getDrawable(R.drawable.llun));
        holder.delete.setVisibility(View.GONE);
        holder.cancel.setVisibility(View.GONE);
        holder.countdown.setVisibility(View.VISIBLE);


        switch (lifeEvents.getType()){
            case 1:
                holder.type.setImageDrawable(mContext.getDrawable(R.drawable.study));
                break;
            case 2:
                holder.type.setImageDrawable(mContext.getDrawable(R.drawable.work));
                break;
            case 3:
                holder.type.setImageDrawable(mContext.getDrawable(R.drawable.exam));
                break;
            case 4:
                holder.type.setImageDrawable(mContext.getDrawable(R.drawable.anniversary));
                break;
            case 5:
                holder.type.setImageDrawable(mContext.getDrawable(R.drawable.trip));
                break;
                default:
                    break;
        }

        switch (lifeEvents.getGrade()){
            case 0:
                holder.grade.setImageDrawable(mContext.getDrawable(R.drawable.dot_red));
                break;
            case 1:
                holder.grade.setImageDrawable(mContext.getDrawable(R.drawable.dot_orange));
                break;
            case 2:
                holder.grade.setImageDrawable(mContext.getDrawable(R.drawable.dot_yellow));
                break;
            case 3:
                holder.grade.setImageDrawable(mContext.getDrawable(R.drawable.dot_blue));
                break;
            case 4:
                holder.grade.setImageDrawable(mContext.getDrawable(R.drawable.dot_green));
                break;
            default:
                break;
        }

        holder.title.setText(lifeEvents.getTitle());
        holder.countdown.setText(""+countDown);
        holder.happenDate.setText(lifeEvents.getHappenDate());
        holder.weekday.setText(BaseActivity.stringToWeekday("yyyy-MM-dd",lifeEvents.getHappenDate()));

    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public int getItemCount() {
        return mLifeEvensList.size();
    }

    private String getCurrDate() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+08"));
        String currDate = df.format(new Date());
        return currDate;
    }

}
