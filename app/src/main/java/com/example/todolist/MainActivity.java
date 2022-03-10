package com.example.todolist;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.TodoAdapter;

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

        emptyTodo = (ImageView) findViewById(R.id.empty);

        cnt_todos = (TextView) findViewById(R.id.show_todos_number);

        cnt_todos.setText(counts + "个待办");

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


    }

    @Override
    public void onResume() {
        super.onResume();
        emptyTodo.bringToFront();
        if(counts == 0) {
            emptyTodo.setVisibility(View.VISIBLE);
        }
        else {
            emptyTodo.setVisibility(View.GONE);
        }
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
                Toast.makeText(MainActivity.this, "您点击了添加键", Toast.LENGTH_SHORT).show();
                newTaskContent = newTodoContent.getText().toString();
                if(newTaskContent != null && !TextUtils.isEmpty(newTaskContent)) {
                    todo newTodo = new todo(newTaskContent);
                    todoList.add(newTodo);
                    counts = todoList.size();
                    cnt_todos.setText(counts + "个待办");
                    todoAdapter.notifyDataSetChanged();
                    emptyTodo.setVisibility(View.GONE);
                }
                newDialog.dismiss();
            }
        });
    }




}