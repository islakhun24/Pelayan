package com.example.pelayan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    OnShareClickedListener mCallback;
    private Context mCtx;
    private List<Food> foodList;

    public FoodAdapter(Context mCtx, List<Food> foodList, OnShareClickedListener onShareClickedListener){
        this.mCtx = mCtx;
        this.foodList = foodList;
        this.mCallback = onShareClickedListener;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewtType) {
        View v = LayoutInflater.from(mCtx).inflate(R.layout.data_list,parent,false);
        return new FoodViewHolder(v);
    }

    @Override
    public void onBindViewHolder( FoodViewHolder holder, int position) {
        final Food food = foodList.get(position);

        Glide.with(mCtx).load(food.getImage()).into(holder.imageView);

        holder.tvName.setText(food.getName());
        holder.tvHarga.setText(food.getHarga());
        holder.cvList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.ShareClicked(food.getHarga().toString(),food.getImage().toString(), String.valueOf(food.getId()).toString(),food.getName().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }

    class FoodViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView tvName, tvHarga;
        CardView cvList;

        public FoodViewHolder(View v){
            super(v);

            tvName = v.findViewById(R.id.tvNamamenu);
            tvHarga = v.findViewById(R.id.tvHargamenu);
            imageView = v.findViewById(R.id.imageTampil);
            cvList = v.findViewById(R.id.cvList);

        }
    }
    public interface OnShareClickedListener {
        public void ShareClicked(String harga, String image, String id, String nama);


    }


}
