package com.example.demochatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.demochatapp.database.Databases;
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

import java.util.HashMap;

public class ActivityRegister extends AppCompatActivity {

    Button btnregister;
    EditText name, username,email,pass,confirmpass;
    FirebaseAuth mauth;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        setactionbar();
        getid();
        event();
        
    }

    private void setactionbar() {

        ActionBar actionBar = getSupportActionBar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setTitle("");

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
                return true;




            default:break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void event() {
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().length()>0&&username.getText().length()>0&&email.getText().length() >0&& pass.getText().length() >0
                        && confirmpass.getText().length() >0&&pass.getText().toString().equals(confirmpass.getText().toString())) {

                    user use=new user(name.getText().toString(),username.getText().toString(),email.getText().toString(),pass.getText().toString());
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");

                    mDatabase.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int x=0;
                            for(DataSnapshot snapsho : snapshot.getChildren()){

                                    user u1= ( user) snapsho.getValue( user.class);
                                    // case n???u l?? email
                                    if( u1.username.equals(use.username)){
                                        x++;
                                        username.setError("T??n ????ng nh???p ???? t???n t???i  ");


                                    }
                                    else  if( u1.email.equals(use.email)){
                                        x++;
                                        username.setError("Email ???? t???n t???i ");
                                    }



                            }
                            if(x==0){
                                Databases databases = new Databases(ActivityRegister.this,"pro.sqlite",null,1);
                                databases.querydata("insert into user values('"+email.getText()+"' ,'"+name.getText()+"','"+username.getText()+"')");
                                    String userId = mDatabase.push().getKey();
                                    mDatabase.child(use.username).setValue(use);
                                    startActivity(new Intent(ActivityRegister.this
                                            ,Activitylogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });




                   /* register(username.toString(),email.toString(),pass.toString(),confirmpass.toString());*/

                }
                else  if(name.getText().length()<=0){
                    name.setError("Vui l??ng nh???p h??? t??n ");

                }
                else  if(username.getText().length()<=0){
                    username.setError("Vui l??ng nh???p t??n ????ng nh???p");

                }
                else  if(email.getText().length()<=0){
                    email.setError("Vui l??ng nh???p email");

                }
                else  if(pass.getText().length()<=0){
                    pass.setError("Vui l??ng nh???p m???t kh???u");

                }
                else  if(confirmpass.getText().length()<=0){
                    confirmpass.setError("Vui l??ng nh???p x??c nh???n  m???t kh???u");


                }
                else  if(!pass.getText().toString().equals(confirmpass.getText().toString())){
                    confirmpass.setError("X??c nh???n m???t kh???u kh??ng ????ng");


                }

            }


        });


    }
    private void register(String user,String email,String pass1,String pass2) {

        mauth.createUserWithEmailAndPassword(email,pass1).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user1 =mauth.getCurrentUser();
                    reference= FirebaseDatabase.getInstance().getReference("Users").child(user1.getUid());
                    Toast.makeText(ActivityRegister.this,"thanh cong",Toast.LENGTH_SHORT).show();

                    if(user1!=null){
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("username",user);
                        hashMap.put("email",email);
                        hashMap.put("id",user1.getUid());
                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(ActivityRegister.this,"tao tai khoan thanh cong",Toast.LENGTH_SHORT).show();

                                    startActivity(new Intent(ActivityRegister.this
                                            ,Activitylogin.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
                                }
                            }
                        });
                    }
                }
            }
        });

    }
    private void getid() {

        btnregister=findViewById(R.id.btnregister);
        name=findViewById(R.id.name);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        confirmpass=findViewById(R.id.confirmpass);

        mauth= FirebaseAuth.getInstance();
    }
}