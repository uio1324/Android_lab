package com.example.shaw.lab8;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    List<Map<String, Object>> ContactList = new ArrayList<>();
    ListView ContactListView;
    myDB ContactDBHelper;
    SimpleAdapter adapter;
    AlertDialog.Builder deletingDialog, updatingDialog;
    TextView name, phone;
    EditText birth, gift;
    Button add;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //创建数据库操作
        ContactDBHelper = new myDB(MainActivity.this);
        ContactList = ContactDBHelper.query_all();//使用数据库中的内容进行初始化
        ContactListView = findViewById(R.id.ListListView);
        adapter = new SimpleAdapter(this, ContactList, R.layout.item, new String[]
                {"name", "birthday", "gift"}, new int[]{R.id.itemNameTextView, R.id.itemBirthdayTextView, R.id.itemGiftTextView});
        ContactListView.setAdapter(adapter);

        //创建列表点击操作
        updatingDialog = new AlertDialog.Builder(MainActivity.this);
        ContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                final int current_id = (int)ContactList.get(pos).get("id");
                Map<String, Object> current_item = ContactDBHelper.query(current_id);//执行单条查询
                final String current_name = (String)current_item.get("name");
                LayoutInflater factor = LayoutInflater.from(MainActivity.this);//自定义对话框
                View view_in = factor.inflate(R.layout.dialoglayout, null);
                name = view_in.findViewById(R.id.dialogNameTextView);
                birth = view_in.findViewById(R.id.dialogBirthdayEditText);
                gift = view_in.findViewById(R.id.dialogGiftEditText);
                phone = view_in.findViewById(R.id.dialogPhoneTextView);

                name.setText(current_name);
                phone.setText(getPhoneNumber(current_name));
                //定义两个按钮
                updatingDialog.setPositiveButton("保存修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ContactDBHelper.update(current_id, current_name, birth.getText().toString(), gift.getText().toString());
                        Map<String, Object> temp = ContactList.get(pos);
                        temp.put("birthday", birth.getText().toString());
                        temp.put("gift", gift.getText().toString());
                        ContactList.set(pos, temp);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("放弃", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
                updatingDialog.setView(view_in).show();//最后一步定义对话框使其生效
            }
        });

        //创建列表长按操作
        deletingDialog = new AlertDialog.Builder(this);
        deletingDialog.setTitle("确认删除？");
        ContactListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;
                deletingDialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        final int current_id = (int)ContactList.get(pos).get("id");
                        ContactDBHelper.delete(current_id);
                        ContactList.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                }).show();
                return true;
            }
        });

        //创建按下按钮操作
        add = findViewById(R.id.AddButton);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Additem.class);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        Map<String, Object> temp = new HashMap<>();
        if(resultCode!=0){
            temp.put("id", data.getIntExtra("id", -1));
            temp.put("name", data.getStringExtra("name"));
            temp.put("birthday", data.getStringExtra("birthday"));
            temp.put("gift", data.getStringExtra("gift"));
            ContactList.add(temp);
            adapter.notifyDataSetChanged();
        }

    }

    private String getPhoneNumber(String name){
        String number = "";
        int isHas = 0;
        Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null, ContactsContract.PhoneLookup.DISPLAY_NAME+"=?",new String[]{name},null);
        if(cursor.moveToFirst()){
            isHas = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));
        }
        if(isHas == 1){
            int ContactID = cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts._ID));
            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + ContactID,
                    null, null);
            while(phone.moveToNext()) {
                number += phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) + " ";
            }
        }else{
            number = "无";
        }
        return number;
    }
}
