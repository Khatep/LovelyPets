package com.example.lovelypets;



import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.lovelypets.authentications.LoginActivity;
import com.example.lovelypets.enums.AuthProvider;
import com.example.lovelypets.enums.ProductType;
import com.example.lovelypets.enums.Gender;
import com.example.lovelypets.eventlisteners.OnBackPressedListener;
import com.example.lovelypets.fragments.AboutUsFragment;
import com.example.lovelypets.fragments.cart.CartFragment;
import com.example.lovelypets.fragments.category.CategoryFragment;
import com.example.lovelypets.fragments.home.HomeFragment;
import com.example.lovelypets.fragments.ProfileFragment;
import com.example.lovelypets.fragments.SearchFragment;
import com.example.lovelypets.models.Product;
import com.example.lovelypets.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.util.Objects;

public class LovelyPetsApplicationActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseUser userAuth;
    private User currentUser;
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference currentUserRef;
    private DatabaseReference usersRef;
    private HomeFragment homeFragment = new HomeFragment();
    private CategoryFragment categoryFragment = new CategoryFragment();
    private SearchFragment searchFragment = new SearchFragment();
    private CartFragment cartFragment = new CartFragment();
    private ProfileFragment profileFragment = new ProfileFragment();
    private AboutUsFragment aboutUsFragment = new AboutUsFragment();
    private NavigationView burgerNavigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, homeFragment)
                .commit();

        mAuth = FirebaseAuth.getInstance();
        userAuth = mAuth.getCurrentUser();

        setupBurgerNavigationMenu();
        setupBottomNavigationMenu();

        if (userAuth != null && userAuth.getEmail() != null) {
            String email = userAuth.getEmail();
            if (email != null) {
                findUserByEmail(email);
            }
        }

        //createProducts();
    }

    private void findUserByEmail(String userEmail) {
        usersRef = firebaseDatabase.getReference().child("users");
        usersRef.orderByChild("email").equalTo(userEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String userId = userSnapshot.getKey();
                        if (userId != null) {
                            getUserDetails(userId);
                        }
                        break;  // We found the user, no need to continue the loop
                    }
                } else {
                    Log.d("Tag", "User not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Tag", "Error finding user: ", databaseError.toException());
            }
        });
    }

    private void getUserDetails(String userId) {
        currentUserRef = firebaseDatabase.getReference().child("users").child(userId);
        currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    currentUser = new User(
                            Objects.requireNonNull(snapshot.child("email").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("password").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("name").getValue()).toString(),
                            Objects.requireNonNull(snapshot.child("surname").getValue()).toString(),
                            LocalDate.of(
                                    Integer.parseInt(Objects.requireNonNull(snapshot.child("birthDate").child("year").getValue()).toString()),
                                    Integer.parseInt(Objects.requireNonNull(snapshot.child("birthDate").child("monthValue").getValue()).toString()),
                                    Integer.parseInt(Objects.requireNonNull(snapshot.child("birthDate").child("dayOfMonth").getValue()).toString())
                            ),
                            Objects.requireNonNull(snapshot.child("phoneNumber").getValue()).toString(),
                            Gender.valueOf(Objects.requireNonNull(snapshot.child("gender").getValue()).toString()),
                            AuthProvider.valueOf(Objects.requireNonNull(snapshot.child("authProvider").getValue()).toString())
                    );
                    updateUserUI();
                    transferUserInformationToProfileFragment();
                } else {
                    Log.d("Tag", "User details not found");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Tag", "Error fetching user details: ", error.toException());
            }
        });
    }

    public void transferUserInformationToProfileFragment() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("currentUser", currentUser);
        profileFragment.setArguments(bundle);
    }
    private void updateUserUI() {
        burgerNavigationView = findViewById(R.id.burger_nav_view);
        View headerView = burgerNavigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView);
        navUsername.setText(currentUser.getEmail());
        Log.d("Tag", currentUser.toString());
    }

    public void setupBurgerNavigationMenu() {
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer,
                toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Шторка
        burgerNavigationView = findViewById(R.id.burger_nav_view);

        //Шапка шторки
        View headerView = burgerNavigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.textView);
        navUsername.setText(R.string.username);

        //TextView в пунке меню
        TextView mSlideTextView = (TextView) MenuItemCompat
                .getActionView(burgerNavigationView.getMenu().findItem(R.id.nav_cart));
        mSlideTextView.setText("5");
        mSlideTextView.setGravity(Gravity.CENTER);
        mSlideTextView.setTypeface(null, Typeface.BOLD);
        mSlideTextView.setTextColor(getResources().getColor(R.color.light_red_color, getApplication().getTheme()));

        burgerNavigationView.setNavigationItemSelectedListener(item -> {
            Fragment f = null;
            boolean showBottomNav = true;

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
                // toolbar.setNavigationIcon(null);          // to hide Navigation icon and text
                bottomNavigationView.setSelectedItemId(R.id.menuItemProfile);
            } else if (item.getItemId() == R.id.nav_about_us) {
                f = aboutUsFragment;
                showBottomNav = false;
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

            toggleBottomNavigationView(showBottomNav);
            drawer.close();
            return true;
        });
    }

    private void toggleBottomNavigationView(boolean show) {
        if (bottomNavigationView != null) {
            bottomNavigationView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
    }

    public void setupBottomNavigationMenu() {
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        setTitle(R.string.title_home);
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
                //toolbar.setNavigationIcon(null);
            }
            setTitle(item.getTitle());

            assert f != null;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, f)
                    .commit();

            toggleBottomNavigationView(true);
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (currentFragment instanceof OnBackPressedListener) {
            ((OnBackPressedListener) currentFragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
}