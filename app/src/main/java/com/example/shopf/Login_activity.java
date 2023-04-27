package com.example.shopf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Login_activity extends AppCompatActivity {
    EditText emailTxt, passwordTxt;
    TextView gotoRegister;
    Button loginButton;
    String emailText, pwdText;
    String escapedEmail = "";
    boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTxt = findViewById(R.id.editUserName);
        passwordTxt = findViewById(R.id.editPassword);
        loginButton = findViewById(R.id.loginButton);
        gotoRegister = findViewById(R.id.registerTextView);
        emailTxt.setText("");
        passwordTxt.setText("");

        DAOusers dao = new DAOusers();

        gotoRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login_activity.this, Register_activity.class);
                startActivity(i);
            }
        });

        //emailText = dao.getOriginalEmailFrom(emailText);
        //Log.d("RES",emailText);
        /*int specialPos, dotPos;
        specialPos = dotPos = 0;
        specialPos = emailText.indexOf('@');
        if(specialPos != -1)
            emailText = emailText.substring(0, specialPos)+"tokchio"+emailText.substring(specialPos+1);
        Log.d("SPECIAL",emailText);
        dotPos = emailText.indexOf('.');
        if(dotPos != -1)
            while(dotPos != -1) {
                emailText = emailText.substring(0, dotPos)+"tok"+emailText.substring(dotPos+1);
                dotPos = emailText.indexOf('.', dotPos+1);
            }

        Log.d("email formatted",emailText);
        String [] email = emailText.split("tokchio");
        if(email != null) {
            email[1] = "@" + email[1];
            Log.d("@", String.valueOf(email));
            int pointPos = 0;
            String[] dots = email[0].split("tok");
            String[] domain = email[1].split("tok");
            pointPos = dots.length -1;
            for(int i = 0; i < pointPos; i++) {
                originalEmail+= dots[i]+'.';
            }
            originalEmail+= dots[dots.length-1];
            originalEmail+=domain[0]+'.'+domain[1];
        }

        //Log.d("DOTS", String.valueOf(emailElements));
        Log.d("ORIGINAL",String.valueOf(originalEmail));*/
        loginButton.setOnClickListener(v -> {
            pwdText = passwordTxt.getText().toString();
            emailText = emailTxt.getText().toString();
            //emailText = "damifino@gmail.com";

            escapedEmail = dao.escapeSpecialChars(emailText);

            //loginButton.setText(passwordText);
            Log.d("EMAIL", escapedEmail);
            if (pwdText.isEmpty() || emailText.isEmpty())
                Toast.makeText(Login_activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            else {
                String dominio = null;
                if (emailText.contains("@"))
                    dominio = emailText.split("@")[1];
                //Log.d("dominio", dominio);
                String adminDomain = "adminpa.it";
                if (adminDomain.equals(dominio)) {
                    isAdmin = true;
                }
                else isAdmin = false;
                dao.getDatabaseReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        Log.d("data", String.valueOf(snapshot.hasChild(escapedEmail)));
                        if (snapshot.hasChild(escapedEmail)) {
                            if (pwdText.equals(snapshot.child(escapedEmail).child("pwd").getValue(String.class))) {
                                Toast.makeText(Login_activity.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Login_activity.this, MainActivity.class);
                                Users u = new Users(snapshot.child(escapedEmail).child("name").getValue(String.class), snapshot.child(escapedEmail).child("surname").getValue(String.class), snapshot.child(escapedEmail).child("pwd").getValue(String.class));
                                u.setEmail(escapedEmail);
                                Log.d("ISADMIN", String.valueOf(isAdmin));
                                intent.putExtra("isAdmin", isAdmin);
                                intent.putExtra("user", u);
                                startActivity(intent);
                                //finish();
                            } else {
                                Toast.makeText(Login_activity.this, "Wrong password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(Login_activity.this, "Wrong Email", Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

    }
}