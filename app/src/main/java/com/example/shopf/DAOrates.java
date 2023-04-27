package com.example.shopf;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOrates {

    private DatabaseReference databaseReference;

    public DAOrates() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Rates.class.getSimpleName());
    }

    public Task<Void> add(Rates r) {
        return databaseReference.push().setValue(r);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> delete(String key) {
        return databaseReference.child(key).removeValue();
    }

    public Query get() {

        return databaseReference.orderByKey();
        /*if(key == null) {
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.orderByKey().startAfter(key).limitToFirst(8);*/
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

}
