package com.sc2guide.sc2_guides_android;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.service.FirebaseAuthService;
import com.sc2guide.sc2_guides_android.view.auth.LogInActivity;
import com.sc2guide.sc2_guides_android.view.guides.AllFragment;
import com.sc2guide.sc2_guides_android.view.guides.ProtossFragment;
import com.sc2guide.sc2_guides_android.view.guides.ZergFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        {

    private TextView userName;
    private TextView userEmail;

    private AllFragment allFragment;
    private ZergFragment zergFragment;
    private ProtossFragment protossFragment;

    private Toolbar toolbar;
    private ActionBar ab;
    private NavigationView navigationView;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private FirebaseAuthService mAuth;


            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = new FirebaseAuthService();
        fragmentManager = getSupportFragmentManager();

        setUpVariableMap(); // map variable to view comps
        setUpToolBar();// set up toolbar
        ab = getSupportActionBar(); // must be below toolbar lel
        setUpFab ();
        setUpDrawer ();
        setUpDrawerInfo ();  // set up current user info in nav header
        setUpNavigationView();
        setUpNavigation();

        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) { return; }
            // set up allFragment as the first fragment to appear on activity
            allFragment.setArguments(getIntent().getExtras()); // get extra
            transaction = fragmentManager.beginTransaction(); // begin transaction + commit it
            transaction.add(R.id.fragment_container, allFragment).commit();
        }
    }

    private void setUpNavigation() {
        allFragment = new AllFragment();
        zergFragment = new ZergFragment();
        protossFragment = new ProtossFragment();
    }

    // @effects: map variable to layout
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
        if (drawer.isDrawerOpen(GravityCompat.START)) { // if drawer open -> backpress -> close drawer
            drawer.closeDrawer(GravityCompat.START);
        } else {
            // back button return to previous fragment
            // but when no previous it does not return to login screen
            int backStackEntryCount = getSupportFragmentManager().getBackStackEntryCount(); // get backstack count
            if (backStackEntryCount != 0) {
                super.onBackPressed();
            }
        }
    }

    /**
     * @effects : NAVIGATION
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
            //
            makeTransaction(allFragment, "ALL_FRAGMENT");
        } else if (id == R.id.nav_zerg_guides) {
            // Navigate
            makeTransaction(zergFragment, "ZERG_FRAGMENT");
        } else if (id == R.id.nav_protoss_guides) {
            // Navigate
            makeTransaction(protossFragment, "PROTOSS_FRAGMENT");
        } else if (id == R.id.nav_terran_guides) {
            setActionBarInfo("Terran Guides", "Guides for flying buildings to safety");
            // Navigate
        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_log_out) {
            // Sign user out of Firebase and navigate back to login activity
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @effects: handle drawer nav
     * @param fragment
     */
    private void makeTransaction (Fragment fragment, String tag) {
        // Bundle args = new Bundle();
        // args.putInt(ZergFragment.ARG_POSITION, position)
        // fragment.setArguments(args);
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, tag).commit();
        transaction.addToBackStack(null);
    }

    /**
     * // TODO : hard-coded. when press back button does not change title => bad
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

    private void setUpFab () {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to create guide activity
                startActivity(new Intent(MainActivity.this, CreateGuideActivity.class));
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
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }
}
