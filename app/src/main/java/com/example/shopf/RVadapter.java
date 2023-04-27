package com.example.shopf;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageMetadata;

import java.util.ArrayList;

public class RVadapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private final RVinterface rVinterface;
    private boolean isAdmin = false;
    DAOprods dao = new DAOprods();
    DAOusers daOusers = new DAOusers();
    ArrayList<Prods> list = new ArrayList<>();
    TextView pwdAskTxt;
    Button askPwdbtn;
    AlertDialog.Builder dialogBuilder;
    AlertDialog dialog;
    Users u;

    public RVadapter(Context ctx, RVinterface rVinterface) {
        this.context = ctx;
        this.rVinterface = rVinterface;

    }

    public void setItems(ArrayList<Prods> pharmaProds) {
        list.clear();
        list.addAll(pharmaProds);
        Log.d("list", String.valueOf(list));
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_view, parent, false);
        return new ProdsVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProdsVH vh = (ProdsVH) holder;
        Prods p = list.get(position);
        Log.d("P", String.valueOf(p));
        //Log.d("P",String.valueOf(dao.getStorageReference()));
        if (p.getSrcImg() != null) {

            Glide.with(context).load(p.getSrcImg()).into(vh.prodImg);
        }
        vh.txtName.setText(p.getName());
        vh.txtPrice.setText(p.getPrice().toString() + "€");
        //Log.d("rating",String.valueOf(p.getRating()));
        //Log.d("object",String.valueOf(vh.txtRating));
        vh.txtRating.setIsIndicator(true);
        if (p.getRating() != null)
            vh.txtRating.setRating(p.getRating());
        else vh.txtRating.setRating(3.0f);
        if (!getIsAdmin())
            vh.txtOptions.setVisibility(View.GONE);
        vh.txtOptions.setOnClickListener(v -> {

            PopupMenu popupMenu = new PopupMenu(context, vh.txtOptions);
            popupMenu.inflate(R.menu.options_menu);
            createAskPwdDialog(popupMenu);
            popupMenu.setOnMenuItemClickListener(item -> {

                switch (item.getItemId()) {
                    case R.id.menu_edit:
                        Intent i = new Intent(context, AddProds_activity.class);
                        i.putExtra("EDIT", p);
                        i.putExtra("user", this.getUser());
                        i.putExtra("isAdmin", this.getIsAdmin());
                        context.startActivity(i);
                        break;
                    case R.id.menu_remove:
                        DAOprods dao = new DAOprods();

                        dao.delete(p.getKey()).addOnSuccessListener(suc ->
                        {

                            Toast.makeText(context, "Record is removed", Toast.LENGTH_SHORT).show();
                            list.remove(position);
                            this.notifyItemRangeChanged(position, getItemCount());
                            this.notifyItemRemoved(position);

                            //Intent intent = new Intent(context, MainActivity.class);
                            //context.startActivity(intent);
                        }).addOnFailureListener(er ->
                        {
                            Toast.makeText(context, er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        break;
                }
                return false;
            });
            //popupMenu.show();

        });
        vh.rvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("LISTCLICK", String.valueOf(list.get(holder.getAdapterPosition()).getSrcImg()));
                rVinterface.onItemClick(list.get(holder.getAdapterPosition()));

            }
        });
    }

    @Override
    public int getItemCount() {
        //return list.size();
        return list.size();
    }

    public boolean getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {

        this.isAdmin = isAdmin;


    }

    public Users getUser() {
        return this.u;
    }

    public void setUser(Users u) {
        this.u = u;
    }

    public void updateList(ArrayList<Prods> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }
    public void createAskPwdDialog(PopupMenu popupMenu) {
        dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View askPwdView = li.inflate(R.layout.popup_ask_pwd, null);
        dialogBuilder.setView(askPwdView);
        dialog = dialogBuilder.create();
        dialog.show();
        /*try {
            Dialog dialog = new Dialog(context);
            dialog.setCancelable(true);
            dialog.setContentView(R.layout.popup_ask_pwd);
            dialog.show();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }*/
        pwdAskTxt = askPwdView.findViewById(R.id.pwdAskTxt);
        askPwdbtn = askPwdView.findViewById(R.id.pwdAskBtn);
        askPwdbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwdAskTxt.getText().toString().isEmpty())
                    Toast.makeText(context, "Please insert password", Toast.LENGTH_SHORT).show();
                else if (!u.getPwd().equals(pwdAskTxt.getText().toString()))
                    Toast.makeText(context, "Wrong password", Toast.LENGTH_SHORT).show();
                else {
                    dialog.dismiss();
                    popupMenu.show();
                }
            }
        });
    }
}
