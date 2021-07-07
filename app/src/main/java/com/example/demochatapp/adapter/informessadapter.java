package com.example.demochatapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.demochatapp.R;
import com.example.demochatapp.model.informess;
import com.example.demochatapp.model.sendmess;
import com.example.demochatapp.model.user;

import java.util.List;

public class informessadapter extends BaseAdapter {

    public informessadapter(Activity context, int item_layout, List<informess> informesses) {
        this.context = context;
        this.item_layout = item_layout;
        this.informesses = informesses;
    }

    Activity context;
    int item_layout;
    List<informess> informesses;

    @Override
    public int getCount() {
        return informesses.size();
    }

    @Override
    public Object getItem(int position) {
        return informesses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if(convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.iteminforsendmess, null);
            holder.nguoinhan = convertView.findViewById(R.id.name);
            holder.email = convertView.findViewById(R.id.email);
            holder.tinnhan = convertView.findViewById(R.id.tinnhandagui);
            holder.nguoigui = convertView.findViewById(R.id.nguoigui);
            holder.thoigiangui = convertView.findViewById(R.id.thoigiangui);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder)convertView.getTag();
        }
        informess p = informesses.get(position);
        holder.nguoinhan.setText(p.name);
        holder.email.setText(p.email);
        holder.tinnhan.setText(p.tinnhanmoinhat);
        holder.nguoigui.setText(p.nguoigui + ":");
        holder.thoigiangui.setText(String.valueOf(p.thoigiangui));

        /*holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                context.joinmess(p.username);
            }
        });*/
        return convertView;
    }
    public static class ViewHolder{
        TextView nguoinhan;
        TextView email;
        TextView tinnhan;
        TextView nguoigui;
        TextView thoigiangui;

    }
}
