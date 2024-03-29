package com.openclassrooms.realestatemanager.controllers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.DetailFragment;
import com.openclassrooms.realestatemanager.controllers.fragments.MainFragment;
import com.openclassrooms.realestatemanager.view.PhotoAdapter;
import com.openclassrooms.realestatemanager.view.RealEstateViewHolder;

import org.jetbrains.annotations.Nullable;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RealEstateViewHolder.OnItemClickedListener,
        NavigationView.OnNavigationItemSelectedListener, PhotoAdapter.PhotoViewHolder.OnItemClickedListener {

    // Create static variable to identify Intent
    public static final String EXTRA_TAG = "com.openclassrooms.myfragmentapp.Controllers.Activities.MainActivity.EXTRA_TAG";
    public static final String CHANNEL_ID = "com.openclassrooms.myfragmentapp.Controllers.Activities.MainActivity.CHANNEL_REAL_ESTATE";

    private BottomAppBar bottomAppBar;
    private DetailFragment detailFragment;
    private MainFragment mainFragment;
    private long tag = -1;
    private Boolean tablet = false;

    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.activity_drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.main_nav_view)
    NavigationView navigationView;

    @Override
    public void onResume() {
        super.onResume();
        // Call update method here because we are sure that DetailFragment is visible
        this.updateDetailFragmentWithIntentTag();
        if (!tablet) {
            bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
            fab.setImageResource(R.drawable.ic_action_add_white);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkIfTablet();
        this.configureAndShowDetailFragment();
        this.showMainFragment();
        createNotificationChannel();
    }

    private void checkIfTablet() {
        // WILL BE FALSE IF TABLET
        //IF PHONE SHOW BOTTOM APP BAR WITH FAB
        if (getResources().getBoolean(R.bool.portrait_only)) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setActionbarPhone();
        }
        //IF TABLET, SHOW TOOLBAR
        if (findViewById(R.id.frame_layout_detail) != null) {
            tablet = true;
            setActionbarTablet();
        }
    }

    private void setActionbarPhone() {
        bottomAppBar = findViewById(R.id.bottom_app_bar);
        this.setSupportActionBar(bottomAppBar);
        configureDrawerLayout(bottomAppBar, null);

        bottomAppBar.setFabAlignmentMode(BottomAppBar.FAB_ALIGNMENT_MODE_CENTER);
        fab.setImageResource(R.drawable.ic_action_add_white);
        fab.setOnClickListener(v -> createRealEstate());
    }


    //  Start create activity when click on fab if phone
    private void createRealEstate() {
        startActivity(new Intent(this, CreateActivity.class));
    }

    private void setActionbarTablet() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar(toolbar);
        configureDrawerLayout(null, toolbar);

        fab.setImageResource(R.drawable.ic_edit_white);
        fab.setOnClickListener(v -> edit());
    }

    //Start edit activity when click on fab if tablet
    private void edit() {
        if (tag != -1) {
            Intent i = new Intent(this, EditActivity.class);
            i.putExtra(DetailActivity.EXTRA_TAG, tag);
            startActivity(i);
        } else {
            Toast.makeText(this, R.string.choose_item_to_edit, Toast.LENGTH_SHORT).show();
        }
    }

    private void showMainFragment() {
        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_main);
        if (mainFragment == null) {
            mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_main, mainFragment)
                    .commit();
        }
    }

    private void configureAndShowDetailFragment() {
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);

        //only add DetailFragment in Tablet mode (If found frame_layout_detail)
        if (detailFragment == null && findViewById(R.id.frame_layout_detail) != null) {
            detailFragment = new DetailFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_detail, detailFragment)
                    .commit();
        }
    }

    //FOR TABLET ONLY - SHOW REAL ESTATE IN FRAGMENT IF STARTED FROM MAP ACTIVITY
    private void updateDetailFragmentWithIntentTag() {
        if (detailFragment != null) {
            // Get marker tag from intent if it comes from map activity
            Intent intent = getIntent();
            if (intent.hasExtra(EXTRA_TAG)) {
                long tagFromMap = getIntent().getLongExtra(EXTRA_TAG, 0);
                // Update DetailFragment
                detailFragment.updateDetails(tagFromMap);
            }
        }
    }

    // --------------
    // CallBack
    // Handle click in list view to show real estate in detail fragment
    // --------------
    @Override
    public void onItemClick(long id) {
        tag = id;
        // Check if phone, then open DetailActivity
        if (detailFragment == null) {
            Intent i = new Intent(this, DetailActivity.class);
            i.putExtra(DetailActivity.EXTRA_TAG, id);
            startActivity(i);
        } else {
            //If tablet
            detailFragment.updateDetails(id);
        }
    }


    /**
     * Handle click in action bar
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_app_bar_menu, menu);

        // REMOVE CREATE ITEM FROM TOOLBAR IF PHONE
        if (!tablet) {
            menu.removeItem(R.id.app_bar_add);
            supportInvalidateOptionsMenu();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.app_bar_search:
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
                return true;

            case R.id.app_bar_add:
                startActivity(new Intent(MainActivity.this, CreateActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * For Drawer Layout
     */
    private void configureDrawerLayout(BottomAppBar bottomAppBar, Toolbar toolbar) {
        ActionBarDrawerToggle toggle;
        //TABLET
        if (bottomAppBar == null) {
            toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
        } else {
            //PHONE
            toggle = new ActionBarDrawerToggle(this, drawerLayout, bottomAppBar, R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close);
        }
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        initDrawerHeader();
    }

    private void initDrawerHeader() {
        //Inflate header layout
        navigationView.inflateHeaderView(R.layout.drawer_header);
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_map:
                //Start Map view
                startActivity(new Intent(MainActivity.this, MapActivity.class));
                break;
            case R.id.action_euro:
                mainFragment.convertCurrency("euro");
                break;
            case R.id.action_dollar:
                mainFragment.convertCurrency("dollar");
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public void onItemClick(@Nullable Long id, @Nullable Integer position) {
        detailFragment.onItemClick(id, position);
    }
}
