package com.mr21082pm18077.saladrey.ui.cart;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.mr21082pm18077.saladrey.R;
import com.mr21082pm18077.saladrey.data.repo.CartItem;
import org.jspecify.annotations.NonNull;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.VH>{

    public interface OnDelete { void del(CartItem ci); }

    private final List<CartItem> data = new ArrayList<>();
    private final OnDelete listener;

    public CartAdapter(OnDelete l){ listener=l; }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup p, int v){
        View view = LayoutInflater.from(p.getContext())
                .inflate(R.layout.item_cart,p,false);
        return new VH(view);
    }
    @Override public void onBindViewHolder(@NonNull VH h,int pos){
        CartItem ci = data.get(pos);
        h.txtQty.setText(String.valueOf(ci.qty));
        h.txtName.setText(ci.name);
        h.txtSubtotal.setText(String.format("$ %.2f", ci.subtotal()));
        h.btnDel.setOnClickListener(v -> listener.del(ci));
    }
    @Override public int getItemCount(){ return data.size(); }
    public void submit(List<CartItem> l){ data.clear(); data.addAll(l); notifyDataSetChanged(); }

    static class VH extends RecyclerView.ViewHolder{
        TextView txtQty, txtName, txtSubtotal;
        ImageButton btnDel;
        VH(View v){
            super(v);
            txtQty = v.findViewById(R.id.txtQty);
            txtName = v.findViewById(R.id.txtName);
            txtSubtotal = v.findViewById(R.id.txtSubtotal);
            btnDel = v.findViewById(R.id.btnDelete);
        }
    }
}
