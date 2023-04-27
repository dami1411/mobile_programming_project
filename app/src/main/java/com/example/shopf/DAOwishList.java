package com.example.shopf;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class DAOwishList {

    private DatabaseReference databaseReference;

    public DAOwishList() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(WishList.class.getSimpleName());
        databaseReference.keepSynced(true);
    }

    public Task<Void> add(WishList w) {
        return databaseReference.push().setValue(w);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> delete(String key) {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key) {

        return databaseReference.child(key);
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }
}
