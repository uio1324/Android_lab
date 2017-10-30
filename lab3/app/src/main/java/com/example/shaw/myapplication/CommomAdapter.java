package com.example.shaw.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;
import java.util.Map;

/**
 * Created by Shaw on 2017/10/23.
 */

public abstract class CommomAdapter extends RecyclerView.Adapter<ViewHolder> {
    private Context mContext;
    private int mlayoutId;
    protected List <Map<String, Object> > mDatas;
    private LayoutInflater mInflater;
    private OnItemClickListener monItemClickListener = null;
    public CommomAdapter(Context context, int layoutId, List <Map<String, Object>> datas)
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

}
