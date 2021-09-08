package com.example.onlychat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("username");
        String token = bundle.getString("token");

        //EditText editTextUser = findViewById(R.id.login_username);
        BottomNavigationView bottomNavigationView =(BottomNavigationView)findViewById(R.id.navigation);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new FragmentOnline()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.view_bottom_navigation_online:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new FragmentOnline()).commit();
                        //Toast.makeText(getApplicationContext(),"you click online",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.view_bottom_navigation_contacts:
                        getSupportFragmentManager().beginTransaction().replace(R.id.frag,new FragmentContacts()).commit();
                        //Toast.makeText(getApplicationContext(),"you click contacts",Toast.LENGTH_SHORT).show();

                        break;
                    case R.id.view_bottom_navigation_group:
                        Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("type","all");
                        bundle.putString("token",token);
                        bundle.putString("username",username);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
                return true;
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}