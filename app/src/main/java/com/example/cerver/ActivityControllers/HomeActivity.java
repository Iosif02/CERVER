package com.example.cerver.ActivityControllers;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.cerver.Classes.User;
import com.example.cerver.Classes.UserDetails;
import com.example.cerver.R;
import com.example.cerver.Services.FirebaseService;

import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;

import com.example.cerver.Services.UserService;
import com.example.cerver.ui.settings.SettingsFragment;
import com.example.cerver.ui.tasks.TaskFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    DrawerLayout drawerLayout, homeDrawer;
    NavigationView navigationView;
    public Menu navMenus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(this);
        navMenus = navigationView.getMenu();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);

        toggle.syncState();

        getUserDetails();
        showAdminMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.logout:
                mFirebaseAuth.signOut();
                Intent intToLogIn = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intToLogIn);
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new SettingsFragment()).commit();
                break;
            case R.id.tasks:
                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new TaskFragment()).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    protected void getUserDetails() {
        FirebaseUser fUser = mFirebaseAuth.getCurrentUser();

        if(fUser !=  null) {
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View v = navigationView.getHeaderView(0);

            String email = fUser.getEmail();
            String id = fUser.getUid();

            User user = new User(id, email);
            user.getUserDetails(new FirebaseService.UserDetailsCallback() {
                @Override
                public void onCallback(UserDetails userDetails) {
                    if(userDetails != null) {
                        TextView userNameField = (TextView) v.findViewById(R.id.user_name);
                        userNameField.setText(userDetails.getName());
                    }
                }
            });

            if(email != null) {
                TextView emailField = (TextView) v.findViewById(R.id.email);
                emailField.setText(email);
            }
            if(fUser.getPhotoUrl() != null) {
                ImageView image = (ImageView) v.findViewById(R.id.avatar);
                Glide.with(this).load(fUser.getPhotoUrl()).into(image);
            }
        }
    }

    public void showAdminMenu() {
        FirebaseUser fUser = mFirebaseAuth.getCurrentUser();
        if(fUser !=  null) {
            String userId = fUser.getUid().toString();
            UserService userService = new UserService();
            userService.isAdmin(new FirebaseService.isAdminCallback() {
                @Override
                public void onCallback(boolean isAdmin) {
                    if(isAdmin) {
                        navMenus.findItem(R.id.menu_admin).setVisible(true);
                    }
                }
            }, userId);
        }
    }
}
