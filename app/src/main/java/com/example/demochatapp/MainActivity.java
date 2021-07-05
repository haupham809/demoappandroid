package com.example.demochatapp;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.demochatapp.adapter.searchadapter;
import com.example.demochatapp.database.Databases;
import com.example.demochatapp.model.danhsachmess;
import com.example.demochatapp.model.sendmess;
import com.example.demochatapp.model.user;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    private String[] tabs = {"message", "search", "account"};
    private TabLayout tabLayout;
    private LinearLayout linearlayout;
    public static int[] resourceIds = {
            R.layout.activity_messager
            ,R.layout.activity_search
            ,R.layout.activity_account
    };


    ListView listViewtimkiem ,listViewinfor;
    EditText noidung;
    Button btntiemkiem ,btnlogout;
    TextView hotenaccount,emailaccount;
    user account;
    Databases databases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         account= (user) getIntent().getSerializableExtra("account");
        getid();
        event();


    }

    private void event() {

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

               if(tabLayout.getSelectedTabPosition()==0){
                   if(account.username==null){
                       startActivity(new Intent(MainActivity.this
                               ,Activitylogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

                   }
                   linearlayout.removeAllViews();
                   View view = getLayoutInflater().inflate(R.layout.activity_infor,null);
                   linearlayout.addView(view);
                   listViewinfor=findViewById(R.id.listViewinfor);
                   eventnguoinhan();
               }
                else if (tabLayout.getSelectedTabPosition()==1){
                   if(account.username==null){
                       startActivity(new Intent(MainActivity.this
                               ,Activitylogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

                   }
                    linearlayout.removeAllViews();
                    View view = getLayoutInflater().inflate(R.layout.activity_search,null);
                    linearlayout.addView(view);
                   listViewtimkiem=findViewById(R.id.listtimkiem);
                   noidung=findViewById(R.id.noidungtiemkiem);
                   btntiemkiem=findViewById(R.id.btntiemkiem);
                    eventtiemkiem();

                }
               else if (tabLayout.getSelectedTabPosition()==2){
                   if(account.username==null){
                       startActivity(new Intent(MainActivity.this
                               ,Activitylogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

                   }
                   linearlayout.removeAllViews();
                    View view = getLayoutInflater().inflate(R.layout.activity_account,null);
                    linearlayout.addView(view);
                   hotenaccount = findViewById(R.id.email);
                   emailaccount =findViewById(R.id.hoten);
                   btnlogout=findViewById(R.id.btnlogout);
                   hotenaccount.setText("Họ tên : "+account.name);
                   emailaccount.setText("Email : "+account.email);
                   eventaccount();
                }


            }



            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }

    private void eventaccount() {
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                account.username=null;
                account.email=null;
                account.pass=null;
                account.name=null;
                startActivity(new Intent(MainActivity.this
                        ,Activitylogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });
    }


    private void eventnguoinhan() {

        final List<user> listUsers=new ArrayList<>();
        final  searchadapter adapter=new  searchadapter(MainActivity.this,R.layout.itemsearch,listUsers);
        listViewinfor.setAdapter(adapter);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(account.username);
                    mDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            danhsachmess u1= (danhsachmess) snapshot.getValue(danhsachmess.class);
                            DatabaseReference mDatabaseuser = FirebaseDatabase.getInstance().getReference("users");
                            mDatabaseuser.orderByChild("username").equalTo(u1.nguoinhan).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    user u= (user) snapshot.getValue(user.class);
                                         listUsers.add(u);
                                        adapter.notifyDataSetChanged();

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

    private void eventtiemkiem() {

        prepareDB();
        getdata();


        btntiemkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(noidung.getText().length()<=0){
                    Toast.makeText(MainActivity.this,"Vui lòng nhập nội dung bạn muốn tìm",Toast.LENGTH_SHORT).show();
                }
                else {
                    final List<user> listUsers=new ArrayList<>();
                    final  searchadapter   adapter=new searchadapter(MainActivity.this,R.layout.itemsearch,listUsers);
                    listViewtimkiem.setAdapter(adapter);
                    listUsers.clear();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");


                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot snapshot1:snapshot.getChildren()){
                                user u1= (user) snapshot1.getValue( user.class);

                                if(u1.name.contains(noidung.getText().toString()) && !u1.username.equals(account.username)){
                                    listUsers.add(u1);
                                    Cursor cursor=databases.getdata("select * from user where email = '" +u1.email+
                                            "' and username = '"+u1.username+"' ");
                                    if(cursor.getCount()==0){
                                        Toast.makeText(MainActivity.this,"them vao database",Toast.LENGTH_SHORT).show();
                                        databases.querydata("insert into user values('"+u1.email+"' ,'"+u1.name+"' ,'"+u1.username+"')");
                                    }

                                    adapter.notifyDataSetChanged();

                                }

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    adapter.notifyDataSetChanged();
                }



            }
        });





    }



    public  void joinmess(String nguoinhan) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(account.username);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x=0;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    danhsachmess u1= (danhsachmess) snapshot1.getValue( danhsachmess.class);
                    if(u1.nguoinhan.equals(nguoinhan))
                        x++;

                }
                if(x==0){
                    danhsachmess ds=new danhsachmess(nguoinhan);
                    String userId = mDatabase.push().getKey();
                    mDatabase.child(userId).setValue(ds);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Intent intent=new Intent(MainActivity.this,Activitymessager.class);
        intent.putExtra("nguoigui",account.username);
        intent.putExtra("nguoinhan",nguoinhan);
        startActivity(intent);

    }


    private void getid() {
        tabLayout = (TabLayout) findViewById(R.id.tab);
        linearlayout = findViewById(R.id.lnlayout);
        tabLayout.addTab(tabLayout.newTab().setText(tabs[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabs[2]));
        View view = getLayoutInflater().inflate(R.layout.activity_infor,null);
        linearlayout.addView(view);
        listViewinfor=findViewById(R.id.listViewinfor);
        eventnguoinhan();



    }

    private void prepareDB() {
        databases=new Databases(this,"pro.sqlite",null,1);
        databases.querydata(" CREATE TABLE IF NOT EXISTS user(email VARCHAR(200) ," + "name VARCHAR(200) ,username VARCHAR(200))");

    }
    private void getdata() {

        final List<user> listsql=new ArrayList<>();
        final searchadapter   adaptersql=new searchadapter(MainActivity.this,R.layout.itemsearch,listsql);
        listViewtimkiem.setAdapter(adaptersql);
        databases=new Databases(this,"pro.sqlite",null ,1);
        listsql.add(account);
        Cursor cursor=databases.getdata("select * from user");
        listsql.clear();
        while (cursor.moveToNext()){
            listsql.add(new user(cursor.getString(1),cursor.getString(2),cursor.getString(0),null));
            adaptersql.notifyDataSetChanged();
        }
    }
}