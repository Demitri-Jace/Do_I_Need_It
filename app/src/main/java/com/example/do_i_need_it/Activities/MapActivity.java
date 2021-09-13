package com.example.do_i_need_it.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.do_i_need_it.R;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        /*Fragment fragment = new MapFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();*/

    }

}