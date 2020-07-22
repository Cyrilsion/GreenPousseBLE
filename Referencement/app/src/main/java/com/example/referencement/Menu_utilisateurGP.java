package com.example.referencement;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Menu_utilisateurGP extends AppCompatActivity { //implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

   @Override
    public void onBackPressed() {
        NavigationUI.navigateUp(Navigation.findNavController(this, R.id.nav_host_fragment), mAppBarConfiguration);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_utilisateur_gp);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /** petit bouton rond en bas a droite, voir plus tard si besoin ou pas
         FloatingActionButton fab = findViewById(R.id.fab);
         fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        Snackbar.make(view, "Rempalcer avec autre chose", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        }
        });
         */
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_recherche, R.id.nav_bac, R.id.nav_compte, R.id.nav_conseil,
                R.id.nav_blog, R.id.nav_aide, R.id.nav_contact)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // listener UNIQUEMENT sur item deconnexion
        // renvoi vers page login + deconnecte l'utilisateur (supprime log des Shared Preferences)
        /*navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // supprime le nom d'utilisateur des SharedPreferences
                SaveSharedPreferences.clearUserName(getApplicationContext());
                // retourne sur login activity
                Intent monintent = new Intent(Menu_utilisateurGP.this, LoginActivity.class); // si back button on retourne dans appli
                // pour qu'on ne puisse pas retourner en arriere une fois deconnecte
                monintent.setFlags(monintent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(monintent);
                return true;
            }
        });*/
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // pour g
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

}
