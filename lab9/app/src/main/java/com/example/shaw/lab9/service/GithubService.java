package com.example.shaw.lab9.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.example.shaw.lab9.model.Github;
import com.example.shaw.lab9.model.Reponsitory;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GithubService{//访问接口的定义
    @GET("/users/{user}")
    Observable<Github> getUser(@Path("user") String user);


    @GET("/users/{user}/repos")
    Observable<List<Reponsitory>> getUserRepos(@Path("user") String user);
}
