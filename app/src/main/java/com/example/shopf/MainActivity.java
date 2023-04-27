package com.example.shopf;

import android.view.Menu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.shopf.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //boolean isLoading = false;
    String key = null;
    boolean isAdmin = false;
    //SwipeRefreshLayout swipeRefreshLayout;
    //RecyclerView recyclerView;
    RVadapter adapter;
    DAOprods dao;
    DAOusers daoUsers;
    BottomNavigationView nav;
    ActivityMainBinding binding;
    //RVinterface rVinterface;
    Users u;
    Users uEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().setIcon(R.drawable.exit);

        //getSupportActionBar().setIcon(R.drawable.exit);
        //ho commentato questo
        setContentView(R.layout.activity_main);

        //swipeRefreshLayout = findViewById(R.id.swip);
        //recyclerView = findViewById(R.id.pharmaProducts);
        //recyclerView.setHasFixedSize(true);
        //LinearLayoutManager manager = new LinearLayoutManager(this);
        //ecyclerView.setLayoutManager(manager);
        //adapter = new RVadapter(this, rVinterface);
        //recyclerView.setAdapter(adapter);
        //Intent i = getIntent();

        Bundle data = getIntent().getExtras();
        if (data != null) {
            u = (Users) data.getParcelable("user");
            uEdit = (Users) data.getParcelable("uEdit");
            isAdmin = data.getBoolean("isAdmin");
        }
        Log.d("u main", String.valueOf(u));
        dao = new DAOprods();
        daoUsers = new DAOusers();
        nav = findViewById(R.id.nav);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        //nav.setSelectedItemId(R.id.home);
        //casomai commentare questo
        //setContentView(binding.getRoot());
        if (uEdit != null)
            u = uEdit;

        //FirebaseRecyclerOptions<Prods> options = new FirebaseRecyclerOptions.
        //loadData();
        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem < lastVisible+3) {
                    if(!isLoading) {
                        isLoading = true;
                        loadData();
                    }
                }
            }
        });*/

        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        //recreate();
                        //nav.setSelectedItemId(R.id.home);
                        Toast.makeText(MainActivity.this, "Home", Toast.LENGTH_SHORT).show();
                        replaceFragment(HomeFragment.newInstance(u, isAdmin));
                        break;
                    case R.id.wishlist:
                        //nav.setSelectedItemId(R.id.wishlist);
                        Toast.makeText(MainActivity.this, "WishList", Toast.LENGTH_SHORT).show();
                        replaceFragment(WishListFragment.newInstance(u));
                        break;
                    case R.id.user:
                        //nav.setSelectedItemId(R.id.user);
                        Toast.makeText(MainActivity.this, "Profile", Toast.LENGTH_SHORT).show();
                        replaceFragment(ProfileFragment.newInstance(u, isAdmin));
                        break;
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        FragmentManager fm = getSupportFragmentManager();
        Fragment fragmentInstance = fm.findFragmentById(R.id.frameContainer);
        if (fragmentInstance == null)
            replaceFragment(HomeFragment.newInstance(u, isAdmin));
        if (fragmentInstance instanceof WishListFragment) {
            replaceFragment(WishListFragment.newInstance(u));
            //View v = nav.findViewById(R.id.wishlist);
            //v.performClick();
        }

    }

    /*@Override
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.log_out, menu);
            inflater.inflate(R.menu.searc_bar, menu);

            //getMenuInflater().inflate(R.menu.log_out, menu);
            //MenuItem menuItem = menu.findItem(R.id.lg_out_btn);
            //menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
             //ImageView log_out_icon = (ImageView) menuItem.getActionView();
             /*log_out_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
          return true;
        }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.lg_out_btn:
                Intent i = new Intent(MainActivity.this, Login_activity.class);
                startActivity(i);
                finish();
                return true;
            case R.id.wishlist:
                replaceFragment(WishListFragment.newInstance(u));
                return true;
            /*case R.id.search_bar:
                SearchView svHome = (SearchView) findViewById(R.id.search_bar);
                if(svHome != null) {
                    svHome.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            search(newText);
                            return true;
                        }
                    });
                }
                return true;*/
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*public void search(String s) {
        ArrayList<Prods> prodSearched = new ArrayList<>();
        for(Prods p:prods) {
            if(p.getDescription().toLowerCase().contains(s.toLowerCase()) || p.getName().toLowerCase().contains(s.toLowerCase())) {
                prodSearched.add(p);
            }
        }
        RVadapter rvItemSearched = new RVadapter(this,this);
        rvItemSearched.setItems(prodSearched);
        rvItemSearched.notifyDataSetChanged();
        recyclerView.setAdapter(rvItemSearched);
    }*/

    private void loadData() {
        //swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addValueEventListener(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Prods> prods = new ArrayList<>();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Prods p = data.getValue(Prods.class);
                    Log.d("firebase", String.valueOf(data.getValue(Prods.class)));
                    p.setKey(data.getKey());
                    prods.add(p);
                    key = data.getKey();
                }
                Log.d("elementi", String.valueOf(prods));
                adapter.setItems(prods);
                adapter.notifyDataSetChanged();
                //isLoading = false;
                //swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameContainer, fragment);
        fragmentTransaction.commit();
    }

    /*@Override
    protected void onRestart() {
        super.onRestart();
        recreate();
    }*/
}