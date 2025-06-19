package com.mr21082pm18077.saladrey.ui.manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.mr21082pm18077.saladrey.data.local.Salad;
import com.mr21082pm18077.saladrey.data.repo.SaladRepository;
import com.mr21082pm18077.saladrey.databinding.FragmentAddEditSaladBinding;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class AddEditSaladDialogFragment extends DialogFragment {

    private FragmentAddEditSaladBinding bind;
    private final SaladRepository repo;
    private Salad editing; // null = alta

    public AddEditSaladDialogFragment(SaladRepository r, @Nullable Salad s){
        repo=r; editing=s;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle b){
        bind = FragmentAddEditSaladBinding.inflate(getLayoutInflater());
        if(editing!=null){
            bind.edtName.setText(editing.name);
            bind.edtPrice.setText(String.valueOf(editing.price));
            if(!editing.imageUri.isEmpty())
                Glide.with(this).load(editing.imageUri).into(bind.img);
        }
        bind.img.setOnClickListener(v->pickImage());

        AlertDialog.Builder d = new AlertDialog.Builder(requireContext())
                .setTitle(editing==null? "Nueva ensalada":"Editar ensalada")
                .setView(bind.getRoot())
                .setPositiveButton("Guardar",(dlg,i)->save())
                .setNegativeButton("Cancelar",null);
        return d.create();
    }

    private final ActivityResultLauncher<String> pick =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> { if(uri!=null) { bind.img.setImageURI(uri); selectedUri=uri.toString(); } });
    private String selectedUri="";

    private void pickImage(){ pick.launch("image/*"); }

    private void save(){
        String n = bind.edtName.getText().toString().trim();
        double p = Double.parseDouble(bind.edtPrice.getText().toString());
        if(editing==null){
            Salad s=new Salad(); s.name=n; s.price=p; s.imageUri=selectedUri;
            repo.insert(s);
        }else{
            editing.name=n; editing.price=p; editing.imageUri=selectedUri;
            repo.update(editing);
        }
    }
}
