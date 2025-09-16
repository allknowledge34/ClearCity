package com.sachin.clearcity.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sachin.clearcity.ApplicationActivity;
import com.sachin.clearcity.R;
import com.sachin.clearcity.StatusActivity;
import com.sachin.clearcity.models.WasteModel;

import java.util.List;

public class WasteAdapter extends RecyclerView.Adapter<WasteAdapter.ViewHolder> {

    private Context context;
    private List<WasteModel> wasteList;

    public WasteAdapter(Context context, List<WasteModel> wasteList) {
        this.context = context;
        this.wasteList = wasteList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_waste_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        WasteModel waste = wasteList.get(position);

        holder.nameTxt.setText(waste.getTitle());

        if (waste.getImageUrls() != null && !waste.getImageUrls().isEmpty()) {
            Glide.with(context)
                    .load(waste.getImageUrls().get(0))
                    .placeholder(R.drawable.baseline_image_24)
                    .into(holder.img);

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, StatusActivity.class);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return wasteList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView nameTxt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            nameTxt = itemView.findViewById(R.id.nameTxt);
        }
    }
}
