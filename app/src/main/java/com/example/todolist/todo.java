package com.example.todolist;

import java.util.Calendar;

/**
 * 定义了关于待做事项的 recyclerview 适配器类型*/
public class todo {

    private Calendar timeOfSetTodo;

    private StringBuffer mainTask;

    private boolean finished;

    public todo (String task) {
        timeOfSetTodo = Calendar.getInstance();
        timeOfSetTodo.setTimeInMillis(System.currentTimeMillis());
        mainTask = new StringBuffer();
        mainTask.append(task);
        finished = false;
    }

    public String getTimeOfSetTodo () {
        return timeOfSetTodo.get(Calendar.HOUR) + ":" + timeOfSetTodo.get(Calendar.MINUTE);
    }

    public String getTaskContent () {
        return mainTask.toString();
    }

    public boolean getTaskState () {
        return finished;
    }
}
