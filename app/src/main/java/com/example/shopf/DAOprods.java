package com.example.shopf;


import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.ktx.Firebase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;

public class DAOprods {

    private DatabaseReference databaseReference;
    private StorageReference pathReference;

    public DAOprods() {
        //FirebaseStorage storage = FirebaseStorage.getInstance();

        //StorageReference storageRef = storage.getReference();
        //athReference = storageRef.child("images");
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(Prods.class.getSimpleName());
    }

    public Task<Void> add(Prods p) {
        return databaseReference.push().setValue(p);
    }

    public Task<Void> update(String key, HashMap<String, Object> hashMap) {
        return databaseReference.child(key).updateChildren(hashMap);
    }

    public Task<Void> delete(String key) {
        return databaseReference.child(key).removeValue();
    }

    public Query get(String key) {
        /*if(key == null) {
            return databaseReference.orderByKey().limitToFirst(8);
        }
        return databaseReference.orderByKey().startAfter(key).limitToFirst(8);*/
        return databaseReference.orderByKey();
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    /*public StorageReference getStorageReference() {
        return pathReference;
    }*/


}
