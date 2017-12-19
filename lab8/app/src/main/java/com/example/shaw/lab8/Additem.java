package com.example.shaw.lab8;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Additem extends AppCompatActivity {
    EditText name, birthday, gift;
    Button save;
    private myDB ContactDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additem);
        name = findViewById(R.id.EditNameEditText);
        birthday = findViewById(R.id.EditBirthdayEditText);
        gift = findViewById(R.id.EditGiftEditText);
        save = findViewById(R.id.SaveButton);
        ContactDBHelper = new myDB(Additem.this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current_id;
                String current_name = name.getText().toString();
                String current_birthday = birthday.getText().toString();
                String current_gift = gift.getText().toString();
                if(ContactDBHelper.is_name_exist(current_name)){
                    Toast.makeText(getApplication(), "已存在该名字的条目", Toast.LENGTH_LONG).show();
                }
                else{
                    current_id = ContactDBHelper.insert(current_name, current_birthday, current_gift);
                    Intent intent = new Intent(Additem.this, MainActivity.class);
                    intent.putExtra("id", current_id);
                    intent.putExtra("name", current_name);
                    intent.putExtra("birthday", current_birthday);
                    intent.putExtra("gift", current_gift);
                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });





    }
}
