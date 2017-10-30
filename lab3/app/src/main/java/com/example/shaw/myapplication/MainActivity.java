package com.example.shaw.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.OvershootInLeftAnimator;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity {
    List<Map<String, Object>> itemList = new ArrayList<>();
    List<Map<String, Object>> data = new ArrayList<>();
    SimpleAdapter simpleAdapter;
    //用于初始化商品界面列表
    private void initItemList()
    {
        String [] itemName = new String[]
                {"Enchated Forest", "Arla Milk",
                        "Devondale Milk", "Kindle Oasis", "waitrose 早餐麦片",
                        "Mcvitie's cookies", "Ferrero Rocher","Maltesers","Lindt","Borggreve"};

        for(int i = 0; i < 10; i++){
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("item",itemName[i]);
            temp.put("firstLetter",itemName[i].substring(0,1));
            itemList.add(temp);
        }
    }
    private void initShoppingList()
    {
        String[] shoppingList = new String[] {"购物车"};
        String[] itemList_firstLetter = new String[] {"*"};
        String[] price = new String[] {"价格"};
        for(int i = 0; i < 1; i++){
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("item",shoppingList[i]);
            temp.put("firstLetter",itemList_firstLetter[i]);
            temp.put("price", price[i]);
            data.add(temp);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //商品列表操作
        initItemList();
        final RecyclerView itemRecycleView = (RecyclerView) findViewById(R.id.itemListView);
        itemRecycleView.setLayoutManager(new LinearLayoutManager(this));
        final CommomAdapter itemListAdapter = new CommomAdapter(this, R.layout.itemall,itemList) {
            @Override
            public void convert(ViewHolder holder, Map<String, Object> data) {
                TextView firstLetter = holder.getView(R.id.firstLetter);
                TextView item = holder.getView(R.id.item);
                firstLetter.setText(data.get("firstLetter").toString());
                item.setText(data.get("item").toString());
            }
        };



        //设置动画效果

        ScaleInAnimationAdapter animationAdapter = new ScaleInAnimationAdapter(itemListAdapter);
        animationAdapter.setDuration(1000);
        itemRecycleView.setAdapter(animationAdapter);
        itemRecycleView.setItemAnimator(new OvershootInLeftAnimator());
//        itemRecycleView.setAdapter(itemListAdapter);


        //设置商品列表监听事件
        itemListAdapter.setOnItemClickListener(new CommomAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                String temp = itemList.get(position).get("item").toString();
                Intent goIntent = new Intent(MainActivity.this, ItemDetail.class);
                goIntent.putExtra("itemName",temp);
                startActivityForResult(goIntent,1);
            }

            @Override
            public void onLongClick(int position) {
                String temp = Integer.toString(position);
                itemListAdapter.notifyItemRemoved(position);
                itemList.remove(position);
                Toast.makeText(MainActivity.this, "移除第"+temp+"个商品", LENGTH_SHORT).show();
            }
        });

        //购物车初始化
        initShoppingList();
        final ListView listView = (ListView) findViewById(R.id.shoppingListView);
        simpleAdapter = new SimpleAdapter(this, data, R.layout.shoppingall,
                new String[] {"firstLetter","item", "price"}, new int[]{
                R.id.shoppinFfirstLetterTextView,
                R.id.shoppingItemTextView,
                R.id.shoppingPriceTextView});
        listView.setAdapter(simpleAdapter);
        //购物车对话框初始化
        final AlertDialog.Builder list_alertdialog = new AlertDialog.Builder(this);
        list_alertdialog.setTitle("移除商品")
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int x = position;
                if(x != 0)
                {
                    list_alertdialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            data.remove(x);
                            simpleAdapter.notifyDataSetChanged();
                        }
                    }).setMessage("从购物车移除"+data.get(x).get("item").toString()).create().show();
                }
                return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i != 0) {
                    String chose_name = data.get(i).get("item").toString();
                    Intent intent = new Intent(MainActivity.this, ItemDetail.class);
                    intent.putExtra("itemName", chose_name);
                    startActivityForResult(intent, 1);
                }
            }
        });




        //商品界面和购物车切换浮标
        final FloatingActionButton Item_to_Shopping = (FloatingActionButton) findViewById(R.id.fab);
        Item_to_Shopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listView.getVisibility() == View.VISIBLE)
                {
                    listView.setVisibility(View.GONE);
                    Item_to_Shopping.setImageResource(R.drawable.shoplist);
                    itemRecycleView.setVisibility(View.VISIBLE);
                }
                else if(itemRecycleView.getVisibility() == View.VISIBLE)
                {
                    listView.setVisibility(View.VISIBLE);
                    Item_to_Shopping.setImageResource(R.drawable.mainpage);
                    itemRecycleView.setVisibility(View.GONE);
                }
            }
        });





    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data1)
    {
        if(requestCode == 1)
        {
            if(resultCode == RESULT_OK)
            {
                String itemName = data1.getStringExtra("itemName");
                String price = data1.getStringExtra("price");
                if(itemName != null && price != null)
                {
                    Map<String,Object> temp = new LinkedHashMap<>();
                    temp.put("firstLetter", itemName.substring(0,1));
                    temp.put("item", itemName);
                    temp.put("price", price);
                    data.add(temp);
                    simpleAdapter.notifyDataSetChanged();
                }
            }
        }

    }


}
