package com.example.ygz.dm.javaBeans;

import cn.bmob.v3.BmobObject;

/**
 * @author:Ygz
 * @date:2018/10/31 time:18:42
 * @project:LifeEvents
 * @remark:事件类
 */
public class LifeEvents extends BmobObject {


    String userID;
    String title;//事件标题
    int type;//事件类型，使用数字标号
    int grade;//重要等级，使用数字表示
    String happenDate;//发生日期
    String remarks;//事件详细信息（备注）
    Boolean trash;//回收站（true为在回收站）


    public String getUserID() {
        return userID;
    }
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public int getGrade() {
        return grade;
    }
    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getHappenDate() {
        return happenDate;
    }
    public void setHappenDate(String happenDate) {
        this.happenDate = happenDate;
    }

    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Boolean getTrash() {
        return trash;
    }
    public void setTrash(Boolean trash) {
        this.trash = trash;
    }

}
