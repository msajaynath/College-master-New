package com.example.hp.college;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.List;

import Entity.Courses;
import Entity.User;

public class RegisterActivity extends AppCompatActivity {

    //private User u;
    EditText name,username,password;
    Button update;
    ImageView changePass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        final SharedPreferences sharedpreferences = getSharedPreferences("Diet", Context.MODE_PRIVATE);



        changePass= (ImageView) findViewById(R.id.changepass);
        update= (Button) findViewById(R.id.update);
        name= (EditText) findViewById(R.id.name);

        username= (EditText) findViewById(R.id.username);
        password= (EditText) findViewById(R.id.password);



        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(username.getText().toString().length()<4)
                {
                    Toast.makeText(getApplicationContext(), "Username too short.(Min 4 letters)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.getText().toString().length()>4) {
                    List<User> userList = Courses.findWithQuery(User.class, "Select * from User where username = ?", username.getText().toString() + "");

                    if(userList.size()>0)
                    {
                        Toast.makeText(getApplicationContext(), "Username already exits.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    User u = new User();
                    u.name = name.getText().toString();
                    u.username=username.getText().toString();
                    u.password = password.getText().toString();

                    u.save();
                    Toast.makeText(getApplicationContext(), "Registered.Please login!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Password too short.(Min 4 letters)", Toast.LENGTH_SHORT).show();
                    return;


                }
            }
        });





    }
}
