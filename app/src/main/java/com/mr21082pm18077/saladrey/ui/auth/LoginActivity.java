package com.mr21082pm18077.saladrey.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.mr21082pm18077.saladrey.MainActivity;
import com.mr21082pm18077.saladrey.data.repo.AuthRepository;
import com.mr21082pm18077.saladrey.databinding.ActivityLoginBinding;
import com.mr21082pm18077.saladrey.session.SessionManager;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding b;
    private AuthRepository repo;

    @Override
    protected void onCreate(Bundle s) {
        super.onCreate(s);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SessionManager.init(this);
        if (SessionManager.isLogged()) {
            startMain();
            return;
        }

        repo = new AuthRepository(this);
        b = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        b.btnLogin.setOnClickListener(v -> {
            String m = b.edtMail.getText().toString().trim();
            String p = b.edtPass.getText().toString();
            repo.login(m, p, (ok, msg) -> runOnUiThread(() -> {
                if (ok) startMain();
                else Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
            }));
        });

        b.txtGoReg.setOnClickListener(v ->
                startActivity(new Intent(this, RegisterActivity.class)));
    }

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
