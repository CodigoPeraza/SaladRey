package com.mr21082pm18077.saladrey;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

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

        /* ---------- VISTA ---------- */
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        /* ---------- INICIALIZAR SESSION ---------- */
        SessionManager.init(this);

        /* ---------- TOOLBAR ---------- */
        setSupportActionBar(binding.appBarMain.toolbar);

        /* ---------- DRAWER & NAV ---------- */
        NavigationView navigationView = binding.navView;
        DrawerLayout drawer = binding.drawerLayout;
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,      // Catálogo
                R.id.nav_gallery,   // Carrito
                R.id.nav_manager    // Manager
        ).setOpenableLayout(drawer).build();

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        /* ---------- VISIBILIDAD MANAGER ---------- */
        boolean isAdmin = SessionManager.isAdmin();
        navigationView.getMenu().findItem(R.id.nav_manager).setVisible(isAdmin);

        /* ---------- CERRAR SESIÓN ---------- */
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_logout) {
                SessionManager.logout();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                return true;      // consumido
            }
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);
            if (handled) drawer.close();
            return handled;
        });

        /* ---------- CORREO EN EL HEADER ---------- */
        View header = navigationView.getHeaderView(0);
        TextView txtEmail = header.findViewById(R.id.txtEmail);
        txtEmail.setText(SessionManager.getEmail());

        /* ---------- OCULTAR FAB GLOBAL ---------- */
        binding.appBarMain.fab.hide();
        binding.appBarMain.fab.setOnClickListener(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController =
                Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
