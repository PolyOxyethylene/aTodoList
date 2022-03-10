package com.example.todolist;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.todo;

import java.util.List;

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder> {

    private List<todo> mTodoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View todoListView;
        TextView setTodoTime;
        TextView taskContent;
        CheckBox checkTaskFinish;
        boolean finished;

        public ViewHolder (View view) {
            super(view);
            todoListView = view;
            finished = false;
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

        holder.taskContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "原来你也玩原神", Toast.LENGTH_SHORT).show();
            }
        });

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
        holder.finished = aTodo.getTaskState();
    }

    @Override
    public int getItemCount() {
        return  mTodoList.size();
    }


}




//public class TodoAdapter extends ArrayAdapter<todo> {
//
//    private int resourceId;
//
//    public TodoAdapter (Context context, int textViewResourceId, List<todo> objects) {
//        super(context, textViewResourceId, objects);
//        resourceId = textViewResourceId;
//    }
//
//    @Override
//    public View getView (int position, View convertView, ViewGroup parents) {
//        todo newTask = getItem(position);
//        ViewHolder viewHolder;
//        View view;
//        if(convertView == null) {
//            view = LayoutInflater.from(getContext()).inflate(resourceId, parents, false);
//            viewHolder = new ViewHolder();
//            viewHolder.mainTask = view.findViewById(R.id.things_todo);
//            viewHolder.setTodoTime = view.findViewById(R.id.setTodo_time);
//            view.setTag(viewHolder);
//        }
//        else {
//            view = convertView;
//            viewHolder = (ViewHolder) view.getTag();
//        }
////        TextView mainTask = (TextView) view.findViewById(R.id.show_todos_number);
////        mainTask.setText(newTask.getTaskContent());
//        return view;
//    }
//
//    class ViewHolder {
//
//        TextView mainTask;
//
//        TextView setTodoTime;
////        boolean finished;
//    }
//}
