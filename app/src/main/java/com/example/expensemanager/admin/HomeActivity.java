package com.example.expensemanager.admin;

import static java.security.AccessController.getContext;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.expensemanager.MainActivity;
import com.example.expensemanager.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    //Fragment

    private DashboardFragment dashboardFragment;
    private IncomeFragment incomeFragment;
    private UsersFragment usersFragment;
    private StatsFragment statsFragment;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homeadmin);
        mAuth=FirebaseAuth.getInstance();
        Toolbar toolbar=findViewById(R.id.my_toolbar);
        toolbar.setTitle("Admin Personal Finance Manager");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle
                (
                        this,
                        drawerLayout,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close
                )
        {};
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);


        toggle.syncState();

        NavigationView navigationView=findViewById(R.id.navView);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView=findViewById(R.id.bottomNavbar);
        frameLayout=findViewById(R.id.main_frame);

        dashboardFragment=new DashboardFragment();
        incomeFragment=new IncomeFragment();
        Fragment expenseFragment=new ExpenseFragment();
        usersFragment=new UsersFragment();
        statsFragment=new StatsFragment();
        SharedPreferences sh= getSharedPreferences("mysharepref", Context.MODE_PRIVATE);
        String uid=sh.getString("id","");
            setFragment(usersFragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){

                    case R.id.dashboard:
                        setFragment(dashboardFragment);
                        return true;
                    case R.id.income:
                        setFragment(incomeFragment);
                        return true;
                    case R.id.users:
                        setFragment(usersFragment);
                        return true;
                    case R.id.stats:
                        setFragment(statsFragment);
                        return true;
                    case R.id.expense:
                        setFragment(expenseFragment);
                        return true;

                    default:
                        return false;


                }
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);

        if(drawerLayout.isDrawerOpen(GravityCompat.END)){
            drawerLayout.closeDrawer(GravityCompat.END);
        }
        else{
            super.onBackPressed();
        }

    }

    public void displaySelectedListener(int itemId){
        Fragment fragment = null;

        switch(itemId){
            case android.R.id.home:
                DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
                drawerLayout.openDrawer(GravityCompat.START);
                return;
            case R.id.dashboard:
                bottomNavigationView.setSelectedItemId(R.id.dashboard);
                fragment=new DashboardFragment();
                break;

            case R.id.income:
                bottomNavigationView.setSelectedItemId(R.id.income);
                fragment=new IncomeFragment();
                break;

            case R.id.expense:
                bottomNavigationView.setSelectedItemId(R.id.expense);
                fragment=new UsersFragment();
                break;
            case R.id.users:
                fragment=new UsersFragment();
                break;
            case R.id.account:
                fragment=new AccountFragment();
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }
        if(fragment!=null){
            FragmentTransaction ft=getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.main_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawerLayout=findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i("ITEM ID", Integer.toString(item.getItemId()));
        displaySelectedListener(item.getItemId());
        return true;
    }
}