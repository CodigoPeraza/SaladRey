package com.mr21082pm18077.saladrey.data.repo;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.mr21082pm18077.saladrey.data.local.AppDatabase;
import com.mr21082pm18077.saladrey.data.local.Salad;
import com.mr21082pm18077.saladrey.data.local.SaladDao;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Acceso a datos de ensaladas (Room) en hilo de fondo.
 */
public class SaladRepository {

    private final SaladDao dao;

    /**
     * Ejecuta las operaciones I/O en un único hilo.
     */
    private final ExecutorService io = Executors.newSingleThreadExecutor();

    public SaladRepository(Context ctx) {
        dao = AppDatabase.get(ctx).saladDao();
    }

    /* ---------- LECTURA ---------- */

    public LiveData<List<Salad>> all() {
        return dao.getAll();          // Room devuelve LiveData reactivo
    }

    /* ---------- ESCRITURA ---------- */

    public void insert(Salad s) {
        io.execute(() -> {
            long newId = dao.insert(s);  // Room autogenera el ID
            s.id = newId;                // lo reflejamos en el objeto en memoria
        });
    }

    public void update(Salad s) {
        io.execute(() -> dao.update(s));
    }

    public void delete(Salad s) {
        io.execute(() -> dao.delete(s));
    }
}

