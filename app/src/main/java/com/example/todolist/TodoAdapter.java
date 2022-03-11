package com.example.todolist;

import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    // 为适配器添加触摸事件
    // 新建内部接口
    public interface OnItemClickListener{
        void onItemClick(View view,int position);
    }

    public interface OnItemLongClickListener{
        void onItemLongClick(View view,int position);
    }

    // 新建两个私有变量用于保存用户设置的监听器及其set方法
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener mOnItemLongClickListener) {
        this.mOnItemLongClickListener = mOnItemLongClickListener;
    }



    private List<todo> mTodoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View todoListView;
        TextView setTodoTime;
        TextView taskContent;
        CheckBox checkTaskFinish;

        public ViewHolder (View view) {
            super(view);
            todoListView = view;
            setTodoTime = (TextView) view.findViewById(R.id.setTodo_time);
            taskContent = (TextView) view.findViewById(R.id.things_todo);
            checkTaskFinish = (CheckBox) view.findViewById(R.id.check_finish);
        }
    }

    public TodoAdapter (List<todo> todoList) {
        mTodoList = todoList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int Viewtype) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_items, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.checkTaskFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()) {
                    view.setBackgroundResource(R.drawable.textbar_checked);
                }
                else if(!compoundButton.isChecked()){
                    view.setBackgroundResource(R.drawable.textbar);
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        todo aTodo = mTodoList.get(position);
        holder.setTodoTime.setText(aTodo.getTimeOfSetTodo());
        holder.taskContent.setText(aTodo.getTaskContent());

        if(mOnItemClickListener != null) {
            // 为todoListView设置监听器
            holder.todoListView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.todoListView, position);
                }
            });
        }

        if(mOnItemLongClickListener != null) {
            holder.todoListView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnItemLongClickListener.onItemLongClick(holder.todoListView, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return  mTodoList.size();
    }


}



