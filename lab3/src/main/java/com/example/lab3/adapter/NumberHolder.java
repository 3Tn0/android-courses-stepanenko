package com.example.lab3.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.lab3.R;

public class NumberHolder extends RecyclerView.ViewHolder  {

    public final TextView number;

    public NumberHolder(ViewGroup parent) {
        super(LayoutInflater.from(parent.getContext()).inflate(R.layout.lab3_item_number, parent, false));
        number = itemView.findViewById(R.id.number);
    }
    public void bind(String studentIndex) {
        number.setText(studentIndex);
    }
}
