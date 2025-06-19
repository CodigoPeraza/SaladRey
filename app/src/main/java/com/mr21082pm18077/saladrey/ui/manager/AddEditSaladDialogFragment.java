package com.mr21082pm18077.saladrey.ui.manager;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.mr21082pm18077.saladrey.data.local.Salad;
import com.mr21082pm18077.saladrey.data.repo.SaladRepository;
import com.mr21082pm18077.saladrey.databinding.FragmentAddEditSaladBinding;

public class AddEditSaladDialogFragment extends DialogFragment {

    private static final int REQ_READ_IMG = 100;
    private final SaladRepository repo;
    private final Salad editing;          // null = alta
    private FragmentAddEditSaladBinding bind;
    private String selectedUri = "";
    // --------------------------- selección de imagen ---------------------------
    private final ActivityResultLauncher<String> picker =
            registerForActivityResult(new ActivityResultContracts.GetContent(),
                    uri -> {
                        if (uri != null) {
                            selectedUri = uri.toString();
                            bind.img.setImageURI(uri);
                        }
                    });

    public AddEditSaladDialogFragment(SaladRepository r, @Nullable Salad s) {
        repo = r;
        editing = s;
    }

    // --------------------------- ciclo de vida ---------------------------
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        bind = FragmentAddEditSaladBinding.inflate(getLayoutInflater());

        // Si estamos en modo edición, precargar datos
        if (editing != null) {
            bind.edtName.setText(editing.name);
            bind.edtPrice.setText(String.valueOf(editing.price));

            selectedUri = editing.imageUri;              // conservar imagen
            if (!editing.imageUri.isEmpty()) {
                Glide.with(this).load(editing.imageUri).into(bind.img);
            }
        }

        bind.img.setOnClickListener(v -> pickImage());

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(editing == null ? "Nueva ensalada" : "Editar ensalada")
                .setView(bind.getRoot())
                .setPositiveButton("Guardar", (d, i) -> save())
                .setNegativeButton("Cancelar", null);

        // -------- Botón ELIMINAR con confirmación --------
        if (editing != null) {
            builder.setNeutralButton("Eliminar", (d, i) -> confirmDelete());
        }

        return builder.create();
    }

    private void pickImage() {
        if (Build.VERSION.SDK_INT >= 33 &&
                ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.READ_MEDIA_IMAGES},
                    REQ_READ_IMG);
        } else {
            picker.launch("image/*");
        }
    }

    @Override
    public void onRequestPermissionsResult(int r, @NonNull String[] p, @NonNull int[] g) {
        if (r == REQ_READ_IMG && g.length > 0 && g[0] == PackageManager.PERMISSION_GRANTED) {
            picker.launch("image/*");
        } else if (r == REQ_READ_IMG) {
            Toast.makeText(getContext(),
                    "Necesitas conceder permiso para seleccionar imágenes",
                    Toast.LENGTH_LONG).show();
        }
    }

    // --------------------------- guardar ---------------------------
    private void save() {
        String name = bind.edtName.getText().toString().trim();
        String priceTxt = bind.edtPrice.getText().toString().trim();

        if (name.isEmpty()) {
            bind.tilName.setError("Requerido");
            return;
        }
        if (priceTxt.isEmpty()) {
            bind.tilPrice.setError("Requerido");
            return;
        }

        double price = Double.parseDouble(priceTxt);

        if (editing == null) {            // alta
            Salad s = new Salad();
            s.name = name;
            s.price = price;
            s.imageUri = selectedUri;
            repo.insert(s);
        } else {                          // edición
            editing.name = name;
            editing.price = price;
            editing.imageUri = selectedUri;
            repo.update(editing);
        }
    }

    // --------------------------- confirmar eliminación ---------------------------
    private void confirmDelete() {

        // 1.  Capturamos un contexto válido ANTES de mostrar el segundo diálogo
        final Context safeCtx = requireContext();   // todavía está adjunto aquí

        new AlertDialog.Builder(safeCtx)
                .setTitle("Eliminar ensalada")
                .setMessage("¿Seguro que deseas eliminar \"" + editing.name + "\"?")
                .setPositiveButton("Sí, eliminar", (d, i) -> {

                    repo.delete(editing);

                    // 2.  Mostrar el Toast SOLO si aún tenemos contexto
                    Toast.makeText(safeCtx,
                            "Ensalada eliminada", Toast.LENGTH_SHORT).show();

                    dismiss();          // cierra el diálogo de edición
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

}
