package com.example.ygz.dm.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ygz.dm.R;
import com.example.ygz.dm.base.BaseActivity;
import com.example.ygz.dm.javaBeans.LifeEvents;

import java.util.List;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import me.grantland.widget.AutofitTextView;

/**
 * @author:Ygz
 * @date:2018/12/8 time:0:13
 * @project:TrashListAdapter
 * @remark:回收站适配器
 */
public class TrashListAdapter extends RecyclerView.Adapter<TrashListAdapter.ViewHolder>{

    private Context mContext;

    private List<LifeEvents> mLifeEvensList;


    /*构造方法*/
    public TrashListAdapter(List<LifeEvents> lifeEventsList){
        mLifeEvensList = lifeEventsList;
    }



    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        AutofitTextView title;
        ImageButton restore,delete;
        public ViewHolder(View itemView) {
            super(itemView);

            cardView = (CardView)itemView;
            title = (AutofitTextView) itemView.findViewById(R.id.item_trash_tv_title);
            restore = (ImageButton)itemView.findViewById(R.id.item_trash_imgbtn_restore);
            delete = (ImageButton)itemView.findViewById(R.id.item_trash_imgbtn_delete);

        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_trash,parent,false);
        final ViewHolder holder = new ViewHolder(view);
/**
 * 恢复事件按钮的点击事件
 * */
            holder.restore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 通知主界面更新
                    LifeEvents lifeEvents = mLifeEvensList.get(holder.getAdapterPosition());
                    lifeEvents.setTrash(false);
                    lifeEvents.update(lifeEvents.getObjectId(),new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null){
                                mLifeEvensList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                            }else {
                                Log.e("Bmob","恢复事件失败：位置：TrashListAdapter 错误信息："+e.getMessage()+"  错误码："+e.getErrorCode());
                            }
                        }
                    });

                }
            });

/**
 * 彻底删除按钮的点击事件
 * */
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO 删除操作
                LifeEvents lifeEvents = mLifeEvensList.get(holder.getAdapterPosition());
                lifeEvents.delete(lifeEvents.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null){
                            mLifeEvensList.remove(holder.getAdapterPosition());
                            notifyItemRemoved(holder.getAdapterPosition());
                        }else {
                            Log.e("Bmob","事件彻底删除失败：位置：TrashListAdapter 错误信息："+e.getMessage()+"  错误码："+e.getErrorCode());
                        }
                    }
                });
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LifeEvents lifeEvents = mLifeEvensList.get(position);
        holder.title.setText(lifeEvents.getTitle());
    }

    @Override
    public int getItemCount() {
        return mLifeEvensList.size();
    }


}
