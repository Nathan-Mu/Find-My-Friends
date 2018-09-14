package edu.monash.fit5046.fit5046a2;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private boolean enterFriendFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        enterFriendFragment = intent.getBooleanExtra("restartFriendFragment", false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setTitle("Home");
        FragmentManager fragmentManager = getFragmentManager();

        if (enterFriendFragment) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FriendsFragment()).commit();
        } else {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment nextFragment = null;
        switch (id) {
            case R.id.nav_home:
                nextFragment = new MainFragment();
                getSupportActionBar().setTitle("Home");
                break;
            case R.id.nav_update:
                nextFragment = new UpdateFragment();
                getSupportActionBar().setTitle("Update");
                break;
            case R.id.nav_search:
                nextFragment = new SearchFragment();
                getSupportActionBar().setTitle("Search");
                break;
            case R.id.nav_friends:
                nextFragment = new FriendsFragment();
                getSupportActionBar().setTitle("Friends");
                break;
            case R.id.nav_report:
                nextFragment = new ReportFragment();
                getSupportActionBar().setTitle("Report");
                break;
            case R.id.nav_logout:
                Intent newIntent = new Intent(this, MainActivity.class);
                SharedPreferences sharedPreferences = getSharedPreferences("spsMonashFriendFinder", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("autoLogin", false);
                editor.putInt("autoLoginStId", 0);
                editor.putString("autoLoginPassword", "");
                editor.commit();
                startActivity(newIntent);
                finish();
                break;
        }
        if (id != R.id.nav_logout) {
            FragmentManager fragmentManager = getFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
        return false;
    }
}
