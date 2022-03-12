package com.example.todolist;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.Collections;
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

    public interface OnCheckCLickListener {
        void onCheckCLick(View view,int position);
    }

    // 新建两个私有变量用于保存用户设置的监听器及其set方法
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;

    private OnCheckCLickListener mOnCheckCLickListener;

    public void setOnCheckCLickListener(OnCheckCLickListener mOnCheckCLickListener) {
        this.mOnCheckCLickListener = mOnCheckCLickListener;
    }

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
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        todo aTodo = mTodoList.get(position);
        holder.setTodoTime.setText(aTodo.getTimeOfSetTodo());
        holder.taskContent.setText(aTodo.getMainTask());

        holder.checkTaskFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!compoundButton.isPressed())return;	//加这一条，否则当我setChecked()时会触发此listener
            }
        });

        if(aTodo.isFinished() == true) {
            holder.itemView.setBackgroundResource(R.drawable.textbar_checked);
            holder.checkTaskFinish.setChecked(true);
        }
        else {holder.checkTaskFinish.setChecked(false);}

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

        if(mOnCheckCLickListener != null) {
            holder.checkTaskFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = holder.getLayoutPosition();
                    mOnCheckCLickListener.onCheckCLick(view, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return  mTodoList.size();
    }

}

//    //通过返回值来设置是否处理某次拖曳或者滑动事件
//    @Override
//    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//        if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
//            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN |
//                    ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
//            int swipeFlags = 0;
//            return makeMovementFlags(dragFlags, swipeFlags);
//        } else {
//            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
//            int swipeFlags = 0;
//            return makeMovementFlags(dragFlags, swipeFlags);
//        }
//    }

//    //当长按并进入拖曳状态时，拖曳的过程中不断的回调此方法
//    @Override
//    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//        //拖动的 item 的下标
//        int fromPosition = viewHolder.getAdapterPosition();
//        //目标 item 的下标，目标 item 就是当拖曳过程中，不断和拖动的 item 做位置交换的条目。
//        int toPosition = target.getAdapterPosition();
//        if (fromPosition < toPosition) {
//            for (int i = fromPosition; i < toPosition; i++) {
//                Collections.swap(todoList, i, i + 1);
//            }
//        } else {
//            for (int i = fromPosition; i > toPosition; i--) {
//                Collections.swap(todoList, i, i - 1);
//            }
//        }
//        todoAdapter.notifyItemMoved(fromPosition, toPosition);
//        return true;
//    }
//
//    //滑动删除的回调
//    @Override
//    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//        int adapterPosition = viewHolder.getAdapterPosition();
//        todoAdapter.notifyItemRemoved(adapterPosition);
//        todoList.remove(adapterPosition);
//    }


//        holder.checkTaskFinish.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                if(compoundButton.isChecked()) {
//                    view.setBackgroundResource(R.drawable.textbar_checked);
//                    mTodoList.get(holder.getAdapterPosition()).setFinished(true);
//                }
//                else if(!compoundButton.isChecked()){
//                    view.setBackgroundResource(R.drawable.textbar);
//                }
//            }
//        });
