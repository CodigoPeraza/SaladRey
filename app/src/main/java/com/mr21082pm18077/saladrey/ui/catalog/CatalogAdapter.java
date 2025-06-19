package com.mr21082pm18077.saladrey.ui.catalog;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mr21082pm18077.saladrey.R;
import com.mr21082pm18077.saladrey.data.local.Salad;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.VH> {

    private final List<Salad> data = new ArrayList<>();
    private final OnAddClick listener;
    public CatalogAdapter(OnAddClick l) {
        this.listener = l;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_salad, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Salad s = data.get(pos);

        h.txtName.setText(s.name);
        h.txtPrice.setText(String.format("$ %.2f", s.price));

        // Cargar imagen real si existe
        if (!s.imageUri.isEmpty()) {
            Glide.with(h.img.getContext())
                    .load(Uri.parse(s.imageUri))
                    .placeholder(R.drawable.ic_launcher_background)   // opcional
                    .into(h.img);
        } else {
            h.img.setImageResource(R.drawable.ic_launcher_background);
        }

        h.itemView.setOnClickListener(v -> listener.addToCart(s));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void submitList(List<Salad> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    public interface OnAddClick {
        void addToCart(Salad salad);
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtName, txtPrice;
        ImageView img;

        VH(View v) {
            super(v);
            txtName = v.findViewById(R.id.txtName);
            txtPrice = v.findViewById(R.id.txtPrice);
            img = v.findViewById(R.id.imgSalad);
        }
    }
}
