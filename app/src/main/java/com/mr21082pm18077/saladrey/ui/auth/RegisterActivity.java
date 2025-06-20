package com.mr21082pm18077.saladrey.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mr21082pm18077.saladrey.MainActivity;
import com.mr21082pm18077.saladrey.data.repo.AuthRepository;
import com.mr21082pm18077.saladrey.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding b;
    private AuthRepository repo;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);
        repo = new AuthRepository(this);
        b = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.btnRegister.setOnClickListener(v -> {
            String m = b.edtMail.getText().toString().trim();
            String p = b.edtPass.getText().toString();
            repo.register(m, p, (ok, msg) -> runOnUiThread(() -> {
                if (ok) {
                    startActivity(new Intent(this, MainActivity.class));
                    Toast.makeText(this, "Cuenta creada", Toast.LENGTH_SHORT).show();
                    finish();   // vuelve a Login → Main
                } else {
                    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                }
            }));
        });
    }
}
