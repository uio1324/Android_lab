package com.example.shaw.lab7;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Shaw on 2017/12/11.
 */

public class FileEditor extends AppCompatActivity{
    EditText Name,Content;
    Button SAVE,LOAD,CLEAR,DELETE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.file_editor);

        Name = findViewById(R.id.NameEditText);
        Content = findViewById(R.id.ContentEditText);
        SAVE = findViewById(R.id.SAVEButton);
        LOAD = findViewById(R.id.LOADButton);
        CLEAR = findViewById(R.id.CLEARButton);
        DELETE = findViewById(R.id.DELETEButton);

        SAVE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try(FileOutputStream fileOutputStream = openFileOutput(Name.getText().toString(), MODE_PRIVATE)) {
                    String str = Content.getText().toString();
                    fileOutputStream.write(str.getBytes());
                    Log.i("TAG","Successfully saved file.");
                    Toast.makeText(FileEditor.this, "Save successfully", Toast.LENGTH_LONG).show();
                }
                catch (IOException e) {
                    //e.printStackTrace();
                    Log.e("TAG", "Fail to save file");
                    Toast.makeText(FileEditor.this, "Fail to save file", Toast.LENGTH_LONG).show();
                } ;
            }
        });

        LOAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try(FileInputStream fileInputStream = openFileInput(Name.getText().toString())){
                    byte[] contents = new byte[fileInputStream.available()];
                    fileInputStream.read(contents);
                    String temp = new String(contents);
                    Content.setText(temp);
                    Toast.makeText(FileEditor.this, "Load successfully", Toast.LENGTH_LONG).show();
                }
                catch (IOException e) {
                    //e.printStackTrace();
                    Log.e("TAG", "Fail to read file.");
                    Toast.makeText(FileEditor.this, "Fail to load file", Toast.LENGTH_LONG).show();
                }
            }
        });

        CLEAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Content.setText("");
            }
        });

        DELETE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String temp = Name.getText().toString();
                //尝试寻找文件
                if(FileEditor.this.getApplicationContext().deleteFile(temp)){
                    Toast.makeText(FileEditor.this, "Delete Successfully", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(FileEditor.this, "Failed to find files, Check again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //重载返回键，使其不返回登录页面而是返回home屏
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
