package com.example.shopf;

import android.content.Intent;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.EventListener;
import java.util.HashMap;

public class DAOusers {

    private DatabaseReference databaseReference;
    boolean flag = false;
    String passwordDB = null;
    boolean isAdmin = false;


    public DAOusers() {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Users.class.getSimpleName());
        databaseReference.keepSynced(true);
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }


    public Task<Void> add(Users u, String email) {

        return databaseReference.child(email).setValue(u);
    }

    public Task<Void> update(String email, HashMap<String, Object> hashMap) {
        return databaseReference.child(email).updateChildren(hashMap);
    }

    public Task<Void> delete(String email) {
        return databaseReference.child(email).removeValue();
    }

    public Query get(String email) {
        if (email == null) {
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.orderByKey().startAfter(email).limitToFirst(8);
    }

    public String escapeSpecialChars(String toFormat) {
        int specialPos, dotPos;
        specialPos = dotPos = 0;
        specialPos = toFormat.indexOf('@');
        if (specialPos != -1)
            toFormat = toFormat.substring(0, specialPos) + "tokchio" + toFormat.substring(specialPos + 1);
        Log.d("SPECIAL", toFormat);
        dotPos = toFormat.indexOf('.');
        if (dotPos != -1)
            while (dotPos != -1) {
                toFormat = toFormat.substring(0, dotPos) + "tok" + toFormat.substring(dotPos + 1);
                dotPos = toFormat.indexOf('.', dotPos + 1);
            }

        Log.d("email formatted", toFormat);
        return toFormat;
    }

    public String getOriginalEmailFrom(String formatted) {
        String originalEmail = "";
        String[] email = formatted.split("tokchio");
        if (email != null) {
            email[1] = "@" + email[1];
            Log.d("@", String.valueOf(email));
            int pointPos = 0;
            String[] dots = email[0].split("tok");
            String[] domain = email[1].split("tok");
            pointPos = dots.length - 1;
            for (int i = 0; i < pointPos; i++) {
                originalEmail += dots[i] + '.';
            }
            originalEmail += dots[dots.length - 1];
            originalEmail += domain[0] + '.' + domain[1];
        }

        //Log.d("DOTS", String.valueOf(emailElements));
        Log.d("ORIGINAL", String.valueOf(originalEmail));
        return originalEmail;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

}
