package com.example.minidouying;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * author : neo
 * time   : 2019/07/09
 * desc   :
 */
public class R_Adapter extends RecyclerView.Adapter {

    private List<R_Data> mList;

    @NonNull @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View root = LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.inmain_resourses_item_activity, viewGroup, false);
        return new R_ViewHolder(root);
    }

    @Override public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        ((R_ViewHolder)viewHolder).bind(mList.get(position));
    }

    @Override public int getItemCount() {
        return mList.size();
    }

    public void setData(List<R_Data> list) {
        mList = list;
    }
}

