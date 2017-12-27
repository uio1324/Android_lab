package com.example.shaw.lab9;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaw.lab9.adapter.CommonAdapter;
import com.example.shaw.lab9.adapter.ViewHolder;
import com.example.shaw.lab9.factory.ServiceFactory;
import com.example.shaw.lab9.model.Github;
import com.example.shaw.lab9.service.GithubService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    List<Map<String, Object>> itemList = new ArrayList<>();
    CommonAdapter adapter;
    private GithubService service;

    EditText search;
    Button clear, fetch;
    RecyclerView ListRecyclerView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化界面控件
        search = findViewById(R.id.searchEditText);
        clear = findViewById(R.id.clearButton);
        fetch = findViewById(R.id.fetchButton);
        ListRecyclerView = findViewById(R.id.itemListRecyclerView);
        progressBar = findViewById(R.id.prograssMainProgressBar);

        Retrofit GithubRetrofit = ServiceFactory.createRetrofit("https://api.github.com/");
        service = GithubRetrofit.create(GithubService.class);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clearAllData();
            }
        });

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String User = search.getText().toString();
                progressBar.setVisibility(View.VISIBLE);//开始传输先设置转圈圈
                service.getUser(User)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Github>() {
                            @Override
                            public void onCompleted() {
                                System.out.println("完成传输");
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(MainActivity.this, e.getMessage() + "请确认你搜索的用户存在", Toast.LENGTH_SHORT).show();
                                Log.e("Github-Demo", e.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onNext(Github github) {//执行添加数据任务
                                adapter.addData(github);
                            }
                        });
            }
        });





        //使用CommonAdapter进行数据绑定
        ListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter(this, R.layout.item_for_activity_main, itemList) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> data) {
                TextView name = holder.getView(R.id.itemNameTextView);
                TextView id = holder.getView(R.id.itemIDTextView);
                TextView blog = holder.getView(R.id.itemBlogTextView);
                name.setText(data.get("name").toString());
                id.setText(data.get("id").toString());
                blog.setText(data.get("blog").toString());
            }
        };
        ListRecyclerView.setAdapter(adapter);//设置adapter使其生效
        //设置adapter的点击、长按操作
        adapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this, Infomation.class);
                intent.putExtra("name", adapter.getData(position, "name"));
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {//长按删除
                adapter.removeData(position);
            }
        });




    }
}
