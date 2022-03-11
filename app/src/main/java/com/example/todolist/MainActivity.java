package com.example.todolist;

import android.content.Context;
import android.graphics.ColorSpace;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private int counts = 0; // 记录待办的数量（不包括子任务）
    private TextView cnt_todos; // 在主界面输出待办数量
    private ImageView emptyTodo;
    private List<todo> todoList = new ArrayList<>();
    private EditText newTodoContent;
    private String newTaskContent;
    private Button confirmAddition;
    private TodoAdapter todoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.initialize(this);
        todoList = LitePal.findAll(todo.class);
        if(!todoList.isEmpty()) {
            todoList.sort((todo A, todo B) -> {
                return B.getTimeOfSetTodoC().compareTo(A.getTimeOfSetTodoC());
            });
        }


        counts = todoList.size();
        emptyTodo = (ImageView) findViewById(R.id.empty);
        if(counts != 0) {
            emptyTodo.setVisibility(View.GONE);
        }
        cnt_todos = (TextView) findViewById(R.id.show_todos_number);
        cnt_todos.setText(counts + "个待办");

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT)
        {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }





        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        todoAdapter = new TodoAdapter(todoList);
        recyclerView.setAdapter(todoAdapter);



        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.hide();
        }


        ImageButton addList = (ImageButton) findViewById(R.id.Add_List);
        addList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addTodoDialog();

            }
        });

        // 为 todoAdapter 设置长按和点击监听器
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

        todoAdapter.setOnItemLongClickListener(new TodoAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(view.getContext(), R.style.MyDialog);
                newDialogBuilder.setView(R.layout.edit_todo_dialog);
                AlertDialog newDialog = newDialogBuilder.create();
                newDialog.setCancelable(false);
                newDialog.show();
                Button delete = (Button) newDialog.findViewById(R.id.choice_delete);
                Button cancelDelete = (Button) newDialog.findViewById(R.id.choice_delete_cancel);

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        todoList.get(position).delete();
                        todoList.remove(position);
                        todoAdapter.notifyItemRemoved(position);
                        Toast.makeText(MainActivity.this, "您删除了", Toast.LENGTH_SHORT).show();
                        counts = todoList.size();
                        if(counts != 0) {
                            emptyTodo.setVisibility(View.GONE);
                        }
                        cnt_todos.setText(counts + "个待办");
                        todoAdapter.notifyItemRangeChanged(0,todoList.size());
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

    }



    private void addTodoDialog () {
        AlertDialog.Builder newDialogBuilder = new AlertDialog.Builder(MainActivity.this, R.style.MyDialog);
        newDialogBuilder.setView(R.layout.text_dialog);
        AlertDialog newDialog = newDialogBuilder.create();
        newDialog.show();
        newTodoContent = (EditText) newDialog.findViewById(R.id.add_todo);
        confirmAddition = (Button) newDialog.findViewById(R.id.add);
        confirmAddition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newTodoContent.getText();
//                Toast.makeText(MainActivity.this, "您点击了添加键", Toast.LENGTH_SHORT).show();
                newTaskContent = newTodoContent.getText().toString();
                if(newTaskContent != null && !TextUtils.isEmpty(newTaskContent)) {
                    todo newTodo = new todo(newTaskContent);
                    todoList.add(0,newTodo);
                    newTodo.save();
                    counts = todoList.size();
                    cnt_todos.setText(counts + "个待办");
                    todoAdapter.notifyDataSetChanged();
                    emptyTodo.setVisibility(View.GONE);
                }
                newDialog.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for(todo item : todoList) {
            item.save();
        }
    }
}