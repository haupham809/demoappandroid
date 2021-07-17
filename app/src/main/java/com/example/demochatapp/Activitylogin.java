package com.example.demochatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demochatapp.database.Databases;
import com.example.demochatapp.model.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Properties;

public class Activitylogin extends AppCompatActivity {

    Button login;
    TextView resetpass,register;
    EditText username,password;
    Databases databases;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitylogin);

        prepareDB();
        getdata();
        getid();
        event();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

    }

    private void event() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();

                if(username.getText().length()>0&&password.getText().length() >0) {

                    String use=username.getText().toString();
                    String pass =password.getText().toString();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");


                    mDatabase.orderByKey().equalTo(username.getText().toString()).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                            user u1= ( user) snapshot.getValue( user.class);

                            if( u1.username.equals(use) && u1.pass.equals(pass)){

                                Intent intent=new Intent(Activitylogin.this,MainActivity.class);
                                intent.putExtra("account", u1);
                                databases.querydata("insert into userdangnhap values('"+u1.email+"' ,'"+u1.name+"' ,'"+u1.username+"' ,'"+u1.pass+ "')");
                                startActivity(intent);


                            }else {
                                Toast.makeText(Activitylogin.this,"tên đăng nhập và mật khẩu không đúng ",Toast.LENGTH_SHORT).show();
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
                else  if(username.getText().length()<=0&&password.getText().length() <=0){
                    Toast.makeText(Activitylogin.this,"Vui lòng nhập tên đăng nhập và mật khẩu",Toast.LENGTH_SHORT).show();

                }
                else  if(username.getText().length()<=0&&password.getText().length() >0){
                    Toast.makeText(Activitylogin.this,"Vui lòng nhập tên đăng nhập",Toast.LENGTH_SHORT).show();

                }
                else  if(username.getText().length()>0&&password.getText().length() <=0){
                    Toast.makeText(Activitylogin.this,"Vui lòng nhập mật khẩu",Toast.LENGTH_SHORT).show();

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Activitylogin.this,ActivityRegister.class);

                startActivity(intent);

            }
        });
        resetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               Dialog dialog=new  Dialog(Activitylogin.this);
                dialog.setContentView(R.layout.activity_resetpass);
                dialog.show();
                EditText username;
                Button btnsave;

                username=dialog.findViewById(R.id.username);
                btnsave=dialog.findViewById(R.id.btnreset);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
                btnsave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(username.getText().length()>0){
                            mDatabase.orderByKey().equalTo(username.getText().toString()).addChildEventListener(new ChildEventListener() {
                                @Override
                                public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                                    user u1= ( user) snapshot.getValue( user.class);
                                    guimail(u1.email,u1.pass);
                                    dialog.cancel();

                                }

                                @Override
                                public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                }

                                @Override
                                public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {

                                }

                                @Override
                                public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {


                                }
                            });

                            mDatabase.orderByKey().equalTo(username.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        username.setError("Username không đúng");
                                    }

                                }
                            });


                        }
                        else
                            username.setError("vui lòng nhập username");

                    }
                });





            }
        });



    }

    private void guimail(String email , String pass) {


    }


    private void prepareDB() {
        databases=new Databases(this,"pro.sqlite",null,1);
        databases.querydata(" CREATE TABLE IF NOT EXISTS userdangnhap(email VARCHAR(200) ," + "name VARCHAR(200) ,username VARCHAR(200),password VARCHAR(200))");

    }

    private void getdata() {


        databases=new Databases(this,"pro.sqlite",null ,1);

        Cursor cursor=databases.getdata("select * from userdangnhap");

        while (cursor.moveToNext()){
            user account = new user(cursor.getString(1),cursor.getString(2),cursor.getString(0),cursor.getString(3));
            Intent intent=new Intent(Activitylogin.this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("account", account);
            startActivity(intent);
        }
    }
    private void getid() {

        login=findViewById(R.id.btnlogin);
        resetpass=findViewById(R.id.resetpass);
        register=findViewById(R.id.register);
        username=findViewById(R.id.username);
        password=findViewById(R.id.pass);


    }
}