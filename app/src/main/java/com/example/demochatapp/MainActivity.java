package com.example.demochatapp;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.demochatapp.adapter.informessadapter;
import com.example.demochatapp.adapter.searchadapter;
import com.example.demochatapp.database.Databases;
import com.example.demochatapp.model.danhsachmess;
import com.example.demochatapp.model.informess;
import com.example.demochatapp.model.sendmess;
import com.example.demochatapp.model.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


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
    EditText test;
    EditText noidung;
    Button btntiemkiem ,btnlogout,btneditacc;
    TextView hotenaccount,emailaccount,usernameaccount;
    user account;
    Databases databases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         account= (user) getIntent().getSerializableExtra("account");
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
                   hotenaccount = findViewById(R.id.hoten);
                   emailaccount =findViewById(R.id.email);
                   usernameaccount =findViewById(R.id.username);
                   btnlogout=findViewById(R.id.btnlogout);
                   btneditacc=findViewById(R.id.btnedit);
                   hotenaccount.setText(account.name);
                   emailaccount.setText(account.email);
                   usernameaccount.setText(account.username);
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
                databases=new Databases(MainActivity.this,"pro.sqlite",null,1);
                databases.querydata("delete from userdangnhap ");

                startActivity(new Intent(MainActivity.this
                        ,Activitylogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

            }

        });
        btneditacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.activity_editacc);
                dialog.show();
                TextView editusername,editemail;
                EditText editname;
                Button editsave;
                editusername=dialog.findViewById(R.id.txttaikhoan);
                editemail=dialog.findViewById(R.id.txtemail);
                editname=dialog.findViewById(R.id.editname);
                editsave=dialog.findViewById(R.id.btnsave);
                editemail.setText(account.email);
                editusername.setText(account.username);
                editname.setText(account.name);
                editsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(editname.getText().length()>0){
                            if(!updateaccount(editname.getText().toString(),account.pass)){
                                databases=new Databases(MainActivity.this,"pro.sqlite",null,1);
                                databases.querydata(" UPDATE userdangnhap SET  name = '"+editname.getText().toString()+"' ");
                                Toast.makeText(MainActivity.this,"Đổi tên thành công",Toast.LENGTH_SHORT).show();
                                hotenaccount.setText(editname.getText());
                                dialog.cancel();
                            }

                        }
                        else editname.setError("Vui lòng nhập tên");

                    }
                });



              /*  updateaccount(if*//**//*);*/
            }
        });
    }

    private boolean updateaccount(String name,String pass) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        user user=new user(name,account.username,account.email,pass);
       return mDatabase.child(account.username).setValue(user).isSuccessful();
    }


    private void eventnguoinhan() {
        databases=new Databases(this,"pro.sqlite",null,1);
        databases.querydata(" CREATE TABLE IF NOT EXISTS message(nguoigui NVARCHAR(200) ,nguoinhan NVARCHAR(200) ,tinnhan nvarchar(2000),thoigiangui datetime )");
        databases.querydata(" CREATE TABLE IF NOT EXISTS user(email VARCHAR(200) ," + "name VARCHAR(200) ,username VARCHAR(200))");
        databases.querydata(" CREATE TABLE IF NOT EXISTS nguoidanhan(username VARCHAR(200) ," + "nguoinhan VARCHAR(200) )");


        final List<informess>  listUsers=new ArrayList<>();
        final informessadapter adapter=new  informessadapter(MainActivity.this,R.layout.itemsearch,listUsers);
        listViewinfor.setAdapter(adapter);

        //luu thông tin tai khoản va tin nhan tu firebase vao sqllite
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(account.username);
        mDatabase.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            danhsachmess nguoidanhan= (danhsachmess) snapshot.getValue(danhsachmess.class);

                            Cursor cur=databases.getdata("select * from user where username='"+nguoidanhan.nguoinhan+"'");
                            if(cur.getCount() <=0 ){


                                String s= nguoidanhan.nguoinhan;
                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

                                mDatabase.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        for (DataSnapshot snapshot1:snapshot.getChildren()){
                                            user u1= (user) snapshot1.getValue( user.class);
                                            if(u1.username.equals(s) ){

                                                databases.querydata(" insert into user values( '" +u1.email+"' , '"+u1.name+"' , '" +u1.username+"' ) ");

                                            }

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });


                            }
                            //them tin nhan vao sql lite
                            DatabaseReference mDatabasemess1 = FirebaseDatabase.getInstance().getReference("message");
                            mDatabasemess1.addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                                    sendmess tinnhan= (sendmess) snapshot.getValue(sendmess.class);
                                    if( (tinnhan.nguoigui.equals(account.username)&&tinnhan.nguoinhan.equals(nguoidanhan.nguoinhan)) || (tinnhan.nguoinhan.equals(account.username)&&tinnhan.nguoigui.equals(nguoidanhan.nguoinhan))  ){
                                        Cursor cursor = databases.getdata("select * from message where nguoigui = '"+tinnhan.nguoigui+"'  and nguoinhan = '"+tinnhan.nguoinhan+"'  and   tinnhan  ='"+tinnhan.tinnhan+"'  and  thoigiangui = '"+tinnhan.thoigiangui+"' ");
                                       if(cursor.getCount()<=0){
                                           databases.querydata(" insert into message values( '" +tinnhan.nguoigui+"' , '"+tinnhan.nguoinhan+"' , '" +tinnhan.tinnhan+"' , '"+tinnhan.thoigiangui+"') ");
                                       }

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

        //luu thong tin nguo da nhan

        DatabaseReference mDatabasedsmessto = FirebaseDatabase.getInstance().getReference(account.username);
        mDatabasedsmessto.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x=0;
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    danhsachmess uuu= (danhsachmess) snapshot1.getValue( danhsachmess.class);

                    Cursor cur=databases.getdata("select * from nguoidanhan where username ='"+account.username+"' and nguoinhan = '"+uuu.nguoinhan+"'");
                    if(cur.getCount() <=0 ){

                        databases.querydata(" insert into nguoidanhan values( '"+account.username+"' , '"+uuu.nguoinhan+"' ) ");

                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        //lay thong tin hien vao adapter informess

        Cursor cur=databases.getdata("select * from nguoidanhan where username='"+account.username+"' ");
        while (cur.moveToNext()){

            Cursor informesss =databases.getdata("select * from message where ( nguoigui = '"+account.username+"' and nguoinhan = '"+cur.getString(1)+"' ) or " +
                    " (  nguoinhan = '"+account.username+"' and nguoigui = '"+cur.getString(1)+"' )  " + " order by thoigiangui desc  LIMIT 1 ");
            informesss.moveToNext();
             Cursor user=databases.getdata("select * from user where username='"+cur.getString(1)+"' ");
            user.moveToNext();
            if(informesss.getCount()>0&& informesss.getCount()>0){
                if(informesss.getString(0).equals(account.username)){
                    informess infor=new informess(cur.getString(1),user.getString(1),user.getString(0),account.name,informesss.getString(2),informesss.getLong(3));
                    listUsers.add(infor);
                    adapter.notifyDataSetChanged();

                }
                else {
                    informess infor=new informess(cur.getString(1),user.getString(1),user.getString(0),user.getString(1),informesss.getString(2),informesss.getLong(3));
                    listUsers.add(infor);
                    adapter.notifyDataSetChanged();

                }
            }




        }








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
                                if((u1.name.contains(noidung.getText().toString())
                                        || u1.email.contains(noidung.getText().toString())) && !u1.username.equals(account.username)){
                                    listUsers.add(u1);
                                    java.util.Date utilDate = new java.util.Date();
                                    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                                    databases.querydata("insert into lichsutimkiem values('"+account.username+"' ,"+sqlDate.getTime()+",'"+u1.username+"')");
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



    public  void joinmess(String nguoinhan,String name) {


        Intent intent=new Intent(MainActivity.this,Activitymessager.class);
        intent.putExtra("nguoigui",account.username);
        intent.putExtra("nguoinhan",nguoinhan);
        intent.putExtra("tennguoinhan",name);
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
        databases.querydata("CREATE TABLE IF NOT EXISTS lichsutimkiem(id VARCHAR(200) ," + "thoigian DateTime, username VARCHAR(200))");
    }
    private void getdata() {

        final List<user> listsql=new ArrayList<>();
        final searchadapter   adaptersql=new searchadapter(MainActivity.this,R.layout.itemsearch,listsql);
        listViewtimkiem.setAdapter(adaptersql);
        databases=new Databases(this,"pro.sqlite",null ,1);
        listsql.add(account);
        Cursor cursor=databases.getdata("select DISTINCT id, username from lichsutimkiem where id='"+account.username+"'" + " order by thoigian desc");
        listsql.clear();
        while (cursor.moveToNext()){

            String s= cursor.getString(1);
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                        user u1= (user) snapshot1.getValue( user.class);
                        if(u1.username.equals(s) ){
                            listsql.add(u1);
                            adaptersql.notifyDataSetChanged();

                        }

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





        }
    }
}