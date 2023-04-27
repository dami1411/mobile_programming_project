package com.example.shopf;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.ArrayRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WishListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WishListFragment extends Fragment implements RVinterface, SearchView.OnQueryTextListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView countItemTV, favoriteTV;
    //SearchView svWsl;
    ArrayList<Prods> prods = new ArrayList<>();
    RecyclerView rvWsl;
    //boolean isLoading = false;
    RVadapter adapterWsl;
    DAOprods dao;
    DAOwishList daoWsl;
    Users u;
    String keyProd = null;
    String keyWsl = null;
    int iterator = 0;

    public WishListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WishListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WishListFragment newInstance(String param1, String param2) {
        WishListFragment fragment = new WishListFragment();
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
        return inflater.inflate(R.layout.fragment_wish_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dao = new DAOprods();
        daoWsl = new DAOwishList();
        if (getArguments() != null)
            u = (Users) getArguments().getParcelable(
                    "user");
        countItemTV = view.findViewById(R.id.countItemTV);
        favoriteTV = view.findViewById(R.id.favoriteTV);
        //svWsl = view.findViewById(R.id.sv_wsl);
        rvWsl = view.findViewById(R.id.rv_wsl);
        rvWsl.setHasFixedSize(true);
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        rvWsl.setLayoutManager(manager);
        adapterWsl = new RVadapter(getContext(), this);
        rvWsl.setAdapter(adapterWsl);
        loadData();


            /*rvWsl.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)recyclerView.getLayoutManager();
                int totalItem = linearLayoutManager.getItemCount();
                int lastVisible = linearLayoutManager.findLastCompletelyVisibleItemPosition();
                if(totalItem < lastVisible+3) {
                    if(!isLoading) {
                        isLoading = true;
                        //loadData();
                    }
                }
            }
        });*/
        rvWsl.addOnScrollListener(new HidingScrollListener() {
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
        favoriteTV.setVisibility(View.GONE);
    }

    private void showViews() {
        favoriteTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_menu, menu);
        MenuItem item = menu.findItem(R.id.search_bar);
        //item.setVisible(false);
        SearchView svWsl = (SearchView) item.getActionView();
        svWsl.setQueryHint("prod's name or ingredients");
        if (svWsl != null) {
            svWsl.setOnQueryTextListener(this);

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

        ArrayList<String> keysWsl = new ArrayList<>();
        ArrayList<String> keysProd = new ArrayList<>();
        daoWsl.getDatabaseReference().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot data : snapshot.getChildren()) {

                    Log.d("iterator ", String.valueOf(snapshot.getChildrenCount()));
                    iterator++;
                    if (data.child("emailWsl").getValue(String.class).equals(u.getEmail())) {
                        keyProd = data.child("keyProd").getValue(String.class);
                        if (keyProd != null) {
                            Log.d("key", keyProd);
                            keysWsl.add(data.getKey());
                            keysProd.add(keyProd);

                        }


                    }
                }

                Log.d("elementi TEST", String.valueOf(keysProd));
                Log.d("size", String.valueOf(keysProd.size()));

                for (int i = 0; i < keysProd.size(); i++) {
                    keyWsl = keysWsl.get(i);
                    Log.d("keyTEST", keysProd.get(i));
                    dao.getDatabaseReference().child(keysProd.get(i)).addValueEventListener(new ValueEventListener() {
                        @Override

                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Log.d("snapshot", String.valueOf(snapshot));
                            Prods p = snapshot.getValue(Prods.class);
                            if (p != null) {

                                p.setKey(snapshot.getKey());
                                prods.add(p);
                            } else
                                daoWsl.delete(keyWsl);
                            /*for(DataSnapshot data : snapshot.getChildren()) {
                                Prods p = data.getValue(Prods.class);
                                 Log.d("firebase",String.valueOf(data.getValue(Prods.class)));
                                 p.setKey(data.getKey());
                                prods.add(p);
                                    //key = data.getKey();
                              }*/

                            adapterWsl.setItems(prods);
                            adapterWsl.notifyDataSetChanged();
                            Log.d("elementi", String.valueOf(prods));
                            Log.d("ItemCount", String.valueOf(adapterWsl.getItemCount()));
                            //isLoading = false;
                            //swipeRefreshLayout.setRefreshing(false);
                            if (adapterWsl.getItemCount() > 0)
                                countItemTV.setVisibility(View.GONE);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            //swipeRefreshLayout.setRefreshing(false);
                        }

                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onItemClick(Prods p) {
        Toast.makeText(getContext(), p.getName(), Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getContext(), ShowProd.class);
        i.putExtra("prod", p);
        i.putExtra("user", u);
        startActivity(i);
    }

    public static WishListFragment newInstance(Users u) {
        WishListFragment fragment = new WishListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", u);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText;
        ArrayList<Prods> newList = new ArrayList<>();
        for (Prods p : prods) {
            if (p.getName().toLowerCase().contains(userInput) || p.getDescription().toLowerCase().contains(userInput))
                newList.add(p);
        }
        adapterWsl.updateList(newList);
        return true;
    }
}