package com.example.pelayan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    private List<CartParameter> cartList;
    private int checkedPosition = 0;

    public CartAdapter(List<CartParameter> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list, parent, false);
        CartViewHolder cartViewHolder = new CartViewHolder(view);
        return cartViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.CartViewHolder holder, int position) {
        holder.tvNamaMenu.setText(cartList.get(position).getNama());
        holder.tvHargaMenu.setText(cartList.get(position).getHarga());
        holder.tvQty.setText(cartList.get(position).getQty()+"x");
        holder.tvSubtotal.setText(cartList.get(position).getSubtotal());
        holder.tvKeterangan.setText(cartList.get(position).getKeterangan());
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{

        TextView tvNamaMenu, tvHargaMenu, tvQty, tvSubtotal,tvKeterangan;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNamaMenu = itemView.findViewById(R.id.tvNamamenu);
            tvHargaMenu = itemView.findViewById(R.id.textViewHarga);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvSubtotal = itemView.findViewById(R.id.textViewSubtotal);
            tvKeterangan = itemView.findViewById(R.id.tvKeterangan);

        }
    }
}
