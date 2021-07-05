package com.example.demochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demochatapp.model.user;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;

public class Activitylogin extends AppCompatActivity {

    Button login;
    TextView resetpass,register;
    EditText username,password;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activitylogin);


        getid();
        event();
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
                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int x=0;
                            for(DataSnapshot snapsho : snapshot.getChildren()){

                                user u1= ( user) snapsho.getValue( user.class);

                                if( u1.username.equals(use) && u1.pass.equals(pass)){
                                    Intent intent=new Intent(Activitylogin.this,MainActivity.class);
                                    intent.putExtra("account", u1);
                                    startActivity(intent);
                                    x++;
                                    break;

                                }
                            }
                            if(x==0){
                                Toast.makeText(Activitylogin.this,"tên đăng nhập và mật khẩu không đúng ",Toast.LENGTH_SHORT).show();


                            }

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


    }



    private void getid() {

        login=findViewById(R.id.btnlogin);
        resetpass=findViewById(R.id.resetpass);
        register=findViewById(R.id.register);
        username=findViewById(R.id.username);
        password=findViewById(R.id.pass);


    }
}