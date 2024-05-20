package com.example.lovelypets;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.lovelypets.authentications.LoginActivity;
import com.example.lovelypets.fragments.CartFragment;
import com.example.lovelypets.fragments.CategoryFragment;
import com.example.lovelypets.fragments.HomeFragment;
import com.example.lovelypets.fragments.ProfileFragment;
import com.example.lovelypets.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LovelyPetsApplicationActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseUser user;
    private TextView userEmailtextView;
    private HomeFragment homeFragment = new HomeFragment();
    private CategoryFragment categoryFragment = new CategoryFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private CartFragment cartFragment = new CartFragment();
    private ProfileFragment profileFragment = new ProfileFragment();

    NavigationView burgerNavigationView;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        assert user != null;
        Toast.makeText(this, user.getDisplayName() + " --- " + user.getEmail(), Toast.LENGTH_LONG).show();

        userEmailtextView = findViewById(R.id.user_email);

        setupBottomNavigationMenu();
        setupBurgerNavigationMenu();
    }


    public void setupBurgerNavigationMenu() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Нужно чтоб бургер открывался
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //drawer.openDrawer(GravityCompat.START);

        //Шторка
        burgerNavigationView = findViewById(R.id.burger_nav_view);

        //Шапка шторки
        View headerView = burgerNavigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView);
        navUsername.setText(getResources().getString(R.string.username));

        //TextView в пунке меню
        TextView mSlideTextView = (TextView) MenuItemCompat
                .getActionView(burgerNavigationView.getMenu().findItem(R.id.nav_cart));
        mSlideTextView.setText("5");
        mSlideTextView.setGravity(Gravity.CENTER);
        mSlideTextView.setTypeface(null, Typeface.BOLD);

        mSlideTextView.setTextColor(getResources().getColor(R.color.teal_200, getApplication().getTheme()));

        SwitchCompat mGallerySwitch = (SwitchCompat) MenuItemCompat.getActionView(burgerNavigationView.getMenu().findItem(R.id.nav_category));
        mGallerySwitch.setGravity(Gravity.CENTER);
        mGallerySwitch.setChecked(true);
        mGallerySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> Toast.makeText(getApplicationContext(), String.valueOf(isChecked), Toast.LENGTH_LONG).show());


        burgerNavigationView.setNavigationItemSelectedListener(item -> {
            Fragment f = null;
            if (item.getItemId() == R.id.nav_home) {
                f = homeFragment;
                bottomNavigationView.setSelectedItemId(R.id.menuItemHome);
            } else if (item.getItemId() == R.id.nav_category) {
                f = categoryFragment;
                bottomNavigationView.setSelectedItemId(R.id.menuItemCategory);
            } else if (item.getItemId() == R.id.nav_search) {
                f = searchFragment;
                bottomNavigationView.setSelectedItemId(R.id.menuItemSearch);
            } else if (item.getItemId() == R.id.nav_cart) {
                f = cartFragment;
                bottomNavigationView.setSelectedItemId(R.id.menuItemCart);
            } else if (item.getItemId() == R.id.nav_profile) {
                f = profileFragment;
                bottomNavigationView.setSelectedItemId(R.id.menuItemProfile);
            } else if (item.getItemId() == R.id.nav_logout) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(LovelyPetsApplicationActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;
            }
            setTitle(item.getTitle());

            assert f != null;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .commit();

            drawer.close();

            return true;
        });
    }

    public void setupBottomNavigationMenu() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment f = null;
            if (item.getItemId() == R.id.menuItemHome) {
                f = homeFragment;
                burgerNavigationView.setCheckedItem(R.id.menuItemHome);
            } else if (item.getItemId() == R.id.menuItemCategory) {
                f = categoryFragment;
                burgerNavigationView.setCheckedItem(R.id.menuItemCategory);
            } else if (item.getItemId() == R.id.menuItemSearch) {
                f = searchFragment;
                burgerNavigationView.setCheckedItem(R.id.menuItemSearch);
            } else if (item.getItemId() == R.id.menuItemCart) {
                f = cartFragment;
                burgerNavigationView.setCheckedItem(R.id.menuItemCart);
            } else if (item.getItemId() == R.id.menuItemProfile) {
                f = profileFragment;
                burgerNavigationView.setCheckedItem(R.id.menuItemProfile);
            }

            setTitle(item.getTitle());
            assert f != null;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .commit();
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isOpen()) {
            drawer.close();
        } else {
            super.onBackPressed();
        }
    }
}
