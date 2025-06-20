package com.mr21082pm18077.saladrey.data.repo;

import android.content.Context;

import com.mr21082pm18077.saladrey.data.local.AppDatabase;
import com.mr21082pm18077.saladrey.data.local.User;
import com.mr21082pm18077.saladrey.data.local.UserDao;
import com.mr21082pm18077.saladrey.session.SessionManager;

import org.jetbrains.annotations.Nullable;

import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthRepository {

    private final UserDao dao;
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public AuthRepository(Context ctx) {
        dao = AppDatabase.get(ctx).userDao();
    }

    /**
     * Registro
     */
    public void register(String mail, String pass, Callback cb) {
        io.execute(() -> {
            String m = mail.trim().toLowerCase(Locale.US);   // ← normaliza
            if (dao.findByEmail(m) != null) {
                cb.done(false, "El correo ya existe");
                return;
            }
            User u = new User();
            u.email = m;
            u.password = pass.trim();                       // quita espacios
            u.isAdmin = m.equals("admin@salad.com");
            long id = dao.insert(u);
            u.id = id;
            SessionManager.save(u.email, u.isAdmin);
            cb.done(true, null);
        });
    }

    public void login(String mail, String pass, Callback cb) {
        io.execute(() -> {
            String m = mail.trim().toLowerCase(Locale.US);   // ← igual
            User u = dao.login(m, pass.trim());
            if (u == null) cb.done(false, "Credenciales inválidas");
            else {
                SessionManager.save(u.email, u.isAdmin);
                cb.done(true, null);
            }
        });
    }


    public interface Callback {
        void done(boolean ok, @Nullable String msg);
    }
}
