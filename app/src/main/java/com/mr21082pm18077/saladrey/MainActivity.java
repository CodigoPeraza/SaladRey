package com.mr21082pm18077.saladrey;

import android.content.Intent;
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
import com.mr21082pm18077.saladrey.session.SessionManager;
import com.mr21082pm18077.saladrey.ui.auth.LoginActivity;

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
        boolean isAdmin = SessionManager.isAdmin();
        navigationView.getMenu().findItem(R.id.nav_manager).setVisible(isAdmin);

        if (!isAdmin) {
            navigationView.getMenu()
                    .findItem(R.id.nav_manager)
                    .setVisible(false);
        }

        // Cerrar session

        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                // 1. Vaciar sesión
                SessionManager.logout();

                // 2. Ir a LoginActivity
                startActivity(new Intent(this, LoginActivity.class));
                finish(); // cierra MainActivity
                return true; // consumido
            }
            // Para los demás ítems deja que NavigationUI haga su trabajo
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawer.close();      // opcional: cerrar drawer al cambiar destino
            return handled;
        });
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
