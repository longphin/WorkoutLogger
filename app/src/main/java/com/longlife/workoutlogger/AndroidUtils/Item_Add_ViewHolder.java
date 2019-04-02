/*
 * Created by Longphi Nguyen on 3/28/19 9:57 AM.
 * Copyright (c) 2019. All rights reserved.
 * Last modified 3/28/19 9:57 AM.
 */

package com.longlife.workoutlogger.AndroidUtils;

import android.view.View;
import android.widget.ImageButton;

import com.longlife.workoutlogger.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class Item_Add_ViewHolder extends RecyclerView.ViewHolder {
    private ImageButton addButton;

    public Item_Add_ViewHolder(@NonNull View itemView) {
        super(itemView);

        addButton = itemView.findViewById(R.id.btn_addItem);
    }

    public ImageButton getAddButton() {
        return addButton;
    }
}
