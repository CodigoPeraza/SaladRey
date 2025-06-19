package com.mr21082pm18077.saladrey.ui.manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
    // 1. ActivityResultLauncher
    // ---- 1. Picker con OpenDocument (UN ÚNICO Uri) ----
    private final ActivityResultLauncher<String[]> picker =
            registerForActivityResult(new ActivityResultContracts.OpenDocument(),
                    uri -> {                    //  <-- parámetro Único
                        if (uri != null) {

                            // ► Otorgamos permiso persistente de lectura
                            requireContext().getContentResolver()
                                    .takePersistableUriPermission(
                                            uri,
                                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                                    );

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

    // 2. Lanzar el picker
    private void pickImage() {
        String[] mimeTypes = new String[]{"image/*"};
        picker.launch(mimeTypes);     // ya no necesitamos pedir permiso aquí
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
