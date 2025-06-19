package com.mr21082pm18077.saladrey.ui.manager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.google.android.material.snackbar.Snackbar;
import com.mr21082pm18077.saladrey.data.local.Salad;
import com.mr21082pm18077.saladrey.data.repo.SaladRepository;
import com.mr21082pm18077.saladrey.databinding.FragmentAddEditSaladBinding;

import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NonNull;

public class AddEditSaladDialogFragment extends DialogFragment {

    private static final int REQ_READ_IMG = 100;
    private final SaladRepository repo;
    private final Salad editing; // null = alta
    private FragmentAddEditSaladBinding bind;
    private String selectedUri = "";
    private final ActivityResultLauncher<String> pick =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            bind.img.setImageURI(uri);
                            selectedUri = uri.toString();
                        }
                    });

    public AddEditSaladDialogFragment(SaladRepository r, @Nullable Salad s) {
        repo = r;
        editing = s;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle b) {
        bind = FragmentAddEditSaladBinding.inflate(getLayoutInflater());
        if (editing != null) {
            bind.edtName.setText(editing.name);
            bind.edtPrice.setText(String.valueOf(editing.price));
            if (!editing.imageUri.isEmpty())
                Glide.with(this).load(editing.imageUri).into(bind.img);
        }
        bind.img.setOnClickListener(v -> pickImage());

        AlertDialog.Builder d = new AlertDialog.Builder(requireContext())
                .setTitle(editing == null ? "Nueva ensalada" : "Editar ensalada")
                .setView(bind.getRoot())
                .setPositiveButton("Guardar", (dlg, i) -> save())
                .setNegativeButton("Cancelar", null);
        return d.create();
    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQ_READ_IMG);
        } else {
            pick.launch("image/*");
        }
    }

    @Override
    public void onRequestPermissionsResult(int r, @NonNull String[] p, @NonNull int[] g) {
        if (r == REQ_READ_IMG) {
            if (g.length > 0 && g[0] == PackageManager.PERMISSION_GRANTED) {
                pick.launch("image/*");
            } else {
                Snackbar.make(bind.getRoot(),
                        "Debes permitir acceso a tus fotos para seleccionar una imagen",
                        Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void save() {
        // 1) Nombre
        String n = bind.edtName.getText().toString().trim();
        if (n.isEmpty()) {
            bind.tilName.setError("Requerido");
            return;
        }

        // 2) Precio como String y como double
        String pTxt = bind.edtPrice.getText().toString().trim();
        double p = pTxt.isEmpty() ? 0 : Double.parseDouble(pTxt);

        // 3) Validación del precio
        if (pTxt.isEmpty() || p <= 0) {
            bind.tilPrice.setError("Precio inválido");
            return;
        }

        // 4) Crear o actualizar la ensalada
        if (editing == null) {
            Salad s = new Salad();
            s.name = n;
            s.price = p;
            s.imageUri = selectedUri;
            repo.insert(s);
        } else {
            editing.name = n;
            editing.price = p;
            editing.imageUri = selectedUri;
            repo.update(editing);
        }
    }
}
