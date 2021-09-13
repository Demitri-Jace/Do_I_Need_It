package com.example.do_i_need_it.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.example.do_i_need_it.Fragments.HomeFragment;
import com.example.do_i_need_it.Fragments.MapFragment;
import com.example.do_i_need_it.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //firebase
    FirebaseAuth firebaseAuth;
    FirebaseUser fUser;

    //navigation bar ids
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //firebase
        firebaseAuth = FirebaseAuth.getInstance();
        fUser = firebaseAuth.getCurrentUser();

        //navigation bar
        drawerLayout = (DrawerLayout) findViewById(R.id.home_nav_side_bar);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        NavigationView navigationView = (NavigationView) findViewById(R.id.side_nav);
        navigationView.setNavigationItemSelectedListener(this);

        if (savedInstanceState == null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new HomeFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_home);
        }

        updateNavigationHeader();
    }

    //navigation bar

    @Override
    public void onBackPressed(){

        if (drawerLayout.isDrawerOpen(GravityCompat.START)){

            drawerLayout.closeDrawer(GravityCompat.START);

        }else {

            super.onBackPressed();

        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){

            case R.id.nav_home:

                getSupportActionBar().setTitle("Home");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();

                break;

            case R.id.nav_map:

                getSupportActionBar().setTitle("Map");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MapFragment()).commit();

                break;

            case R.id.nav_share:

                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();

                /*ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
                String apk_path = applicationInfo.sourceDir;
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("application/vnd.android.package-archive");
                share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apk_path)));
                startActivity(share.createChooser(share, "Share This Application Via"));*/

                break;

            case R.id.nav_log_out:

                FirebaseAuth.getInstance().signOut();
                Intent log_in_activity = new Intent(getApplicationContext(), LogInActivity.class);
                startActivity(log_in_activity);
                finish();

                break;

        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)){

            return true;

        }
        return super.onOptionsItemSelected(item);
    }

    public void updateNavigationHeader(){

        NavigationView navigationView = (NavigationView) findViewById(R.id.side_nav);
        View header_view = navigationView.getHeaderView(0);
        TextView nav_user_name = header_view.findViewById(R.id.user_name);
        TextView nav_user_email = header_view.findViewById(R.id.user_email);
        ImageView nav_user_photo = header_view.findViewById(R.id.user_profile_photo);

        nav_user_name.setText(fUser.getDisplayName());
        nav_user_email.setText(fUser.getEmail());

        Glide.with(this).load(fUser.getPhotoUrl()).into(nav_user_photo);

    }

    //-----------------------------------------------------------------------------------------------------------------------------------------------
}