package com.example.shaw.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shaw on 2017/10/24.
 */

public class ItemDetail extends AppCompatActivity {
    List<Map<String, Object>> ItemInformation = new ArrayList<>();
    List<Map<String, Object>> someOption = new ArrayList<>();
    int[] imageID = {R.drawable.enchatedforest, R.drawable.arla, R.drawable.devondale, R.drawable.kindle,
            R.drawable.waitrose, R.drawable.mcvitie, R.drawable.ferrero, R.drawable.maltesers,R.drawable.lindt,
            R.drawable.borggreve};
    String toGetExtras = null;
    String toReturnItemName = null;
    String toReturnPrice = null;

    int mID = -1;
    void init()
    {
        String[] itemName = new String[]{"Enchated Forest", "Arla Milk",
                "Devondale Milk", "Kindle Oasis", "waitrose 早餐麦片",
                "Mcvitie's cookies", "Ferrero Rocher","Maltesers","Lindt","Borggreve"};
        String[] price = new String[]{"¥ 5.00", "¥ 59.00", "¥ 79.00",
                "¥ 2399.00", "¥ 179.00",
                "¥ 14.90", "¥ 132.59","¥ 141.43","¥ 139.43","¥ 28.90"};
        String[] some = new String[]{"作者","产地","产地","版本","重量","产地","重量",
                "重量","重量","重量"};
        String[] thing = new String[]{"Johanna Basford","德国","澳大利亚","8GB","2Kg","英国",
                "300g","118g","249g","640g"};
        String[] options = {"一键下单","分享商品","不感兴趣","查看更多商品促销信息"};
        for(int i = 0; i < 10; i++)
        {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("itemName", itemName[i]);
            temp.put("price", price[i]);
            temp.put("some",some[i]);
            temp.put("thing", thing[i]);
            ItemInformation.add(temp);
        }
        for(int i = 0; i < 4; i++)
        {
            Map<String, Object> temp1 = new LinkedHashMap<>();
            temp1.put("options", options[i]);
            someOption.add(temp1);
        }
    }

    @Override
    protected void onCreate(Bundle savaInstanceState)
    {
        super.onCreate(savaInstanceState);
        setContentView(R.layout.item_infomation);
        init();

        Bundle mextras = getIntent().getExtras();
        if(mextras != null)
        {
            toGetExtras = mextras.getString("itemName");
            for(int i = 0; i < ItemInformation.size(); i++)
                if (ItemInformation.get(i).get("itemName").equals(toGetExtras))
                    mID = i;
        }

        //需要修改的UI控件
        ImageView itemImage = (ImageView) findViewById(R.id.itemImageView);
        TextView item = (TextView) findViewById(R.id.itemNameTextView) ;
        TextView price = (TextView) findViewById(R.id.priceTextView);
        TextView weight = (TextView) findViewById(R.id.weighTextView);
        final ImageButton Star = (ImageButton) findViewById(R.id.StarImageButton);
        ImageButton back = (ImageButton) findViewById(R.id.backImageButton);
        ImageButton addToShopping = (ImageButton) findViewById(R.id.addImageButton);

        //更改商品名称
        item.setText(ItemInformation.get(mID).get("itemName").toString());
        //更改图片
        itemImage.setImageResource(imageID[mID]);
        //更改商品价格
        price.setText(ItemInformation.get(mID).get("price").toString());
        //更改商品信息
        weight.setText(ItemInformation.get(mID).get("some").toString()+" "+
                ItemInformation.get(mID).get("thing").toString());
        //设置星星按钮
        Star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Star.getTag().toString().equals("0"))
                {
                    Star.setImageResource(R.drawable.full_star);
                    Star.setTag("1");
                }
                else
                {
                    Star.setImageResource(R.drawable.empty_star);
                    Star.setTag("0");
                }
            }
        });
        //返回按钮
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backIntent = new Intent();
                backIntent.putExtra("itemName", toReturnItemName);
                backIntent.putExtra("price", toReturnPrice);
                setResult(RESULT_OK,backIntent);
                finish();
            }
        });
        //设置购物车按钮
        addToShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toReturnItemName = ItemInformation.get(mID).get("itemName").toString();
                toReturnPrice = ItemInformation.get(mID).get("price").toString();
                Toast.makeText(ItemDetail.this, "商品已添加到购物车",Toast.LENGTH_SHORT).show();
            }
        });
        //设置更多信息的列表
        ListView moreList = (ListView) findViewById(R.id.moreListView);
        SimpleAdapter msimpleAdapter = new SimpleAdapter(this, someOption, R.layout.more_option,
                new String[] {"options"}, new int[]{R.id.moreOptionTextView});
        moreList.setAdapter(msimpleAdapter);

    }

    //手机自带的返回按键设置，与返回按钮一样即可
    @Override
    public void onBackPressed(){
        Intent backIntent = new Intent();
        backIntent.putExtra("itemName", toReturnItemName);
        backIntent.putExtra("price", toReturnPrice);
        setResult(RESULT_OK,backIntent);
        finish();
    }




}
