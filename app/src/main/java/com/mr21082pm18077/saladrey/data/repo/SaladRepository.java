package com.mr21082pm18077.saladrey.data.repo;
import android.content.Context;
import androidx.lifecycle.LiveData;
import com.mr21082pm18077.saladrey.data.local.AppDatabase;
import com.mr21082pm18077.saladrey.data.local.Salad;
import com.mr21082pm18077.saladrey.data.local.SaladDao;
import java.util.List;
import java.util.concurrent.Executors;

public class SaladRepository {
    private final SaladDao dao;
    public SaladRepository(Context ctx) {
        dao = AppDatabase.get(ctx).saladDao();
    }
    public LiveData<List<Salad>> all() { return dao.getAll(); }
    public void insert(Salad s) {
        Executors.newSingleThreadExecutor().execute(() -> dao.insert(s));
    }
    public void update(Salad s) { /* idem */ }
    public void delete(Salad s) { /* idem */ }
}
