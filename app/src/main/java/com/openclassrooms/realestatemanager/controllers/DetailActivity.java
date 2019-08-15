package com.openclassrooms.realestatemanager.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.controllers.fragments.DetailFragment;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {


    // 1 - Declare detail fragment
    private DetailFragment detailFragment;

    // Create static variable to identify Intent
    public static final String EXTRA_TAG = "com.openclassrooms.myfragmentapp.Controllers.Activities.DetailActivity.EXTRA_TAG";
    private long tag;


    @Override
    public void onResume() {
        super.onResume();
        // 3 - Call update method here because we are sure that DetailFragment is visible
        this.updateDetailFragmentWithIntentTag();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);
        // 2 - Configure and show  fragment
        this.configureAndShowDetailFragment();
        setActionbar();
    }


    private void setActionbar() {
        BottomAppBar bottomAppBar = findViewById(R.id.bottom_app_bar);
        this.setSupportActionBar(bottomAppBar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        bottomAppBar.setNavigationOnClickListener(view -> startActivity(new Intent(DetailActivity.this, MainActivity.class)));
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setImageResource(R.drawable.ic_action_edit_dark);
        fab.setOnClickListener(v -> edit());
    }

    private void edit() {
        Log.e("Detail", "edit clicked with tag " + tag);
        Intent i = new Intent(this, EditActivity.class);
        i.putExtra("RealEstateId", tag);
        startActivity(i);

    }

    // --------------
    // FRAGMENTS
    // --------------

    private void configureAndShowDetailFragment() {
        // A - Get FragmentManager (Support) and Try to find existing instance of fragment in FrameLayout container
        detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_layout_detail);
        Log.e("Detail", "det frag");

        if (detailFragment == null) {
            // B - Create new fragment
            detailFragment = new DetailFragment();
            Log.e("Detail", "new det frag");
            // C - Add it to FrameLayout container
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout_detail, detailFragment)
                    .commit();
        }
    }

    // --------------
    // UPDATE UI
    // --------------

    // 2 - Update DetailFragment with tag passed from Intent
    private void updateDetailFragmentWithIntentTag() {
        // Get tag from intent
        tag = getIntent().getLongExtra(EXTRA_TAG, 0);
        // Update DetailFragment's TextView
        detailFragment.updateDetails(tag);
        Log.e("Detail", "update det frag with" + tag);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.app_bar_search:
                startActivity(new Intent(DetailActivity.this, SearchActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}