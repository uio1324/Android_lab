package com.example.shaw.myapplication;

import android.content.DialogInterface;
import android.support.annotation.IdRes;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    @IdRes int ChoiceRes;
    TextInputLayout mNumber;
    TextInputLayout  mPassword;
    EditText NumberEdit;
    EditText PasswordEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView mImage = (ImageView) findViewById(R.id.imageView);
        final AlertDialog.Builder mDialog = new AlertDialog.Builder(this);
        final String choseDialog[] = {"拍摄","从相册选择"};
        mDialog.setTitle("上传图片")
                .setItems(choseDialog, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(getApplication(),"您选择了["+choseDialog[i]+"]",Toast.LENGTH_LONG).show();}
                    })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        Toast.makeText(getApplication(),R.string.cancel,Toast.LENGTH_LONG).show();}
                }).create();

        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mDialog.show();
            }
        } );


        mNumber = (TextInputLayout) findViewById(R.id.editNum);
        mPassword = (TextInputLayout) findViewById(R.id.editPassword);
        NumberEdit =  mNumber.getEditText();
        PasswordEdit =  mNumber.getEditText();
        NumberEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0)  mNumber.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        PasswordEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.length() != 0)  mPassword.setErrorEnabled(false);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        final RadioGroup mGroup = (RadioGroup)findViewById(R.id.radioGroup);
        mGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if(checkedId==R.id.radioButton_Student)
                {
                    Snackbar.make(group,R.string.radioButton_Student,Snackbar.LENGTH_LONG)
                            .setAction(R.string.confirm, new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Toast.makeText(getApplication(),"Snackerbar的确定按钮被点击了",Toast.LENGTH_LONG).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
                else
                {
                    Snackbar.make(group,R.string.radioButton_Teacher,Snackbar.LENGTH_LONG)
                            .setAction(R.string.confirm, new View.OnClickListener(){
                                @Override
                                public void onClick(View view){
                                    Toast.makeText(getApplication(),"Snackerbar的确定按钮被点击了",Toast.LENGTH_LONG).show();
                                }
                            })
                            .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                            .setDuration(5000)
                            .show();
                }
            }
        });

        Button mLogin = (Button)findViewById(R.id.button_Login);
        mLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int msg;
                String getNumber = mNumber.getEditText().getText().toString();
                String getPassword = mPassword.getEditText().getText().toString();
                if (getNumber.equals("123456") && getPassword.equals("6666")) {
                    msg = R.string.loginSuccess;
                }
                else {
                    msg = R.string.loginFail;
                }
                Snackbar.make(view,msg,Snackbar.LENGTH_LONG)
                        .setAction(R.string.confirm, new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Toast.makeText(getApplication(),"Snackerbar的确定按钮被点击了",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(5000)
                        .show();

                if(getNumber.length() == 0)
                {
                    mNumber.setErrorEnabled(true);
                    mNumber.setError("学号不能为空");
                }
                if(getPassword.length() == 0)
                {
                    mPassword.setErrorEnabled(true);
                    mPassword.setError("密码不能为空");
                }

            }
        });

        Button mRegist = (Button)findViewById(R.id.button_Regist);
        mRegist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int msg;
                ChoiceRes = mGroup.getCheckedRadioButtonId();
                if (ChoiceRes==R.id.radioButton_Student) {
                    msg = R.string.refuseStudent;
                }
                else {
                    msg = R.string.refuseTeacher;
                }
                Snackbar.make(view,msg,Snackbar.LENGTH_LONG)
                        .setAction(R.string.confirm, new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                Toast.makeText(getApplication(),"Snackerbar的确定按钮被点击了",Toast.LENGTH_LONG).show();
                            }
                        })
                        .setActionTextColor(getResources().getColor(R.color.colorPrimary))
                        .setDuration(5000)
                        .show();
            }
        });
    }
}

