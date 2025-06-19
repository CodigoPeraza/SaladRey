package com.mr21082pm18077.saladrey.ui.catalog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mr21082pm18077.saladrey.R;
import com.mr21082pm18077.saladrey.data.repo.CartRepository;

import org.jspecify.annotations.NonNull;

public class CatalogFragment extends Fragment {

    private CatalogViewModel vm;
    private CatalogAdapter adapter;

    public CatalogFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater infl, ViewGroup c,
                             Bundle s) {
        return infl.inflate(R.layout.fragment_catalog, c, false);
    }

    @Override
    public void onViewCreated(@NonNull View v, Bundle s) {
        RecyclerView rv = v.findViewById(R.id.recyclerCatalog);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new CatalogAdapter(
                salad -> CartRepository.get().add(salad)); // click -> carrito
        rv.setAdapter(adapter);

        vm = new ViewModelProvider(this).get(CatalogViewModel.class);
        vm.salads.observe(getViewLifecycleOwner(), adapter::submitList);

        TextView empty = v.findViewById(R.id.txtEmptyCatalog);
        vm.salads.observe(getViewLifecycleOwner(), list -> {
            adapter.submitList(list);
            empty.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }


}
