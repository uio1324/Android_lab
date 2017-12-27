package com.example.shaw.lab9;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shaw.lab9.adapter.CommonAdapter;
import com.example.shaw.lab9.adapter.ViewHolder;
import com.example.shaw.lab9.factory.ServiceFactory;
import com.example.shaw.lab9.model.Github;
import com.example.shaw.lab9.model.Reponsitory;
import com.example.shaw.lab9.service.GithubService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Infomation extends AppCompatActivity {
    RecyclerView infomation;
    ProgressBar progressBar;
    List<Map<String,Object>> infoList = new ArrayList<>();
    CommonAdapter adapter;
    private GithubService service;

    String login = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_infomation);
        infomation = findViewById(R.id.infomationRecyclerView);
        progressBar = findViewById(R.id.progressInfomationProgressBar);
        Retrofit GithubRetrofit = ServiceFactory.createRetrofit("https://api.github.com/");
        service = GithubRetrofit.create(GithubService.class);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            login = bundle.getString("name");
        }

        //设置adapter的数据绑定
        infomation.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CommonAdapter(this, R.layout.item_for_infomation, infoList) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> data) {
                TextView name = holder.getView(R.id.infomationNameTextView);
                TextView language = holder.getView(R.id.infomationLanTextView);
                TextView hint = holder.getView(R.id.infomationHintTextView);
                name.setText(data.get("name").toString());
                language.setText(data.get("language").toString());
                hint.setText(data.get("hint").toString());
            }
        };
        infomation.setAdapter(adapter);

        service.getUserRepos(login)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Reponsitory>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("完成传输");
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(Infomation.this, e.getMessage() + "请确认你搜索的用户拥有Reponsitory", Toast.LENGTH_SHORT).show();
                        Log.e("Github-Demo", e.getMessage());
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onNext(List<Reponsitory> reponsitories) {
                        for (int i = 0; i < reponsitories.size(); i++){
                            adapter.addData(reponsitories.get(i));//数据类型不同，这里是List，adapter中传入的是Reponsitory，转换一下再存入
                        }
                    }
                });
    }
}
