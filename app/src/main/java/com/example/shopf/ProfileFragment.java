package com.example.shopf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.materialswitch.MaterialSwitch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    TextView firstNameTxt, secondNameTxt, emailTxt, pwdAskTxt;
    ImageView editImg, removeImg, addProdBtn;
    Switch switchDark;
    Button askPwdbtn;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Users u;
    DAOusers daoUsers;
    boolean isAdmin = false;
    boolean editBtnClicked = false;
    boolean addProdBtnClicked = false;

    //boolean nightMode = false;
    //SharedPreferences sharedPreferences;
    //SharedPreferences.Editor editor;
    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

        return inflater.inflate(R.layout.fragment_profile, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        daoUsers = new DAOusers();
        if (getArguments() != null) {
            u = (Users) getArguments().getParcelable(
                    "user");
            isAdmin = getArguments().getBoolean("isAdmin");
        }
        firstNameTxt = view.findViewById(R.id.getNameTV);
        secondNameTxt = view.findViewById(R.id.getSecondNameTV);
        emailTxt = view.findViewById(R.id.getEmailTV);
        editImg = view.findViewById(R.id.editImgV);
        removeImg = view.findViewById(R.id.removeImgV);
        switchDark = view.findViewById(R.id.switchDark);
        //logOutImg = view.findViewById(R.id.logOutImgTV);
        //editRemoveProdBtn = view.findViewById(R.id.removeEditProdBtn);
        addProdBtn = view.findViewById(R.id.addProdBtn);

        //Glide.with(this).load(R.drawable.ic_baseline_edit_24).fitCenter().into(editImg);
        //Glide.with(this).load(R.drawable.ic_baseline_delete_24).centerCrop().into(removeImg);
        //Glide.with(this).load(R.drawable.ic_baseline_add_card_24).centerCrop().into(addProdBtn);

        firstNameTxt.setText(u.getName());
        secondNameTxt.setText(u.getSurname());
        emailTxt.setText(u.getOriginalEmailFrom(u.getEmail()));


        //sharedPreferences = getContext().getSharedPreferences("MODE", Context.MODE_PRIVATE);
        //editor = sharedPreferences.edit();
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES)
            switchDark.setChecked(true);
        else if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO)
            switchDark.setChecked(false);
        switchDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchDark.isChecked())
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });
         /*nightMode = sharedPreferences.getBoolean("nightMode", false);
         Log.d("MODE",String.valueOf(nightMode));
         if(nightMode) {
            switchDark.setChecked(true);
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }

        else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            //editor = sharedPreferences.edit();
            //editor.putBoolean("nightMode", true);
        }

        //editor.apply();

        switchDark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nightMode) {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_NO);
                    editor.putBoolean("nightMode", false);
                    editor.apply();
                }
                else {
                    AppCompatDelegate
                            .setDefaultNightMode(
                                    AppCompatDelegate
                                            .MODE_NIGHT_YES);
                    editor.putBoolean("nightMode", true);
                    editor.apply();
                }
            }
        });*/
        //switchDark.setChecked(false);
        /*switchDark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                else
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        })*/
        ;

        if (isAdmin == false) {
            //editRemoveProdBtn.setVisibility(View.GONE);
            addProdBtn.setVisibility(View.GONE);
        }

        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAskPwdDialog();
                editBtnClicked = true;
                addProdBtnClicked = false;
            }
        });
        removeImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAskPwdDialog();
                addProdBtnClicked = false;
                editBtnClicked = false;
            }
        });

        addProdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAskPwdDialog();
                addProdBtnClicked = true;
                editBtnClicked = false;
            }
        });

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_menu, menu);
        MenuItem item = menu.findItem(R.id.search_bar);
        item.setVisible(false);
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

    public void createAskPwdDialog() {
        dialogBuilder = new AlertDialog.Builder(getContext());
        View askPwdView = getLayoutInflater().inflate(R.layout.popup_ask_pwd, null);
        dialogBuilder.setView(askPwdView);
        dialog = dialogBuilder.create();
        dialog.show();
        pwdAskTxt = askPwdView.findViewById(R.id.pwdAskTxt);
        askPwdbtn = askPwdView.findViewById(R.id.pwdAskBtn);
        askPwdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwdAskTxt.getText().toString().isEmpty())
                    Toast.makeText(getContext(), "Please insert password", Toast.LENGTH_SHORT).show();
                else if (!u.getPwd().equals(pwdAskTxt.getText().toString()))
                    Toast.makeText(getContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                else {
                    dialog.dismiss();
                    Intent i;
                    if (editBtnClicked) {
                        i = new Intent(getContext(), Register_activity.class);
                        editBtnClicked = false;
                        i.putExtra("EDIT", u);
                        i.putExtra("isAdmin", isAdmin);
                        startActivity(i);
                    } else if (addProdBtnClicked) {
                        i = new Intent(getContext(), AddProds_activity.class);
                        i.putExtra("user", u);
                        i.putExtra("isAdmin", isAdmin);
                        startActivity(i);
                    } else {
                        Intent idelete = new Intent(getContext(), Register_activity.class);
                        daoUsers.delete(u.getEmail()).addOnSuccessListener(succDelete -> {
                            Toast.makeText(getContext(), "your account is deleted with success", Toast.LENGTH_SHORT).show();
                            startActivity(idelete);
                        }).addOnFailureListener(errDelete -> {
                            Toast.makeText(getContext(), errDelete.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                    }


                }

            }

        });
    }

    public static ProfileFragment newInstance(Users u, boolean isAdmin) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", u);
        fragment.setArguments(bundle);
        bundle.putBoolean("isAdmin", isAdmin);
        return fragment;
    }
}