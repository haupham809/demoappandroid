package com.example.demochatapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.demochatapp.MainActivity;
import com.example.demochatapp.R;
import com.example.demochatapp.model.user;

import java.util.List;

public class searchadapter extends BaseAdapter {


    public searchadapter(MainActivity context, int item_layout, List<user> users) {
        this.context = context;
        this.item_layout = item_layout;
        this.users = users;
    }

    MainActivity context;
    int item_layout;
    List<user> users;

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return users.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

        if(view == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.itemsearch, null);
            holder.hoten = view.findViewById(R.id.hoten);
            holder.email = view.findViewById(R.id.email);
            holder.add = view.findViewById(R.id.btnadd);
            view.setTag(holder);
        }
        else{
            holder = (ViewHolder)view.getTag();
        }
        user p = users.get(i);
        holder.hoten.setText(p.name);
        holder.email.setText(p.email);

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.joinmess(p.username);
            }
        });
        return view;

    }


    public static class ViewHolder{
        TextView hoten;
        TextView email;
        ImageButton add;

    }
}
