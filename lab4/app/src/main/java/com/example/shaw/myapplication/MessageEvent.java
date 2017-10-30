package com.example.shaw.myapplication;

/**
 * Created by Shaw on 2017/10/30.
 */

public class MessageEvent {
    private String mitem;
    private String mprice;
    public MessageEvent(String item, String price)
    {
        super();
        mitem = item;
        mprice = price;
    }
    public String getItem(){
        return mitem;
    }
    public String getPrice(){
        return mprice;
    }
}
