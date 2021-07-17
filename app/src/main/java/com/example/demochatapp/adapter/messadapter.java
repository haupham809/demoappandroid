package com.example.demochatapp.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demochatapp.Activitymessager;
import com.example.demochatapp.MainActivity;
import com.example.demochatapp.R;
import com.example.demochatapp.model.sendmess;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class messadapter extends BaseAdapter {
    public messadapter(Activitymessager context, int item_layout, List<sendmess> mess, String nguoigui) {
        this.context = context;
        this.item_layout = item_layout;
        this.mess = mess;
        this.nguoigui = nguoigui;
    }

    Activitymessager context;
    int item_layout;
    List<sendmess> mess;
    String nguoigui;



    @Override
    public int getCount() {
        return mess.size();
    }

    @Override
    public Object getItem(int i) {
        return mess.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;

            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            sendmess me1 = mess.get(i);
            if(me1.nguoigui.equals(nguoigui)){
                view = inflater.inflate(R.layout.chat_item_right,null);
            }
            else if(me1.nguoinhan.equals(nguoigui)){
                view = inflater.inflate(R.layout.chat_item_left,null);
            }


            holder.txtnoidung = view.findViewById(R.id.txtnoidung);
            holder.txtthoigian=view.findViewById(R.id.thoigiangui);
            holder.itemtinnhan=view.findViewById(R.id.itemtinnhan);
            view.setTag(holder);

        sendmess p = mess.get(i);
        Date date=new Date(p.thoigiangui);
        final DateFormat df2 = new SimpleDateFormat("HH:mm");

        // Date ==> String.
        String dateString2 = df2.format(date);
        holder.txtthoigian.setText(dateString2);
        holder.txtnoidung.setText(p.tinnhan);


        holder.itemtinnhan.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                context.deletetinnhan(p.nguoigui,p.nguoinhan,p.thoigiangui);
                return false;
            }
        });





        return view;
    }

    public static class ViewHolder{
        LinearLayout itemtinnhan;
        TextView txtnoidung,txtthoigian;

    }
}
