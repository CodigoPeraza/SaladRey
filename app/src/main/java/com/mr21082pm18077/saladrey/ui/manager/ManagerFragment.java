package com.mr21082pm18077.saladrey.ui.manager;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mr21082pm18077.saladrey.R;
import com.mr21082pm18077.saladrey.data.local.Salad;
import com.mr21082pm18077.saladrey.data.repo.SaladRepository;

import org.jspecify.annotations.NonNull;

public class ManagerFragment extends Fragment {

    private SaladRepository repo;
    private ManagerAdapter ad;

    @Override public View onCreateView(@NonNull LayoutInflater i, ViewGroup c, Bundle s){
        return i.inflate(R.layout.fragment_manager, c,false);
    }
    @Override public void onViewCreated(@NonNull View v, Bundle s){
        repo = new SaladRepository(requireContext());
        RecyclerView rv = v.findViewById(R.id.recyclerManager);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        ad = new ManagerAdapter(new ManagerAdapter.Actions() {
            @Override public void edit(Salad s) {
                new AddEditSaladDialogFragment(repo, s)
                        .show(getParentFragmentManager(), "edit");
            }
            @Override public void delete(Salad s) {
                repo.delete(s);
            }
        });

        // Observa los cambios en tiempo real
        repo.all().observe(getViewLifecycleOwner(), ad::submit);

        // FAB flotante
        ((FloatingActionButton) requireActivity().findViewById(R.id.fab))
                .setOnClickListener(b ->
                        new AddEditSaladDialogFragment(repo,null)
                                .show(getParentFragmentManager(),"add"));
    }
}
