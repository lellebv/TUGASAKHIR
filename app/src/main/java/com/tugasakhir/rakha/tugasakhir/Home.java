package com.tugasakhir.rakha.tugasakhir;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Home extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    HomeFragment homeFragment = new HomeFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, homeFragment);
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_dashboard:

                    Maps2Fragment homeFragments = new Maps2Fragment();
                    FragmentTransaction fragmentTransactions = getSupportFragmentManager().beginTransaction();
                    fragmentTransactions.replace(R.id.content, homeFragments);
                    fragmentTransactions.commit();
                    return true;

                case R.id.navigation_notifications:

                    UserFragment homeFragmentss = new UserFragment();

                     FragmentTransaction fragmentTransactionss = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionss.replace(R.id.content, homeFragmentss);
                    fragmentTransactionss.commit();
                    return true;

            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Inisialisasi fragment maps
        HomeFragment homeFragment = new HomeFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content, homeFragment);
        fragmentTransaction.commit();

        //inisialisasi BottomNavigationView
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //membuat event click dengan memanggil function listener mOnNavigationItemSelectedListener diatas
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    public void onBackPressed(){
        moveTaskToBack(false);
    }


}
