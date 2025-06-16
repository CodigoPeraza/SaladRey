package com.mr21082pm18077.saladrey.ui.catalog;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.mr21082pm18077.saladrey.data.local.Salad;
import com.mr21082pm18077.saladrey.data.repo.SaladRepository;
import org.jspecify.annotations.NonNull;
import java.util.List;

public class CatalogViewModel extends AndroidViewModel {
    private final SaladRepository repo;
    public LiveData<List<Salad>> salads;

    public CatalogViewModel(@NonNull Application app) {
        super(app);
        repo = new SaladRepository(app);
        salads = repo.all();
        // Datos demo la 1ª vez
        initDemo();
    }

    /** Inserta 3 ensaladas si la tabla está vacía */
    private void initDemo() {
        salads.observeForever(list -> {
            if (list != null && list.isEmpty()) {
                repo.insert(demo("César", 4.99));
                repo.insert(demo("Mediterránea", 5.49));
                repo.insert(demo("Veggie Deluxe", 6.25));
            }
        });
    }
    private Salad demo(String n,double p){
        Salad s=new Salad(); s.name=n; s.price=p; s.imageUri="";
        return s;
    }
}
