package com.rajkishorbgp.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.rajkishorbgp.onlineshopping.fragment.AccountInformationFragment;
import com.rajkishorbgp.onlineshopping.fragment.CartFragment;
import com.rajkishorbgp.onlineshopping.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView toolbarText;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        toolbar = findViewById(R.id.toolbar);
        toolbarText = findViewById(R.id.toolbarText);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);

        setSupportActionBar(toolbar);

        // Status bar color
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.black));

        // SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);

        // Navigation header
        View headerView = navigationView.getHeaderView(0);
        TextView profileName = headerView.findViewById(R.id.profileName);
        TextView profileEmail = headerView.findViewById(R.id.profileEmail);

        profileName.setText(sharedPreferences.getString("name", ""));
        profileEmail.setText(sharedPreferences.getString("email", ""));

        // Drawer toggle
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.open,
                R.string.close
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Default fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, new HomeFragment())
                .commit();

        toolbarText.setText("Home");

        // Navigation item click handling (FIXED)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();

                if (id == R.id.homeMenu) {
                    loadFragment(new HomeFragment(), "Home");

                } else if (id == R.id.accountInformation) {
                    loadFragment(new AccountInformationFragment(), "Account Information");

                } else if (id == R.id.cartMenu) {
                    loadFragment(new CartFragment(), "Cart");

                } else if (id == R.id.signOutMenu) {
                    signOut();
                }

                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void loadFragment(Fragment fragment, String title) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, fragment)
                .commit();
        toolbarText.setText(title);
    }

    private void signOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("userDetails", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", false);
        editor.apply();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
