package com.example.shopf;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements RVinterface, SearchView.OnQueryTextListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ArrayList<Prods> prods = new ArrayList<>();
    RecyclerView recyclerView;
    //private Toolbar mToolbar;
    //SearchView svHome;
    TextView discoverTV;
    LinearLayout discoverRL;
    boolean isAdmin = false;
    RVadapter adapter;
    String key = null;
    DAOprods dao;
    Users u;
    boolean isLoading = false;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dao = new DAOprods();
        if (getArguments() != null) {
            u = (Users) getArguments().getParcelable(
                    "user");
            isAdmin = getArguments().getBoolean("isAdmin");
        }
        discoverTV = view.findViewById(R.id.discoverTV);
        //discoverRL = view.findViewById(R.id.discoverRL);
        //svHome = view.findViewById(R.id.search_bar);

        recyclerView = view.findViewById(R.id.pharmaProducts);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(manager);
        adapter = new RVadapter(getContext(), this);
        adapter.setUser(u);
        adapter.setIsAdmin(isAdmin);
        recyclerView.setAdapter(adapter);
        loadData();
        //LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //int pos = linearLayoutManager.findFirstVisibleItemPosition();
        //linearLayoutManager.findViewByPosition(pos).getTop()==0 && pos==0


        /*recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
           public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {


                //int totalItem = linearLayoutManager.getItemCount();
                //int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                /*if(totalItem < lastVisible+3) {
                    if(!isLoading) {
                        isLoading = true;
                        //loadData();
                    }
                }
            }
        });*/
        recyclerView.addOnScrollListener(new HidingScrollListener() {
            @Override
            public void onHide() {
                hideViews();
            }

            @Override
            public void onShow() {
                showViews();
            }
        });
    }

    private void hideViews() {
        //discoverTV.animate().translationY(-discoverTV.getHeight()).setInterpolator(new AccelerateInterpolator(2));
        //discoverTV.setPadding(0,0,0,0);
        discoverTV.setVisibility(View.GONE);
    }

    private void showViews() {
        //discoverTV.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
        //discoverTV.setPadding(20,20,20,20);
        discoverTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.action_bar_menu, menu);
        MenuItem item = menu.findItem(R.id.search_bar);
        SearchView svHome = (SearchView) item.getActionView();
        svHome.setQueryHint("prod's name or ingredients");

        if (svHome != null) {

            svHome.setOnQueryTextListener(this);

        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lg_out_btn:
                Intent i = new Intent(getContext(), Login_activity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        //swipeRefreshLayout.setRefreshing(true);
        dao.get(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot data : snapshot.getChildren()) {
                    Prods p = data.getValue(Prods.class);
                    Log.d("src", String.valueOf(p.getSrcImg()));
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

    /*public void search(String s) {

        adapter.getFilter().filter(s);
        ArrayList<Prods> prodSearched = new ArrayList<>();
        for(Prods p:prods) {
            if(p.getDescription().toLowerCase().contains(s.toLowerCase()) || p.getName().toLowerCase().contains(s.toLowerCase())) {
                prodSearched.add(p);
            }
            else if(s.equals(""))
                prods.addAll(prodSearched);
        }
        RVadapter rvItemSearched = new RVadapter(getContext(),this);
        rvItemSearched.setItems(prodSearched);
        rvItemSearched.notifyDataSetChanged();
        recyclerView.setAdapter(rvItemSearched);
    }*/

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection

        switch (item.getItemId()) {
            case R.id.search_bar:
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
        }
        return true;
    }*/

    @Override
    public void onItemClick(Prods p) {
        Toast.makeText(getContext(), p.getName(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), ShowProd.class);
        i.putExtra("prod", p);
        Log.d("TESTTT", String.valueOf(p.getSrcImg()));
        i.putExtra("user", u);
        startActivity(i);
    }

    public static HomeFragment newInstance(Users u, boolean isAdmin) {
        HomeFragment fragment = new HomeFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", u);
        bundle.putBoolean("isAdmin", isAdmin);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        ArrayList<Prods> newList = new ArrayList<>();
        for (Prods p : prods) {
            if (p.getName().toLowerCase().contains(userInput) || p.getDescription().toLowerCase().contains(userInput))
                newList.add(p);
        }
        adapter.updateList(newList);
        return true;
    }
}
