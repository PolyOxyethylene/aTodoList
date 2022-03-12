package com.example.todolist;

import org.litepal.crud.LitePalSupport;

import java.util.Calendar;

/**
 * 定义了关于待做事项的 recyclerview 适配器类型*/
public class todo extends LitePalSupport {

    private String mainTask;

    boolean finished;

    private int month;

    private int date;

    private int hour;

    private int minute;

    private int second;

    public int getSecond() {
        return second;
    }

    public void setSecond(int second) {
        this.second = second;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public void setMainTask(String mainTask) {
        this.mainTask = mainTask;
    }

    public String getTimeOfSetTodo() {
        if(minute < 10) {return (month + 1) + " 月 " + date  + " 日\n   " + hour + ":0" +  minute;}
        else {return (month + 1) + " 月 " + date  + " 日\n   " + hour + ":" + minute;}
    }

    // 用来进行两个待办事件的比较的，小于 10 的数字要补十位的 0
    public String getTimeOfSetTodoC() {
        StringBuffer tempText = new StringBuffer();
        if(month < 10) {tempText.append(0);}
        tempText.append(month);
        if(date < 10) {tempText.append(0);}
        tempText.append(date);
        if(hour < 10) {tempText.append(0);}
        tempText.append(hour);
        if(minute < 10) {tempText.append(0);}
        tempText.append(minute);
        if(second < 10) {tempText.append(0);}
        tempText.append(second);
        return  tempText.toString();
    }

    public String getMainTask() {
        return mainTask;
    }

    public todo (String task) {
        Calendar getTime = Calendar.getInstance();
        getTime.setTimeInMillis(System.currentTimeMillis());
        month = getTime.get(Calendar.MONTH);
        date = getTime.get(Calendar.DATE);
        hour = getTime.get(Calendar.HOUR);
        minute = getTime.get(Calendar.MINUTE);
        second = getTime.get(Calendar.SECOND);
        mainTask = new String(task);
        finished = false;
    }
}
