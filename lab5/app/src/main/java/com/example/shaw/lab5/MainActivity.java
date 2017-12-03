package com.example.shaw.lab5;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity {
    ImageView AlbumImage;
    TextView CurrentTime, Status, CompleteTime;
    SeekBar Music;
    Button Play, Stop, Quit;
    SimpleDateFormat time_format = new SimpleDateFormat("mm:ss");
    ObjectAnimator ImageRotation;
    static boolean hasPermission = false;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };
    private static final int REQUEST_EXTERNAL_STORASGE = 1;
    private MusicService.MyBinder mBinder;

    private ServiceConnection sc = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("service","connected");
            mBinder = (MusicService.MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            sc = null;
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AlbumImage = (ImageView)findViewById(R.id.AlbumImageView);
        CurrentTime = (TextView)findViewById(R.id.CurrentTimeTextView);
        Status = (TextView)findViewById(R.id.StatusTextView);
        CompleteTime = (TextView)findViewById(R.id.CompleteTimeTextView);
        Music = (SeekBar)findViewById(R.id.MusicSeekBar);
        Play = (Button)findViewById(R.id.PlayButton);
        Stop = (Button)findViewById(R.id.StopButton);
        Quit = (Button)findViewById(R.id.QuitButton);
        //初始化图片旋转动画，使用ObjectAnimator实现
        ImageRotation = ObjectAnimator.ofFloat(AlbumImage, "rotation", 0.0f,360.0f);
        ImageRotation.setDuration(10000);
        ImageRotation.setRepeatCount(-1);
        ImageRotation.setInterpolator(new LinearInterpolator());
        ImageRotation.start();
        ImageRotation.pause();

        verifyStoragePermission(MainActivity.this);

        Intent intent = new Intent(this,MusicService.class);
        startService(intent);
        bindService(intent,sc, Context.BIND_AUTO_CREATE);

        final Handler mHandler = new Handler(){
            @Override
            public void handleMessage (Message msg){
                super.handleMessage(msg);
                switch (msg.what){
                    case 123:
                        try {
                            Parcel data = Parcel.obtain();
                            Parcel reply = Parcel.obtain();
                            mBinder.transact(104, data, reply, 0);//执行界面刷新操作
                            int currenttime = reply.readInt();
                            CurrentTime.setText(time_format.format(currenttime));
                            int completetime = reply.readInt();
                            CompleteTime.setText(time_format.format(completetime));
                            Music.setProgress(currenttime);
                            Music.setMax(completetime);
                            if(reply.readInt() == 1){//is playing
                                ImageRotation.resume();
                            }
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };

        final Thread mThread = new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    if(sc != null && hasPermission == true){
                        mHandler.obtainMessage(123).sendToTarget();
                    }
                }
            }

        };
        mThread.start();

        Music.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    try {
                        Parcel data = Parcel.obtain();
                        Parcel reply = Parcel.obtain();
                        data.writeInt(progress);
                        mBinder.transact(105, data, reply,0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //开始定义每个按钮的执行功能
        //播放和暂停键
        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(101 ,data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                if(v.getTag().toString().equals("1")){
                    ((Button)v).setText("PAUSE");
                    v.setTag(0);
                    ImageRotation.resume();
                    Status.setText("Playing");
                }
                else{
                    ((Button)v).setText("PLAY");
                    v.setTag(1);
                    ImageRotation.pause();
                    Status.setText("Paused");
                }
            }
        });
        //停止键
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(102 ,data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                Play.setText("PLAY");
                Play.setTag("1");
                Status.setText("Stopped");
                ImageRotation.end();
                ImageRotation.start();
                ImageRotation.pause();//重置
            }
        });

        //退出键
        Quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Parcel data = Parcel.obtain();
                    Parcel reply = Parcel.obtain();
                    mBinder.transact(103 ,data, reply, 0);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                MainActivity.this.getApplication().unbindService(sc);
                sc = null;
                try{
                    MainActivity.this.finish();
                    System.exit(0);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });


    }

    public static void verifyStoragePermission(Activity activity){
        try{
            int permission = ActivityCompat.checkSelfPermission(activity, "android.permission.READ_EXTERNAL_STORAGE");
            if(permission != PackageManager.PERMISSION_GRANTED){
                //弹出对话框请求读取权限
                ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORASGE);
            }
            else{
                hasPermission = true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permission[], int[] grantResults){
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //权限被同意，可以搞事
            Parcel data = Parcel.obtain();
            Parcel reply = Parcel.obtain();
            try {
                mBinder.transact(106, data, reply,0);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        else{
            //打扰了，告辞！
            MainActivity.this.finish();
            System.exit(0);
        }
    }










}
