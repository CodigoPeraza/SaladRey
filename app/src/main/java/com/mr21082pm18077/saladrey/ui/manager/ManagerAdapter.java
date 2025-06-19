package com.mr21082pm18077.saladrey.ui.manager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mr21082pm18077.saladrey.R;
import com.mr21082pm18077.saladrey.data.local.Salad;

import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.VH> {

    public interface Actions {
        void edit(Salad s);
        void delete(Salad s);
    }

    private final List<Salad> data = new ArrayList<>();
    private final Actions actions;

    public ManagerAdapter(Actions act) { this.actions = act; }

    @NonNull @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_manager_salad, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Salad s = data.get(pos);
        h.txtName.setText(s.name);
        h.txtPrice.setText(String.format("$ %.2f", s.price));
        Glide.with(h.img.getContext())
                .load(s.imageUri.isEmpty() ? R.drawable.ic_launcher_background : s.imageUri)
                .into(h.img);

        h.btnEdit.setOnClickListener(v -> actions.edit(s));
        h.btnDelete.setOnClickListener(v -> actions.delete(s));
    }

    @Override
    public int getItemCount() { return data.size(); }   // ← ¡IMPRESCINDIBLE!

    public void submit(List<Salad> list) {
        data.clear(); data.addAll(list); notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img; TextView txtName, txtPrice;
        ImageButton btnEdit, btnDelete;
        VH(View v) {
            super(v);
            img       = v.findViewById(R.id.imgSalad);
            txtName   = v.findViewById(R.id.txtName);
            txtPrice  = v.findViewById(R.id.txtPrice);
            btnEdit   = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
