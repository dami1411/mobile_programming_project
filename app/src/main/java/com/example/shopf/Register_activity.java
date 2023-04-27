package com.example.shopf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;

public class Register_activity extends AppCompatActivity {
    TextView nameTxt, surnameTxt, emailTxt, pwdTxt, confPwdTxt, alreadyRegistered, emailTV;
    Button registerButton;
    Users uEdit;
    Boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        emailTV = findViewById(R.id.emailTV);
        nameTxt = findViewById(R.id.name);
        surnameTxt = findViewById(R.id.surname);
        emailTxt = findViewById(R.id.email);
        pwdTxt = findViewById(R.id.password);
        confPwdTxt = findViewById(R.id.confPassword);
        alreadyRegistered = findViewById(R.id.loginTextView);
        registerButton = findViewById(R.id.registerButton);

        DAOusers dao = new DAOusers();
        Bundle data = getIntent().getExtras();
        if (data != null) {
            uEdit = (Users) data.getParcelable("EDIT");
            isAdmin = data.getBoolean("isAdmin");
        }


        if (uEdit != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            nameTxt.setText(uEdit.getName());
            surnameTxt.setText(uEdit.getSurname());
            emailTxt.setVisibility(View.GONE);
            emailTV.setVisibility(View.GONE);
            alreadyRegistered.setVisibility(View.GONE);
            pwdTxt.setText(uEdit.getPwd());
            confPwdTxt.setText(uEdit.getPwd());
            registerButton.setText("edit");
        }

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (registerButton.getText().toString().equals("edit")) {
                    uEdit.setName(nameTxt.getText().toString());
                    uEdit.setSurname(surnameTxt.getText().toString());
                    uEdit.setPwd(pwdTxt.getText().toString());
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", uEdit.getName());
                    hashMap.put("pwd", uEdit.getPwd());
                    hashMap.put("surname", uEdit.getSurname());

                    dao.update(uEdit.getEmail(), hashMap).addOnSuccessListener(sucUserEdit -> {
                        Toast.makeText(Register_activity.this, "User's info are updated with success", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(Register_activity.this, MainActivity.class);
                        i.putExtra("uEdit", uEdit);
                        i.putExtra("isAdmin", isAdmin);
                        startActivity(i);
                        finish();
                    }).addOnFailureListener(errUserEdit -> {
                        Toast.makeText(Register_activity.this, errUserEdit.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    //emailTxt.setVisibility(View.VISIBLE);
                    //alreadyRegistered.setVisibility(View.VISIBLE);
                    Log.d("TEST", "ciaooo");
                    String emailText = emailTxt.getText().toString();
                    String escapedEmail = dao.escapeSpecialChars(emailText);
                    if ((nameTxt.getText().toString()).isEmpty() || (surnameTxt.getText().toString()).isEmpty() || (emailTxt.getText().toString()).isEmpty() || (pwdTxt.getText().toString()).isEmpty() || (confPwdTxt.getText().toString()).isEmpty()) {
                        Toast.makeText(Register_activity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    } else if (!(pwdTxt.getText().toString()).equals(confPwdTxt.getText().toString())) {
                        Toast.makeText(Register_activity.this, "Please check both having same password", Toast.LENGTH_SHORT).show();
                    } else {

                        //u.setEmail(emailText);
                        dao.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                Log.d("email", String.valueOf(snapshot.hasChild(escapedEmail)));
                                if (snapshot.hasChild(escapedEmail)) {
                                    Toast.makeText(Register_activity.this, "Email has already registered", Toast.LENGTH_SHORT).show();
                                } else {
                                    Users u = new Users(nameTxt.getText().toString(), surnameTxt.getText().toString(), (pwdTxt.getText().toString()));
                                    dao.add(u, dao.escapeSpecialChars(emailTxt.getText().toString())).addOnSuccessListener(suc -> {
                                        Toast.makeText(Register_activity.this, "User succesfully registered", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(Register_activity.this, Login_activity.class);
                                        startActivity(i);
                                        finish();
                                    }).addOnFailureListener(er -> {
                                        Toast.makeText(Register_activity.this, er.getMessage(), Toast.LENGTH_SHORT).show();
                                    });

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }

            }

        });

        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register_activity.this, Login_activity.class);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                 /*Intent i= new Intent(ShowProd.this, MainActivity.class);
                 startActivity(i);*/
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}