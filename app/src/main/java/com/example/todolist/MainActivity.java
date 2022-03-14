package com.example.todolist;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {

    private int counts = 0; // 记录待办的数量（不包括子任务）

    private int countsFinished = 0;

    private TextView cnt_todos; // 在主界面输出待办数量

    private TextView cnt_todosFinished; // 在主界面输出已完成的待办数量

    private ImageView emptyTodo;    // 当待办为空时输出的一个图标

    private List<todo> todoList = new ArrayList<>();    //放未完成待办的地方

    private List<todo> todoList_finished = new ArrayList<>();   //放已完成待办的地方

    private TodoAdapter todoAdapter;    // 未完成待办列表的适配器

    private TodoAdapter todoAdapter_finished;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        // 初始化待办列表，读取保存的数据，排序是为了还原上一次活动结束前的待办顺序
        List<todo> tempList = LitePal.findAll(todo.class);
        for(todo item : tempList) {
            if(item.isFinished()) {
                todoList_finished.add(item);
            }
            else {
                todoList.add(item);
            }
        }

        //排序
        if(!todoList.isEmpty()) {
            todoList.sort((todo A, todo B) -> {
                return B.getTimeOfSetTodoC().compareTo(A.getTimeOfSetTodoC());
            });
        }

        if(!todoList_finished.isEmpty()) {
            todoList_finished.sort((todo A, todo B) -> {
                return B.getTimeOfSetTodoC().compareTo(A.getTimeOfSetTodoC());
            });
        }

        // 待办空时显示图标
        counts = todoList.size() + todoList_finished.size();
        countsFinished = todoList_finished.size();
        emptyTodo = (ImageView) findViewById(R.id.empty);
        cnt_todos = (TextView) findViewById(R.id.show_todos_number);
        cnt_todosFinished = (TextView) findViewById(R.id.todos_finish_number);

        if(counts != 0) {
            emptyTodo.setVisibility(View.GONE);
        }
        if(countsFinished == 0) {
            cnt_todosFinished.setText("");
        }
        else {
            cnt_todosFinished.setText("已完成 " + countsFinished + " 件");
        }

        cnt_todos.setText(counts + "个待办");

        // 状态栏沉浸，去掉标题栏
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }


        // 未完成的待办列表
        RecyclerView recyclerView1 = (RecyclerView) findViewById(R.id.recyclerview_unfinish);
        recyclerView1.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter = new TodoAdapter(todoList);
        recyclerView1.setAdapter(todoAdapter);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());

        // 已完成的待办列表
        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerview_finished);
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        todoAdapter_finished = new TodoAdapter(todoList_finished);
        recyclerView2.setAdapter(todoAdapter_finished);
        recyclerView2.setItemAnimator(new DefaultItemAnimator());



        // 这是界面右下角的蓝色添加按钮，点击添加待办
        ImageButton addList = (ImageButton) findViewById(R.id.Add_List);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodoDialog();
            }
        });


        // 为 todoAdapter 设置长按和点击监听器
        // 点击的监听器和对应事件
        todoAdapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
                newDialogBuilder.setView(R.layout.text_dialog);
                AlertDialog newDialog = newDialogBuilder.create();
                newDialog.show();
                EditText newTodoContent = (EditText) newDialog.findViewById(R.id.add_todo);
                Button confirmAddition = (Button) newDialog.findViewById(R.id.add);
                confirmAddition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tempText = newTodoContent.getText().toString();
                        if(!TextUtils.isEmpty(tempText)) {
                            todoList.get(position).setMainTask(tempText);
                            todoList.get(position).save();
                            todoAdapter.notifyDataSetChanged();
                        }
                        newDialog.dismiss();
                    }
                });
            }
        });

        // 长按的监听器和对应事件
        todoAdapter.setOnItemLongClickListener(new TodoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(view.getContext(), R.style.MyDialog);
                newDialogBuilder.setView(R.layout.edit_todo_dialog);
                AlertDialog newDialog = newDialogBuilder.create();
                newDialog.setCancelable(false);
                newDialog.show();

                // 两个按钮，删除和取消
                Button delete = (Button) newDialog.findViewById(R.id.choice_delete);
                Button cancelDelete = (Button) newDialog.findViewById(R.id.choice_delete_cancel);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        todoList.get(position).delete();
                        todoList.remove(position);
                        todoAdapter.notifyItemRemoved(position);
//                        Toast.makeText(MainActivity.this, "您删除了", Toast.LENGTH_SHORT).show();
                        counts = todoList.size() + todoList_finished.size();
                        if(counts == 0) {
                            emptyTodo.setVisibility(View.VISIBLE);
                        }
                        cnt_todos.setText(counts + "个待办");
                        todoAdapter.notifyItemRangeChanged(0,todoList.size());
                        newDialog.dismiss();
                    }
                });

                // 取消的话直接退出就行
                cancelDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newDialog.dismiss();
                    }
                });

            }
        });

        todoAdapter.setOnCheckCLickListener(new TodoAdapter.OnCheckCLickListener() {
            @Override
            public void onCheckCLick(View view, int position) {
                CheckBox cao = view.findViewById(R.id.check_finish);
                if(cao.isChecked()) {
//                    Toast.makeText(MainActivity.this, "您点击了添加键", Toast.LENGTH_SHORT).show();
                    todo tempTodo = todoList.get(position);
                    tempTodo.setFinished(true);
                    cao.setChecked(true);
                    todoList.remove(position);
                    todoList_finished.add(0,tempTodo);
                    todoAdapter.notifyItemRemoved(position);
                    todoAdapter_finished.notifyItemInserted(0);
                    countsFinished = todoList_finished.size();
                    cnt_todosFinished.setText("已完成 " + countsFinished + " 件");
                }
            }
        });

        // 已完成待办的适配器的点击修改内容功能跟未完成的是一样的
        todoAdapter_finished.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
                newDialogBuilder.setView(R.layout.text_dialog);
                AlertDialog newDialog = newDialogBuilder.create();
                newDialog.show();
                EditText newTodoContent = (EditText) newDialog.findViewById(R.id.add_todo);
                Button confirmAddition = (Button) newDialog.findViewById(R.id.add);
                confirmAddition.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String tempText = newTodoContent.getText().toString();
                        if(!TextUtils.isEmpty(tempText)) {
                            todoList_finished.get(position).setMainTask(tempText);
                            todoList_finished.get(position).save();
                            todoAdapter_finished.notifyDataSetChanged();
                        }
                        newDialog.dismiss();
                    }
                });
            }
        });

        todoAdapter_finished.setOnItemLongClickListener(new TodoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(view.getContext(), R.style.MyDialog);
                newDialogBuilder.setView(R.layout.edit_todo_dialog);
                AlertDialog newDialog = newDialogBuilder.create();
                newDialog.setCancelable(false);
                newDialog.show();

                // 两个按钮，删除和取消
                Button delete = (Button) newDialog.findViewById(R.id.choice_delete);
                Button cancelDelete = (Button) newDialog.findViewById(R.id.choice_delete_cancel);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        todoList_finished.get(position).delete();
                        todoList_finished.remove(position);
                        todoAdapter_finished.notifyItemRemoved(position);
                        counts = todoList_finished.size() + todoList.size();
                        if (counts == 0) {
                            emptyTodo.setVisibility(View.VISIBLE);
                        }
                        cnt_todos.setText(counts + "个待办");
                        countsFinished = todoList_finished.size();
                        if(countsFinished == 0) {
                            cnt_todosFinished.setText("");
                        }
                        else {
                            cnt_todosFinished.setText("已完成 " + countsFinished + " 件");
                        }
                        todoAdapter_finished.notifyItemRangeChanged(0, todoList.size());
                        newDialog.dismiss();
                    }
                });

                cancelDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        newDialog.dismiss();
                    }
                });
            }
        });

        todoAdapter_finished.setOnCheckCLickListener(new TodoAdapter.OnCheckCLickListener() {
            @Override
            public void onCheckCLick(View view, int position) {
                CheckBox cao = view.findViewById(R.id.check_finish);
                if(!cao.isChecked()) {
//                    Toast.makeText(MainActivity.this, "您点击了添加键", Toast.LENGTH_SHORT).show();
                    todo tempTodo = todoList_finished.get(position);
                    tempTodo.setFinished(false);
                    cao.setChecked(false);
                    todoList_finished.remove(position);
                    todoList.add(0,tempTodo);
                    todoAdapter_finished.notifyItemRemoved(position);
                    todoAdapter.notifyItemInserted(0);
                    countsFinished = todoList_finished.size();
                    if(countsFinished == 0) {
                        cnt_todosFinished.setText("");
                    }
                    else {
                        cnt_todosFinished.setText("已完成 " + countsFinished + " 件");
                    }
                }
            }
        });

    }

    // 添加按钮的对话框事件
    private void addTodoDialog () {
        AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
        newDialogBuilder.setView(R.layout.text_dialog);
        AlertDialog newDialog = newDialogBuilder.create();
        newDialog.show();
        EditText newTodoContent = (EditText) newDialog.findViewById(R.id.add_todo);
        Button confirmAddition = (Button) newDialog.findViewById(R.id.add);
        confirmAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTodoContent.getText();
                String newTaskContent = newTodoContent.getText().toString();
                if(newTaskContent != null && !TextUtils.isEmpty(newTaskContent)) {
                    todo newTodo = new todo(newTaskContent);
                    todoList.add(0,newTodo);
                    newTodo.save();
                    counts = todoList.size() + todoList_finished.size();
                    cnt_todos.setText(counts + "个待办");
                    todoAdapter.notifyItemInserted(0);
                    emptyTodo.setVisibility(View.GONE);
                }
                newDialog.dismiss();
            }
        });
    }

    // 保险起见
    @Override
    protected void onStop() {
        super.onStop();
        for(todo item : todoList) {
            item.save();
        }
        for(todo item : todoList_finished) {
            item.save();
        }
    }

    // 程序退出前维护数据库
    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(todo item : todoList) {
            item.save();
        }
        for(todo item : todoList_finished) {
            item.save();
        }
    }
}
