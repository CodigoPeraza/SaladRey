package com.mr21082pm18077.saladrey.ui.cart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mr21082pm18077.saladrey.R;
import org.jspecify.annotations.NonNull;

public class CartFragment extends Fragment {

    private CartViewModel vm;
    private CartAdapter ad;
    private TextView txtTotal;

    public CartFragment(){}

    @Override public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle s){
        return i.inflate(R.layout.fragment_cart,c,false);
    }
    @Override public void onViewCreated(@NonNull View v, Bundle s){
        RecyclerView rv = v.findViewById(R.id.recyclerCart);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ad = new CartAdapter(ci -> vm.delete(ci));
        rv.setAdapter(ad);

        txtTotal = v.findViewById(R.id.txtTotal);

        vm = new ViewModelProvider(this).get(CartViewModel.class);
        vm.items.observe(getViewLifecycleOwner(), list -> {
            ad.submit(list);
            txtTotal.setText(String.format("Total: $ %.2f", vm.total()));
        });

        v.findViewById(R.id.btnClear).setOnClickListener(b -> vm.clear());
        v.findViewById(R.id.btnPay).setOnClickListener(b ->
                Toast.makeText(getContext(),"¡Pedido confirmado!",Toast.LENGTH_SHORT).show()
        );
    }
}
