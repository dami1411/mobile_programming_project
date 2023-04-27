package com.example.shopf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import java.util.HashMap;

public class AddProds_activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_add_prods);
        EditText srcImg = findViewById(R.id.srcImgTxt);
        EditText prodName = findViewById(R.id.prodNameTxt);
        EditText prodDescription = findViewById(R.id.prodDescriptionTxt);
        EditText prodPrice = findViewById(R.id.prodPriceTxt);
        RatingBar rating = findViewById(R.id.ratingTxt);
        Button insProd = findViewById(R.id.insProd);
        //Button allProds = findViewById(R.id.allProds);
        DAOprods dao = new DAOprods();
        Users u = getIntent().getParcelableExtra("user");
        Boolean isAdmin = getIntent().getBooleanExtra("isAdmin", false);
        Prods pEdit = getIntent().getParcelableExtra("EDIT");

        if (pEdit != null) {
            insProd.setText("UPDATE PRODUCT");
            prodName.setText(pEdit.getName());
            prodPrice.setText(pEdit.getPrice().toString());
            prodDescription.setText(pEdit.getDescription());
            srcImg.setText(pEdit.getSrcImg());
            rating.setRating(pEdit.getRating());
            //allProds.setVisibility(View.GONE);
        } else {
            insProd.setText("INSERT PRODUCT");
            //allProds.setVisibility(View.VISIBLE);
        }
        insProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Prods p = new Prods(srcImg.getText().toString(), prodName.getText().toString(), prodDescription.getText().toString(), Double.parseDouble(prodPrice.getText().toString()), rating.getRating());
                if (pEdit == null) {
                    dao.add(p).addOnSuccessListener(suc ->
                    {
                        Toast.makeText(AddProds_activity.this, "Product is inserted", Toast.LENGTH_SHORT).show();
                        //visualizzare gli elementi una volta inseriti
                        Intent i = new Intent(AddProds_activity.this, MainActivity.class);
                        i.putExtra("user", u);
                        i.putExtra("isAdmin", isAdmin);
                        startActivity(i);
                        finish();
                    }).addOnFailureListener(er ->
                    {
                        Toast.makeText(AddProds_activity.this, er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                } else {
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("name", prodName.getText().toString());
                    hashMap.put("description", prodDescription.getText().toString());
                    hashMap.put("price", Double.parseDouble(prodPrice.getText().toString()));
                    hashMap.put("rating", rating.getRating());
                    hashMap.put("srcImg", srcImg.getText().toString());
                    dao.update(pEdit.getKey(), hashMap).addOnSuccessListener(suc ->
                    {
                        Toast.makeText(AddProds_activity.this, "Product is updated", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(AddProds_activity.this, MainActivity.class);
                        i.putExtra("user", u);
                        i.putExtra("isAdmin", isAdmin);
                        startActivity(i);
                        finish();
                    }).addOnFailureListener(er ->
                    {
                        Toast.makeText(AddProds_activity.this, er.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
        /*allProds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //visualizzare gli elementi una volta inseriti
                Intent i = new Intent(AddProds_activity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });*/
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