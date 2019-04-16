package com.example.ygz.dm.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author:Ygz
 * @date:2018/10/26 time:23:13
 * @project:ActivityCollector
 * @remark:无
 */
public class ActivityCollector {
    public List<Activity> activities = new ArrayList<>();

    public void addActivity(Activity activity){
        activities.add(activity);
    }
    public void removeActivity(Activity activity){
        activities.remove(activity);
    }
    public void finishAllActivity(){
        for (Activity activity:activities){
            activity.finish();
        }
    }
    public int getActivityListLen(){
        return activities.size();
    }
}
