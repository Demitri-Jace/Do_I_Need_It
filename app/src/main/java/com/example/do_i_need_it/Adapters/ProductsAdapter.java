package com.example.do_i_need_it.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.do_i_need_it.Models.Products;
import com.example.do_i_need_it.R;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ViewHolder> {

    private Context context;
    private List<Products> list;

    private OnCallBack onCallBack;

    public ProductsAdapter(Context context, List<Products> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnCallBack(OnCallBack onCallBack) {
        this.onCallBack = onCallBack;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.product_item_layout,parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.prod_title.setText(list.get(position).getName());
        holder.prod_amount.setText(list.get(position).getPrice());

        holder.prod_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCallBack.onButtonDeleteClicked(list.get(position));

            }
        });

        holder.prod_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCallBack.onButtonEditClicked(list.get(position));

            }
        });

        holder.prod_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onCallBack.onButtonShareClicked(list.get(position));

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView prod_title;
        private TextView prod_amount;
        private ImageButton prod_delete;
        private ImageButton prod_edit;
        private ImageButton prod_share;
        private CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            prod_title = itemView.findViewById(R.id.product_title);
            prod_amount = itemView.findViewById(R.id.product_amount);
            prod_delete = itemView.findViewById(R.id.product_delete_btn);
            prod_edit = itemView.findViewById(R.id.product_edit_btn);
            prod_share = itemView.findViewById(R.id.product_share_btn);
            checkBox = itemView.findViewById(R.id.checked);

        }
    }

    public interface OnCallBack{
        void onButtonDeleteClicked(Products products);
        void onButtonEditClicked(Products products);
        void onButtonShareClicked(Products products);
    }

}
