package com.example.referencement;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.referencement.models.LoginModel;
import com.example.referencement.viewmodels.LoginViewModel;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import static com.example.referencement.Fragments.LoginFragment.LOGIN_SUCCESSFUL;

public class Menu_utilisateurGP extends AppCompatActivity { //implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

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

        //TODO : -Onglet conseils + API pr récup
        // -Onglet Mon compte
        // -Onglet Blog
        // -Onglet aide
        // -Onglet contact
        // -Checker sécurité
        // -checker perfs
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
        navigationView.getMenu().findItem(R.id.nav_logout).setOnMenuItemClickListener(menuItem -> {
            // supprime le nom d'utilisateur des SharedPreferences
            SaveSharedPreferences.clearUserName(getApplicationContext());
            // on indique la deco et on retourne sur la page d'accueil
            LoginViewModel loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
            loginViewModel.init();
            loginViewModel.getLoginResult().getValue().logout();
            // retourne sur login activity
            navController.navigate(R.id.nav_bac);

            return true;
        });
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
