package com.mr21082pm18077.saladrey.ui.manager;

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

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.VH> {

    private final List<Salad> data = new ArrayList<>();
    private final Actions act;

    public ManagerAdapter(Actions a) {
        act = a;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_salad, p, false);
        return new VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int pos) {
        Salad s = data.get(pos);
        h.txtName.setText(s.name);
        h.txtPrice.setText(String.format("$ %.2f", s.price));

        Glide.with(h.img.getContext())
                .load(s.imageUri.isEmpty() ? R.drawable.ic_launcher_background
                        : s.imageUri)
                .centerCrop()
                .into(h.img);

        // Tocar = editar
        h.itemView.setOnClickListener(v -> act.edit(s));

        // Mantener pulsado = borrar
        h.itemView.setOnLongClickListener(v -> {
            act.delete(s);
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void submit(List<Salad> list) {
        data.clear();
        data.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * Callbacks para editar y borrar
     */
    public interface Actions {
        void edit(Salad s);

        void delete(Salad s);
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
