package com.example.demochatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.demochatapp.adapter.messadapter;
import com.example.demochatapp.database.Databases;
import com.example.demochatapp.model.danhsachmess;
import com.example.demochatapp.model.sendmess;
import com.example.demochatapp.model.user;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;

public class Activitymessager extends AppCompatActivity {
ListView listView;
EditText input;
FloatingActionButton btnsend;
String nguoigui;
String nguoinhan;
String namenguoinhan;
Databases databases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);
        nguoigui=getIntent().getStringExtra("nguoigui");
        nguoinhan=getIntent().getStringExtra("nguoinhan");
        namenguoinhan =getIntent().getStringExtra("tennguoinhan");

        setactionbar();
        getid();
        event();
        addmess();


    }

    private void setactionbar() {
        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);

        actionBar.setTitle("   "+namenguoinhan);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.deletemess:
                deletemess(nguoinhan);
                onBackPressed();
                break;



            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletemess(String nguoimuonxoa) {
        databases=new Databases(this,"pro.sqlite",null,1);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
        if(!mDatabase.get().isComplete()) {

            databases.querydata(" DELETE FROM message WHERE  ( nguoigui = '" + nguoigui + "' and nguoinhan = '" + nguoinhan + "' ) or ( nguoinhan = '" + nguoigui + "' and nguoigui = '" + nguoinhan + "' )  ");
            mDatabase.orderByChild("nguoigui").equalTo(nguoigui).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot mess : snapshot.getChildren()) {
                        sendmess u1 = (sendmess) mess.getValue(sendmess.class);
                        if (u1.nguoinhan.equals(nguoinhan)) {
                            mess.getRef().removeValue();
                        }



                    }
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });
            mDatabase.orderByChild("nguoigui").equalTo(nguoinhan).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                    for (DataSnapshot mess: snapshot.getChildren()) {
                        sendmess u1= (sendmess) mess.getValue( sendmess.class);
                        if(u1.nguoinhan.equals(nguoigui)){
                            mess.getRef().removeValue();
                        }

                    }



                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    private void addmess() {
        databases=new Databases(this,"pro.sqlite",null,1);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
       final ArrayList<sendmess> mess=new ArrayList<>();
        final messadapter adapter;
        adapter=new com.example.demochatapp.adapter.messadapter(Activitymessager.this,R.layout.chat_item_right,mess,nguoigui);
        listView.setAdapter(adapter);
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                sendmess u1= (sendmess) snapshot.getValue( sendmess.class);

                if(u1.nguoigui.equals(nguoigui) &&u1.nguoinhan.equals(nguoinhan)){

                    /*mess.add(u1);
                    adapter.notifyDataSetChanged();
                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    listView.setStackFromBottom(true);*/
                    if( (u1.nguoigui.equals(nguoigui)&&u1.nguoinhan.equals(nguoinhan)) || (u1.nguoinhan.equals(nguoigui)&&u1.nguoigui.equals(nguoinhan))  ){
                        Cursor cursor = databases.getdata("select * from message where nguoigui = '"+u1.nguoigui+"'  and nguoinhan = '"+u1.nguoinhan+"'  and   tinnhan  ='"+u1.tinnhan+"'  and  thoigiangui = '"+u1.thoigiangui+"' ");

                        playAudio();
                        if(cursor.getCount()<=0){
                            databases.querydata(" insert into message values( '" +u1.nguoigui+"' , '"+u1.nguoinhan+"' , '" +u1.tinnhan+"' , '"+u1.thoigiangui+"') ");

                        }

                    }


                }
                else  if(u1.nguoinhan.equals(nguoigui) &&u1.nguoigui.equals(nguoinhan)){

                    /*mess.add(u1);
                    adapter.notifyDataSetChanged();
                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    listView.setStackFromBottom(true);*/
                    if( (u1.nguoigui.equals(nguoigui)&&u1.nguoinhan.equals(nguoinhan)) || (u1.nguoinhan.equals(nguoigui)&&u1.nguoigui.equals(nguoinhan))  ){
                        Cursor cursor = databases.getdata("select * from message where nguoigui = '"+u1.nguoigui+"'  and nguoinhan = '"+u1.nguoinhan+"'  and   tinnhan  ='"+u1.tinnhan+"'  and  thoigiangui = '"+u1.thoigiangui+"' ");
                        playAudio();
                        if(cursor.getCount()<=0){

                            databases.querydata(" insert into message values( '" +u1.nguoigui+"' , '"+u1.nguoinhan+"' , '" +u1.tinnhan+"' , '"+u1.thoigiangui+"') ");

                        }

                    }
                }

                mess.clear();
                Cursor informesss =databases.getdata("select * from message where ( nguoigui = '"+nguoigui+"' and nguoinhan = '"+nguoinhan+"' ) or " +
                        " (  nguoinhan = '"+nguoigui+"' and nguoigui = '"+nguoinhan+"' )  " + " order by thoigiangui    ");
                while (informesss.moveToNext()){
                    mess.add(new sendmess(informesss.getString(0),informesss.getString(1),informesss.getString(2),informesss.getLong(3)));
                    adapter.notifyDataSetChanged();
                    listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                    listView.setStackFromBottom(true);
                }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                sendmess u1= (sendmess) snapshot.getValue( sendmess.class);
                int x=0;
                ArrayList<sendmess> mess1=mess;
                for (sendmess s:mess1) {
                    if(s.nguoigui.equals(u1.nguoigui) &&s.nguoinhan.equals(u1.nguoinhan)&& s.thoigiangui == u1.thoigiangui){
                        mess.remove(x);
                        adapter.notifyDataSetChanged();
                        break;
                    }
                    x++;
                }





            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //load tin nhan tu sqllite khi ko co internet

        /*if(mDatabase.get().isCanceled()){*/
        mess.clear();
            Cursor informesss =databases.getdata("select * from message where ( nguoigui = '"+nguoigui+"' and nguoinhan = '"+nguoinhan+"' ) or " +
                    " (  nguoinhan = '"+nguoigui+"' and nguoigui = '"+nguoinhan+"' )  " + " order by thoigiangui    ");
            while (informesss.moveToNext()){
                mess.add(new sendmess(informesss.getString(0),informesss.getString(1),informesss.getString(2),informesss.getLong(3)));
                adapter.notifyDataSetChanged();
                listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                listView.setStackFromBottom(true);
            }
      /*  }*/


    }
    private void playAudio() {
        try{

            AssetFileDescriptor assetFileDescriptor =

                    getAssets().openFd("musics/ting.mp3");

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(
                    assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(),
                    assetFileDescriptor.getLength());
            assetFileDescriptor.close();
            mediaPlayer.prepare();
            mediaPlayer.start();
        }catch (Exception e){
            Log.e("Error: " , e.toString());
        }
    }

    private void event() {



        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(input.getText().length()>0){
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
                    sendmess mess=new sendmess(nguoigui,nguoinhan,input.getText().toString(), new Date().getTime());
                    String userId = mDatabase.push().getKey();
                    mDatabase.child(userId).setValue(mess);
                    input.setText("");

                    /*them nguoi nhan tin vao danh sach */
                    DatabaseReference mDatabasedsmessto = FirebaseDatabase.getInstance().getReference(nguoigui);
                    mDatabasedsmessto.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int x=0;
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                danhsachmess uuu= (danhsachmess) snapshot1.getValue( danhsachmess.class);
                                if(uuu.nguoinhan.equals(nguoinhan))
                                    x++;

                            }
                            if(x==0){
                                danhsachmess ds=new danhsachmess(nguoinhan);
                                String userId = mDatabasedsmessto.push().getKey();
                                mDatabasedsmessto.child(userId).setValue(ds);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    /*them nguoi gui tin vao danh sach */
                    DatabaseReference mDatabasedsmessfrom = FirebaseDatabase.getInstance().getReference(nguoinhan);
                    mDatabasedsmessfrom.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int x=0;
                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                danhsachmess uuu= (danhsachmess) snapshot1.getValue( danhsachmess.class);
                                if(uuu.nguoinhan.equals(nguoigui))
                                    x++;

                            }
                            if(x==0){
                                danhsachmess ds=new danhsachmess(nguoigui);
                                String userId = mDatabasedsmessfrom.push().getKey();
                                mDatabasedsmessfrom.child(userId).setValue(ds);

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }
                else {

                    input.setError("vui lòng nhập tin nhắn");
                }
            }
        });

    }

    public  void deletetinnhan(String ng,String nn,long thoigiangui) {


        databases = new Databases(this, "pro.sqlite", null, 1);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("message");
        if (!mDatabase.get().isComplete()) {

            databases.querydata(" DELETE FROM message WHERE   nguoigui = '" + ng + "' and nguoinhan = '" + nn + "' and thoigiangui = " + thoigiangui );
            mDatabase.orderByChild("nguoigui").equalTo(nguoigui).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                    for (DataSnapshot mess : snapshot.getChildren()) {
                        sendmess u1 = (sendmess) mess.getValue(sendmess.class);
                        if (u1.nguoinhan.equals(nn) && u1.nguoigui.equals(ng) && u1.thoigiangui == thoigiangui   ) {
                            mess.getRef().removeValue();
                            Toast.makeText(Activitymessager.this, "da xoa", Toast.LENGTH_SHORT).show();
                        }



                    }


                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });


        }



    }


    private void getid() {
        listView=findViewById(R.id.listnguoinhan);
        input=findViewById(R.id.inputmess);
        btnsend=findViewById(R.id.sendmess);
    }
}