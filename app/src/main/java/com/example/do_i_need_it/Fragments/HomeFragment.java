package com.example.do_i_need_it.Fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_i_need_it.Adapters.ProductsAdapter;
import com.example.do_i_need_it.Models.Products;
import com.example.do_i_need_it.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {

    private CheckBox checkBox_product;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference("Products");

    private List<Products> list = new ArrayList<>();
    private ProductsAdapter adapter;

    private FloatingActionButton add_product;
    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        checkBox_product = (CheckBox) view.findViewById(R.id.checked);

        add_product = (FloatingActionButton) view.findViewById(R.id.product_add_button);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        add_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogForAddingProduct();

            }
        });

        readProductData();

        return view;

    }

    private void showDialogForAddingProduct() {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_product_dialog);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        ImageButton close_btn = dialog.findViewById(R.id.close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });


        EditText edit_product_name = dialog.findViewById(R.id.product_name);
        EditText edit_product_uri = dialog.findViewById(R.id.product_uri);
        EditText edit_product_price = dialog.findViewById(R.id.product_price);
        Button add_button = dialog.findViewById(R.id.add_button);

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_product_name.getText())){

                        edit_product_name.setError("Please Fill In");

                }if (TextUtils.isEmpty(edit_product_uri.getText())) {

                    edit_product_uri.setError("Please Fill In");

                }if (TextUtils.isEmpty(edit_product_price.getText())){

                    edit_product_price.setError("Please Fill In");

                }else {

                    addProductDataToFirebase(edit_product_name.getText().toString(),
                            edit_product_uri.getText().toString(),
                            edit_product_price.getText().toString());

                    dialog.dismiss();

                }

            }
        });

        dialog.show();

    }

    private void addProductDataToFirebase(String name, String uri, String price) {

        String id = myRef.push().getKey();
        Products products = new Products(id,name,price,uri);



        myRef.child(id).setValue(products).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getActivity(), "Successful", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void readProductData() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                list.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Products value = snapshot.getValue(Products.class);
                    list.add(value);

                }

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){

                    Products value = snapshot.getValue(Products.class);
                    list.add(value);

                }

                setClicked();

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("TAG", "Failed to read Product.", error.toException());
            }
        });

    }

    private void setClicked() {

        adapter.setOnCallBack(new ProductsAdapter.OnCallBack() {
            @Override
            public void onButtonDeleteClicked(Products products) {

                deleteProduct(products);

            }

            @Override
            public void onButtonEditClicked(Products products) {

                showDialogForEditingProduct(products);

            }

            @Override
            public void onButtonShareClicked(Products products) {

                sendMessage(products);

            }
        });

    }

    private void sendMessage(Products products) {

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Purchase This Product For Me : " + "\n" + myRef.child(products.getName()).toString() +
                myRef.child(products.getPrice()).toString());
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share");
        startActivity(shareIntent);

    }

    private void deleteProduct(Products products) {

        myRef.child(products.getId()).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {

                Toast.makeText(getActivity(), "Product Deleted : "+products.getName(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void showDialogForEditingProduct(Products products) {

        Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.add_product_dialog);

        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(Objects.requireNonNull(dialog.getWindow()).getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);

        ImageButton close_btn = dialog.findViewById(R.id.close);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();

            }
        });

        final EditText edit_product_name = dialog.findViewById(R.id.product_name);
        final EditText edit_product_uri = dialog.findViewById(R.id.product_uri);
        final EditText edit_product_price = dialog.findViewById(R.id.product_price);
        Button add_button = dialog.findViewById(R.id.add_button);
        add_button.setText("Update");

        edit_product_name.setText(products.getName());
        edit_product_price.setText(products.getPrice());
        edit_product_uri.setText(products.getUri());

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(edit_product_name.getText())){

                    edit_product_name.setError("Please Fill In");

                }if (TextUtils.isEmpty(edit_product_uri.getText())) {

                    edit_product_uri.setError("Please Fill In");

                }if (TextUtils.isEmpty(edit_product_price.getText())){

                    edit_product_price.setError("Please Fill In");

                }else {

                    updateProductData(products, edit_product_name.getText().toString(),
                            products, edit_product_price.getText().toString(),
                            products, edit_product_uri.getText().toString());

                    dialog.dismiss();

                }

            }
        });

        dialog.show();

    }

    private void updateProductData(Products products, String edit_name, Products products1, String edit_price, Products products2, String edit_uri) {

        myRef.child(products.getId()).child("name").setValue(edit_name).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getActivity(), "Product Name Updated", Toast.LENGTH_SHORT).show();

            }
        });

        myRef.child(products1.getId()).child("price").setValue(edit_price).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getActivity(), "Product Price Updated", Toast.LENGTH_SHORT).show();

            }
        });

        myRef.child(products2.getId()).child("uri").setValue(edit_uri).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(getActivity(), "Product Uri Updated", Toast.LENGTH_SHORT).show();

            }
        });

    }

}