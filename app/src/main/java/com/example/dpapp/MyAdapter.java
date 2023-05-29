package com.example.dpapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends ArrayAdapter<String> {

    private ArrayList<String> arrayList;
    private Context context;

    public MyAdapter(Context context, ArrayList<String> arrayList) {
        super(context, 0, arrayList);
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        }

        String item = arrayList.get(position);

        TextView textView = convertView.findViewById(R.id.text_view);
        textView.setText(item);

        return convertView;
    }
}
