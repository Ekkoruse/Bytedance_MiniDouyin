package com.example.minidou3;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;

import com.example.minidou3.utils.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 101;
    private static final int REQUEST_INTERNET = 11;
    private String[] permissions1 = new String[] {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private String[] permissions2 = new String[] {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_hot:
                    //mTextMessage.setText("热点");
                    break;
                case R.id.navigation_post:
                    if (Utils.isPermissionsReady(MainActivity.this,permissions2))
                    {
                        startActivity(new Intent(MainActivity.this, PostActivity.class));
                    }
                    else
                    {
                        Utils.reuqestPermissions(MainActivity.this,permissions2,REQUEST_INTERNET);
                    }
                    break;
                case R.id.navigation_user:
                    startActivity(new Intent(MainActivity.this, UserActivity.class));
                    break;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view_main);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        findViewById(R.id.fab_main).setOnClickListener(v -> {
            if (Utils.isPermissionsReady(MainActivity.this,permissions1))
            {
                startActivity(new Intent(MainActivity.this, myCameraActivity.class));
            }
            else
            {
                Utils.reuqestPermissions(MainActivity.this,permissions1,REQUEST_EXTERNAL_STORAGE);
            }
        });
    }

}
