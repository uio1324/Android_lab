package com.example.shaw.lab7;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText NewPassword, ConfirmPassword, Password;
    Button OK,CLEAR;
    SharedPreferences SP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        SP = getSharedPreferences("Save Password", MODE_PRIVATE);
        if(SP.getString("Password","").equals("")){
            setContentView(R.layout.activity_main);
            onStartFirst();
        }
        else{
            setContentView(R.layout.activity_main_again);
            onStartAgain();
        }
    }

    void onStartFirst(){
        NewPassword = findViewById(R.id.NewPasswordEditText);
        ConfirmPassword = findViewById(R.id.ConfirmPasswordEditText);
        OK = findViewById(R.id.OKButton);
        CLEAR = findViewById(R.id.CLEARButton);


        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String P1, P2;
                P1 = NewPassword.getText().toString();
                P2 = ConfirmPassword.getText().toString();
                if(P1.equals("")){
                    Toast.makeText(MainActivity.this, "Password cannot be empty", Toast.LENGTH_LONG).show();
                }
                else if(!P1.equals(P2)){
                    Toast.makeText(MainActivity.this,"Password Mismatch", Toast.LENGTH_LONG).show();
                }
                //先行确认，然后进行密码存储，使用SharedPreferences
                else{
                    SharedPreferences.Editor SPEdit = SP.edit();
                    SPEdit.putString("Password", P1);
                    SPEdit.commit();
                    Toast.makeText(MainActivity.this, "Password accepted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,FileEditor.class);
                    startActivityForResult(intent,1);
                }
            }
        });

        CLEAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewPassword.setText("");
                ConfirmPassword.setText("");
            }
        });
    }

    void onStartAgain(){
        Password = findViewById(R.id.PasswordEditText);
        OK = findViewById(R.id.OKButton1);
        CLEAR = findViewById(R.id.CLEARButton1);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String P1 = Password.getText().toString();
                String PW = SP.getString("Password", "");
                if(P1.equals("")){
                    Toast.makeText(MainActivity.this,"Password cannot be empty", Toast.LENGTH_LONG).show();
                }
                //判断密码是否正确
                else if (!P1.equals(PW)){
                    Toast.makeText(MainActivity.this,"Password Mismatch", Toast.LENGTH_LONG).show();
                }
                //密码正确
                else{
                    Toast.makeText(MainActivity.this,"Password accepted", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(MainActivity.this,FileEditor.class);
                    startActivityForResult(intent,1);
                }
            }
        });
        CLEAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Password.setText("");
            }
        });


    }





}
