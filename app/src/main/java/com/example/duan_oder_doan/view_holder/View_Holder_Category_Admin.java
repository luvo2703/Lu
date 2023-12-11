package com.example.duan_oder_doan.view_holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.duan_oder_doan.R;

public class View_Holder_Category_Admin extends RecyclerView.ViewHolder {
    public ImageView imgCategory;
    public TextView tvNameCategory;
    public ImageView imgUpdate;

    public View_Holder_Category_Admin(@NonNull View itemView) {
        super(itemView);
        imgCategory = (ImageView) itemView.findViewById(R.id.img_category);
        tvNameCategory = (TextView) itemView.findViewById(R.id.tv_nameCategory);
        imgUpdate = (ImageView) itemView.findViewById(R.id.img_update);
    }
}
