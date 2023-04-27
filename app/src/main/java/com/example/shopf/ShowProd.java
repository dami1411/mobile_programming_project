package com.example.shopf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.HashMap;

public class ShowProd extends AppCompatActivity {
    TextView nameShowProd, descrShowProd, priceShowProd, rateProdTV;
    ImageView imgShowProd;
    RatingBar avgRating, usrRating;
    Button insRate;
    Float usrRate;
    Long totRates = 0l;
    Float sumRates = 0f;
    Float oldRate = 0f;
    Float rateProd = 0f;
    String key = null;
    String keyWsl = null;
    boolean isRated = false;
    boolean isDeleted = false;
    boolean isFavorite = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_show_prod);
        Bundle data = getIntent().getExtras();
        //Intent i = getIntent();
        Prods p = (Prods) data.getParcelable("prod");
        Log.d("ratingDEBUG", String.valueOf(p.getRating()));
        Log.d("SHOWPRODTESTTT", String.valueOf(p.getKey()));
        Users u = (Users) data.getParcelable("user");
        imgShowProd = findViewById(R.id.srcImgShowProd);
        nameShowProd = findViewById(R.id.nameShowProd);
        descrShowProd = findViewById(R.id.descrShowProd);
        priceShowProd = findViewById(R.id.priceShowProd);
        avgRating = findViewById(R.id.ratingShowProd);
        usrRating = findViewById(R.id.ratingUsrShowProd);
        rateProdTV = findViewById(R.id.rateProdTV);
        ImageView favoriteShowProd = findViewById(R.id.favoriteShowProd);
        TextView favoriteTV = findViewById(R.id.favoriteTV);

        if (p.getSrcImg() != null) {
            Log.d("DEBUG", "prima");
            Glide.with(ShowProd.this).load(p.getSrcImg()).into(imgShowProd);
            Log.d("DEBUG", "dopo");
        } else Log.d("DEBUG", "null");


        nameShowProd.setText(p.getName());
        descrShowProd.setText(p.getDescription());
        priceShowProd.setText(p.getPrice().toString()+"€");
        avgRating.setRating(p.getRating());
        insRate = findViewById(R.id.insRate);
        DAOrates dao = new DAOrates();
        DAOprods daOprods = new DAOprods();
        DAOwishList daOwishList = new DAOwishList();

        daOwishList.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    if (data.child("keyProd").getValue(String.class).equals(p.getKey())) {
                        if (data.child("emailWsl").getValue(String.class).equals(u.getEmail())) {
                            keyWsl = data.getKey();
                            Log.d("key", String.valueOf(data.getKey()));
                            isFavorite = true;
                            favoriteShowProd.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                            favoriteTV.setText("remove from WishList");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        favoriteShowProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isFavorite) {

                    daOwishList.delete(keyWsl).addOnSuccessListener(sucDeleteWsl -> {
                        Toast.makeText(ShowProd.this, "Product removed from WishList", Toast.LENGTH_SHORT).show();

                        //Intent i= new Intent(ShowProd.this, MainActivity.class);
                        //finish();

                    }).addOnFailureListener(errDeleteWsl -> {
                        Toast.makeText(ShowProd.this, errDeleteWsl.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                    isFavorite = false;
                    favoriteShowProd.setImageResource(R.drawable.ic_baseline_favorite_24);
                    favoriteTV.setText("add to WishList");
                } else {
                    favoriteShowProd.setImageResource(R.drawable.ic_baseline_favorite_24_red);
                    WishList w = new WishList(p.getKey(), u.getEmail());
                    daOwishList.add(w).addOnSuccessListener(sucWsl -> {
                        Toast.makeText(ShowProd.this, "Product added to WishList with success", Toast.LENGTH_SHORT).show();
                        isFavorite = true;
                    }).addOnFailureListener(errWsl -> {
                        Toast.makeText(ShowProd.this, errWsl.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }

            }
        });

        dao.getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {
                    Log.d("altro membro", String.valueOf(data.child("keyProd").getValue(String.class)));
                    if (p.getKey().equals(data.child("keyProd").getValue(String.class))) {
                        sumRates += data.child("tempRate").getValue(Float.class);
                        totRates++;
                        Log.d("entro", "sono dentro");
                        if (u.getEmail().equals(data.child("emailRate").getValue(String.class))) {
                            isRated = true;
                            key = data.getKey();
                            rateProdTV.setText("Your rate");
                            insRate.setText("update");
                            usrRating.setRating(data.child("tempRate").getValue(Float.class));
                            usrRating.setIsIndicator(true);
                            Log.d("entro", "sono dentro");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        usrRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                int value = (int) rating;
                usrRate = usrRating.getRating();
                switch (value) {
                    case 1:
                        Toast.makeText(ShowProd.this, "😕", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(ShowProd.this, "😐", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(ShowProd.this, "😊", Toast.LENGTH_SHORT).show();
                        break;
                    case 4:
                        Toast.makeText(ShowProd.this, "😁", Toast.LENGTH_SHORT).show();
                        break;
                    case 5:
                        Toast.makeText(ShowProd.this, "😍", Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        insRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRated) {

                    rateProdTV.setText("rate product");
                    insRate.setText("rate");
                    usrRating.setRating(0.0f);
                    oldRate = usrRating.getRating();
                    Float calcAvgRate = ((sumRates + usrRating.getRating()) / totRates);
                    usrRating.setIsIndicator(false);
                    /*HashMap<String,Object> hashMapEdit = new HashMap<>();
                    hashMapEdit.put("avgRate", calcAvgRate );
                    hashMapEdit.put("keyProd", p.getDescription());
                    hashMapEdit.put("tempRate", usrRating);
                    hashMapEdit.put("emailRate", u.getEmail());*/
                    //dao.update(key,hashMapEdit).addOnSuccessListener(
                    dao.delete(key).addOnSuccessListener(sucEdit -> {
                        Toast.makeText(ShowProd.this, "your rate is deleted", Toast.LENGTH_SHORT).show();

                    }).addOnFailureListener(erEdit -> {
                        Toast.makeText(ShowProd.this, erEdit.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                    isRated = false;
                }
                /*dao.get().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(data:DataSnapshot)
                        totRates = snapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });*/
                else {
                    //Log.d("butn",String.valueOf(insRate.getText()));
                    totRates++;

                    if (isDeleted) {
                        sumRates -= oldRate;
                        totRates--;
                    }

                    rateProd = ((sumRates + usrRate) / totRates);
                    Rates r = new Rates(rateProd, p.getKey(), usrRate, u.getEmail());
                    //Toast.makeText(ShowProd.this, "prodotto " + p.getName() + "con codice " + p.getKey() + " recensione di " + usrRate.toString() + " stelle da " + u.getName(), Toast.LENGTH_SHORT).show();
                    dao.add(r).addOnSuccessListener(suc -> {
                        //Toast.makeText(ShowProd.this, "1)rate is inserted with success", Toast.LENGTH_SHORT).show();
                        //modifichiamo il campo recensione del prodotto
                        p.setRating(rateProd);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("name", p.getName());
                        hashMap.put("description", p.getDescription());
                        hashMap.put("price", p.getPrice());
                        hashMap.put("rating", p.getRating());
                        daOprods.update(p.getKey(), hashMap).addOnSuccessListener(success -> {
                            Toast.makeText(ShowProd.this, "Rate is inserted with success", Toast.LENGTH_SHORT).show();
                        }).addOnFailureListener(error -> {
                            Toast.makeText(ShowProd.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                        });

                    }).addOnFailureListener(er -> {
                        Toast.makeText(ShowProd.this, er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {

            finish();
        }
        return super.onKeyDown(keyCode, event);
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