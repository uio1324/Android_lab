package com.example.shaw.lab9.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shaw.lab9.model.Github;
import com.example.shaw.lab9.model.Reponsitory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shaw on 2017/10/23.
 */



public abstract class CommonAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private int mlayoutId;
    protected List <Map<String, Object> > mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener monItemClickListener = null;
    public CommonAdapter(Context context, int layoutId, List <Map<String, Object>> datas)
    {
        mContext = context;
        mlayoutId = layoutId;
        mDatas = datas;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        ViewHolder viewHolder = ViewHolder.get(mContext, parent, mlayoutId);
        return viewHolder;
    }

    public abstract void convert(ViewHolder holder, Map<String, Object> data);

    @Override
    public void onBindViewHolder(final ViewHolder holder, int  position)
    {
        convert(holder, mDatas.get(position));
        if(monItemClickListener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    monItemClickListener.onClick(holder.getAdapterPosition());
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    monItemClickListener.onLongClick(holder.getAdapterPosition());
                    return false;
                }
            });
        }
    }

    @Override
    public  int getItemCount()
    {
        return mDatas.size();
    }
    public interface OnItemClickListener
    {
        void onClick(int position);
        void onLongClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.monItemClickListener = onItemClickListener;
    }

    //需要编写两种情况的addData，一种为了主界面，一种为了详情界面
    public void addData(Github github){
        HashMap<String, Object> git = new HashMap<>();
        git.put("name", github.getLogin());
        git.put("id", "id:" + github.getId());
        git.put("blog", "blog:" + github.getBlog());
        mDatas.add(git);
        notifyDataSetChanged();
    }

    public void addData(Reponsitory reponsitory){
        HashMap<String, Object> repon = new HashMap<>();
        repon.put("name", reponsitory.getName());
        repon.put("language", checkEmpty(reponsitory.getLanguage(), 20));
        repon.put("hint", checkEmpty(reponsitory.getHint(), 20));
        mDatas.add(repon);
        notifyDataSetChanged();
    }


    public String getData(int position, String tag){
        return mDatas.get(position).get(tag).toString();
    }

    public void removeData(int position){
        mDatas.remove(position);
        notifyDataSetChanged();
    }

    public void clearAllData(){
        mDatas.removeAll(mDatas);
        notifyDataSetChanged();
    }

    //为了检查有几项可能为空的闪退情况
    private String checkEmpty(String str, int limit){
        String temp = str;
        if(str == null){
            temp = "";
        }
        else if (temp.length() > limit){
            temp = temp.substring(0,limit) + "...";
        }
        return temp;
    }

}