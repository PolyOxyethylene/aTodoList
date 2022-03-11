package com.example.todolist;

import java.util.Calendar;

/**
 * 定义了关于待做事项的 recyclerview 适配器类型*/
public class todo {

    private Calendar timeOfSetTodo;

    private String mainTask;

    private boolean finished;

    public todo (String task) {
        timeOfSetTodo = Calendar.getInstance();
        timeOfSetTodo.setTimeInMillis(System.currentTimeMillis());
        mainTask = new String(task);
        finished = false;
    }

    public String getTimeOfSetTodo () {
        StringBuffer newStr = new StringBuffer();
        newStr.append((timeOfSetTodo.get(Calendar.MONTH) + 1)+ " 月 " + timeOfSetTodo.get(Calendar.DATE) + " 日\n    " + timeOfSetTodo.get(Calendar.HOUR) + ":");
        if(timeOfSetTodo.get(Calendar.MINUTE) < 10) {
            newStr.append("0" + timeOfSetTodo.get(Calendar.MINUTE));
        }
        else {newStr.append(timeOfSetTodo.get(Calendar.MINUTE));}
        return newStr.toString();
    }

    public String getTaskContent () {
        return mainTask;
    }

    public boolean getTaskState () {
        return finished;
    }

    public boolean changeTaskContent(String str) {
        mainTask = new String(str);
        return true;
    }
}
