package com.example.lovelypets;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.lovelypets.fragments.CartFragment;
import com.example.lovelypets.fragments.CategoryFragment;
import com.example.lovelypets.fragments.HomeFragment;
import com.example.lovelypets.fragments.ProfileFragment;
import com.example.lovelypets.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.fragment.app.Fragment;



//TODO DELETE THIS CLASS
public class BottomActivity extends AppCompatActivity {

    private ActionBar actionBar;

    private void setActionBarTitle(String title) {
        if (actionBar != null)
            actionBar.setTitle(title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom);
        /*actionBar = getSupportActionBar();

        HomeFragment homeFragment = new HomeFragment();
        CategoryFragment categoryFragment = new CategoryFragment();
        SearchFragment searchFragment = new SearchFragment();
        CartFragment cartFragment = new CartFragment();
        ProfileFragment profileFragment = new ProfileFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();


        BottomNavigationView bottomNavView = findViewById(R.id.bottom_nav_view);
        bottomNavView.setOnItemSelectedListener(item -> {
            Fragment f = null;
            String title = "LovelyPets";
            if (item.getItemId() == R.id.menuItemHome) {
                f = homeFragment;
                title = "Home";
            } else if (item.getItemId() == R.id.menuItemCategory) {
                f = categoryFragment;
                title = "Category";
            } else if (item.getItemId() == R.id.menuItemSearch) {
                f = searchFragment;
                title = "Search";
            } else if (item.getItemId() == R.id.menuItemCart) {
                f = cartFragment;
                title = "Cart";
            } else if (item.getItemId() == R.id.menuItemProfile) {
                f = profileFragment;
                title = "Profile";
            }

            setActionBarTitle(title);
            assert f != null;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .commit();

            return true;
        });*/
    }
}