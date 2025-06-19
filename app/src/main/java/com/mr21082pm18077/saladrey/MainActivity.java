package com.mr21082pm18077.saladrey;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.mr21082pm18077.saladrey.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1) Inflar el binding y fijar la vista
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 2) Obtener referencias a vistas
        NavigationView navigationView = binding.navView;
        DrawerLayout drawer = binding.drawerLayout;

        // 3) Toolbar
        setSupportActionBar(binding.appBarMain.toolbar);

        // 4) FAB: acción de Snackbar y navegación al carrito
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        navController.addOnDestinationChangedListener((controller, destination, args) -> {
            if (destination.getId() == R.id.nav_home) {          // Catálogo
                binding.appBarMain.fab.show();
                binding.appBarMain.fab.setOnClickListener(v ->
                        navController.navigate(R.id.action_catalog_to_cart));
            } else {
                binding.appBarMain.fab.hide();
                binding.appBarMain.fab.setOnClickListener(null); // previene leaks
            }
        });

        // 5) Configurar NavigationUI con el Drawer
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,      // Catálogo
                R.id.nav_gallery,   // Carrito
                R.id.nav_manager    // Manager
        ).setOpenableLayout(drawer).build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // 6) Ocultar ítem “Manager” si el usuario no es admin
        boolean isAdmin = getSharedPreferences("prefs", MODE_PRIVATE)
                .getBoolean("isAdmin", true);
        if (!isAdmin) {
            navigationView.getMenu()
                    .findItem(R.id.nav_manager)
                    .setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflar el menú del toolbar
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(
                this, R.id.nav_host_fragment_content_main
        );
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
