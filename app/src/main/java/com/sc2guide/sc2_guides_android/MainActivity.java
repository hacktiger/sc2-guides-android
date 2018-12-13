package com.sc2guide.sc2_guides_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.sc2guide.sc2_guides_android.service.FirebaseAuthService;
import com.sc2guide.sc2_guides_android.view.auth.LogInActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    
    private TextView userName;
    private TextView userEmail;

    private Toolbar toolbar;
    private ActionBar ab;

    private FirebaseAuthService mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = new FirebaseAuthService();
        setUpVariableMap(); // map variable to view comps
        setUpToolBar(); // set up toolbar

        ab = getSupportActionBar();
        setActionBarInfo("Home", "Guides for all races"); // set title of toolbar

        setUpFloatActionButton ();
        setUpDrawer ();
        setUpDrawerInfo ();  // set up current user info in nav header
        setUpNavigationView();
    }


    private void setUpVariableMap() {
        // get the reference in header for variable
        NavigationView headerView = findViewById(R.id.nav_view);
        View hView = headerView.getHeaderView(0);
        // map variable
        userName = hView.findViewById(R.id.nav_user_name);
        userEmail = hView.findViewById(R.id.nav_user_email);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //TODO: on back pressed change to last screen/ but not log in or sign up
            super.onBackPressed();
        }
    }

    /**
     * @effects :
     *      on user click a drawer item
     *      => navigate to a fragment/ activity + change toolbar title/subtitle
     * @param item: MenuItem
     * @return  true (if false cant press)
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // TODO: add more navigations here later
        if (id == R.id.nav_all_guides) {
            // Handle the camera action
            setActionBarInfo("Home", "Guides for all races");
        } else if (id == R.id.nav_zerg_guides) {
            // Change action bar title
            setActionBarInfo("Zerg Guides", "Guides for disgusting zerg players");
            // Navigate
        } else if (id == R.id.nav_protoss_guides) {
            setActionBarInfo("Protoss Guides", "Guides for the A-move bois");
            // Navigate
        } else if (id == R.id.nav_terran_guides) {
            setActionBarInfo("Terran Guides", "Guides for flying buildings to safety");
            // Navigate
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_log_out) {
            // Sign user out of Firebase and navigate back to login activity
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @effects: Helper method to set action bar title and subtitle
     * @param title
     * @param subtitle
     */
    public void setActionBarInfo (String title, String subtitle) {
        ab.setTitle(title);
        ab.setSubtitle(subtitle);
    }

    private void setUpToolBar () {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setUpFloatActionButton () {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void setUpDrawer () {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }


    private void setUpDrawerInfo() {
        userName.setText(mAuth.currentUser().getUid());
        userEmail.setText(mAuth.currentUser().getEmail());
    }

    private void setUpNavigationView () {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

}
