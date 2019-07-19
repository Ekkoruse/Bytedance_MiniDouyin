package com.example.minidouying;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * author : neo
 * time   : 2019/07/09
 * desc   :
 */
public class R_ViewHolder extends RecyclerView.ViewHolder {

    TextView mTextView;
    ImageView image_1,image_2;

    public R_ViewHolder(@NonNull View itemView) {
        super(itemView);
        mTextView = itemView.findViewById(R.id.tv_data);
        image_1 = itemView.findViewById(R.id.res_image_1);
    }

    public void bind(R_Data data) {
        mTextView.setText(data.getI()+"");
       // image_1.setImageURI();
    }
}

