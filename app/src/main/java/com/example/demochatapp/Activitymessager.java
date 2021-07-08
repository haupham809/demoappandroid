package com.example.demochatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.demochatapp.adapter.messadapter;
import com.example.demochatapp.model.danhsachmess;
import com.example.demochatapp.model.sendmess;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class Activitymessager extends AppCompatActivity {
ListView listView;
EditText input;
FloatingActionButton btnsend;
String nguoigui;
String nguoinhan;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messager);
        nguoigui=getIntent().getStringExtra("nguoigui");
        nguoinhan=getIntent().getStringExtra("nguoinhan");
        getid();
        event();
        addmess();


    }

    private void addmess() {
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
                    mess.add(u1);
                    adapter.notifyDataSetChanged();

                }
                else  if(u1.nguoinhan.equals(nguoigui) &&u1.nguoigui.equals(nguoinhan)){
                    mess.add(u1);
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


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

    private void getid() {
        listView=findViewById(R.id.listnguoinhan);
        input=findViewById(R.id.inputmess);
        btnsend=findViewById(R.id.sendmess);
    }
}