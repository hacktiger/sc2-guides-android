package com.sc2guide.sc2_guides_android;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.sc2guide.sc2_guides_android.data.model.Guide;
import com.sc2guide.sc2_guides_android.service.FirebaseAuthService;
import com.sc2guide.sc2_guides_android.view.auth.LogInActivity;
import com.sc2guide.sc2_guides_android.view.common.ErrorFragment;
import com.sc2guide.sc2_guides_android.view.guides.CreateGuideFragment;
import com.sc2guide.sc2_guides_android.view.guides.GuideListFragment;
import com.sc2guide.sc2_guides_android.view.users.ProfileFragment;

import java.io.IOException;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
        {

    private TextView userName;
    private TextView userEmail;

    private GuideListFragment allFragment;
    private GuideListFragment zergFragment;
    private GuideListFragment protossFragment;
    private GuideListFragment terranFragment;
    private ProfileFragment profileFragment;
    private CreateGuideFragment createGuideFragment;

    private Toolbar toolbar;
    private ActionBar ab;
    private FloatingActionButton fab;
    private NavigationView navigationView;
    private View hView;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private ActionBarDrawerToggle toggle;
    private ProgressBar progressBar;

    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;

    private FirebaseAuthService mAuth;


            @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpVariableMap(); // map variable to view comps
        setUpToolBar();// set up toolbar
        ab = getSupportActionBar(); // must be below toolbar lel
        setUpFab ();
        setUpDrawer ();
        setUpDrawerInfo ();  // set up current user info in nav header
        setUpNavigationView();
        setUpNavigation();

        // showErrorDialog();
        initFirstFrag(savedInstanceState);
    }

    private  void initFirstFrag (Bundle savedInstanceState) {
        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) { return; }
            // set up allFragment as the first fragment to appear on activity
            transaction = fragmentManager.beginTransaction(); // begin transaction + commit it
            transaction.add(R.id.fragment_container, allFragment).commit();
        }
    }


    private void setUpNavigation() {
        allFragment = GuideListFragment.newInstance(R.color.all_guide_color, R.drawable.all_gradient,
                "All Guides", "Guides for all races", "All", "ALL_GUIDE");
        zergFragment = GuideListFragment.newInstance(R.color.zergPurple, R.drawable.zerg_gradient,
                "Zerg Guides", "Guides for disgusting zerg players", "Zerg", "ZERG_GUIDE");
        terranFragment = GuideListFragment.newInstance(R.color.terranRed, R.drawable.terran_gradient,
                "Terran Guides", "Learn how to fly buildings from base trades", "Terran", "TERRAN_GUIDE");
        protossFragment = GuideListFragment.newInstance(R.color.protossTeal, R.drawable.protoss_gradient,
                "Protoss Guides", "Guides for the A-move bois", "Protoss", "PROTOSS_GUIDE");
        // TODO: add more params later
        profileFragment = ProfileFragment.newInstance();
    }

    // @effects: map variable to layout
    private void setUpVariableMap() {
        mAuth = new FirebaseAuthService();
        fragmentManager = getSupportFragmentManager();
        // get the reference in header for variable
        NavigationView headerView = findViewById(R.id.nav_view);
        hView = headerView.getHeaderView(0);
        // map variable
        userName = hView.findViewById(R.id.nav_user_name);
        userEmail = hView.findViewById(R.id.nav_user_email);

        progressBar = findViewById(R.id.content_main_progress);
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //
        if (id == R.id.nav_all_guides) {
            navigateToFragment(allFragment, "ALL");
        } else if (id == R.id.nav_zerg_guides) {
            navigateToFragment(zergFragment, "ZERG");
        } else if (id == R.id.nav_protoss_guides) {
            navigateToFragment(protossFragment, "TERRAN");
        } else if (id == R.id.nav_terran_guides) {
            navigateToFragment(terranFragment, "PROTOSS");
        } else if (id == R.id.nav_profile) {
            navigateToFragment(profileFragment, "PROFILE");
        } else if (id == R.id.nav_log_out) {
            // Sign user out of Firebase and navigate back to login activity
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LogInActivity.class));
            finish();
        }
        // close the drawer after navigation
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pick image as profile picture
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ) {
            // get image path
            filePath = data.getData();
            try {
                // change image view => picked img
                ImageView imageView = findViewById(R.id.imageView2);
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    } // end on activity result

    /**
     * @effects: handle drawer nav for guide list
     */
    public void navigateToFragment (Fragment fragment, String tag) {
        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right); // Animation
        transaction.replace(R.id.fragment_container, fragment, tag).commit();
        transaction.addToBackStack(null);
    }

    /**
     * @effects: handle nav for guide details
     *  used in {@code GuideListFragment.onViewCreated.adapter && this.setUpFab}
     */
    public void makeTransaction(Fragment fragment, String tag, String fragName, Guide guide) {
        Bundle args = new Bundle();
        args.putString("FRAGMENT_NAME", fragName);
        if (guide != null) {
            args.putSerializable("GUIDE_OBJECT", guide);
        }
        fragment.setArguments(args);

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right); // Animation
        transaction.replace(R.id.fragment_container, fragment, tag).commit(); // replace -> frag
        transaction.addToBackStack(null);
    }

    /**
     * @effects: Helper method to set action bar title and subtitle
     *  used ib {@code this.onCreate && GuideListFragment.onResume}
     * @param title
     * @param subtitle
     */
    public void setActionBarInfo (String title, String subtitle) {
        ab.setTitle(title);
        ab.setSubtitle(subtitle);
    }

    /**
     * @effects: setting up the tool bar
     *  used in {@code this.onCreate}
     */
    private void setUpToolBar () {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

            /**
             * @effects: set up floating action button ( create new guide )
             *  used in {@code this.onCreate}
             */
    private void setUpFab () {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Navigate to create guide activity
                createGuideFragment = CreateGuideFragment.newInstance();
                makeTransaction(createGuideFragment, "CREATE_GUIDE_FRAGMENT", "CREATE_GUIDE", null);
            }
        });
    }

    private void setUpDrawer () {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public String getUserId () {
        return mAuth.currentUser().getUid();
    }
    public String getUserEmail () { return mAuth.currentUser().getEmail(); }

    private void setUpDrawerInfo() {
        userName.setText(mAuth.currentUser().getUid());
        userEmail.setText(mAuth.currentUser().getEmail());
    }

    private void setUpNavigationView () {
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
    //
    void showErrorDialog (String title, String message) {
        ErrorFragment newFragment = ErrorFragment.newInstance(
                title, message);
        newFragment.show(getSupportFragmentManager(), "dialog");
    }

    public void doPositiveClick () {
        // positive click on the error dialog
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        try {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (NullPointerException e) {
            Toast.makeText(this, "Cannot hide keyboard", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

            public FloatingActionButton getFab() {
                return fab;
            }

            public ProgressBar getProgressBar() {
                return progressBar;
            }

            public ActionBar getAb() {
                return ab;
            }

            public View gethView() {
                return hView;
            }

            public void setNavMenuItemThemeColors(int color){
                //Setting default colors for menu item Text and Icon
                int navDefaultTextColor = Color.parseColor("#202020");
                int navDefaultIconColor = Color.parseColor("#737373");

                //Defining ColorStateList for menu item Text
                ColorStateList navMenuTextList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled},
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_pressed}
                        },
                        new int[] {
                                color,
                                navDefaultTextColor,
                                navDefaultTextColor,
                                navDefaultTextColor,
                                navDefaultTextColor
                        }
                );

                //Defining ColorStateList for menu item Icon
                ColorStateList navMenuIconList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{android.R.attr.state_enabled},
                                new int[]{android.R.attr.state_pressed},
                                new int[]{android.R.attr.state_focused},
                                new int[]{android.R.attr.state_pressed}
                        },
                        new int[] {
                                color,
                                navDefaultIconColor,
                                navDefaultIconColor,
                                navDefaultIconColor,
                                navDefaultIconColor
                        }
                );

                navigationView.setItemTextColor(navMenuTextList);
                navigationView.setItemIconTintList(navMenuIconList);
            }

            public void changeUIColors(int mainColor, int gradient) {
                setNavMenuItemThemeColors(getResources().getColor(mainColor));
                ab.setBackgroundDrawable(new ColorDrawable(getResources().getColor(mainColor)));
                hView.setBackgroundResource(gradient);
            }

            public Uri getFilePath() {
                return filePath;
            }
        }
