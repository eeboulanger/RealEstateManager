package com.openclassrooms.realestatemanager;

import android.animation.Animator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.models.Photo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateRealEstateActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @BindView(R.id.root_layout)
    FrameLayout rootLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.agent)
    EditText agent_tv;
    //TYPE
    @BindView(R.id.house)
    TextView house;
    @BindView(R.id.apartement)
    TextView apartement;
    @BindView(R.id.type)
    EditText type_tv;

    @BindView(R.id.surface)
    EditText surface_tv;
    @BindView(R.id.price)
    EditText price_tv;
    //SPINNERS
    @BindView(R.id.spinner_rooms)
    Spinner spinnerRooms;
    @BindView(R.id.spinner_bathrooms)
    Spinner spinnerBathrooms;

    @BindView(R.id.description)
    EditText description_tv;

    //LOCATION
    @BindView(R.id.geo_loc)
    ImageView geoLocation;
    @BindView(R.id.location)
    EditText location_tv;
    @BindView(R.id.address)
    EditText address_tv;

    //BUTTON
    @BindView(R.id.btn_send)
    Button btn_validate;

    @BindView(R.id.ic_camera)
    TextView camera_ic;
    @BindView(R.id.ic_gallery)
    TextView gallery_ic;

    // For creating real estate object
    private List<Photo> photos = new ArrayList<>();
    private String type = "";
    private String price = "";
    private String location = "";
    private String description = "";
    private String surface = "";
    private String rooms = "";
    private String bathrooms = "";
    private String address = "";
    private boolean sold = false;
    private String startDate = "";
    private String endDate = "";
    private String agent = "";
    private String[] pointsOfInterest = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // overridePendingTransition(R.anim.do_not_move, R.anim.do_not_move);
        setContentView(R.layout.activity_create_real_estate);
        ButterKnife.bind(this);
        initAnimation(savedInstanceState);
        initToolbar();
        initSpinner();
        initButton();
    }

    private void initAnimation(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            rootLayout.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        circularRevealActivity();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        }
    }

    private void circularRevealActivity() {

        int cx = rootLayout.getWidth() / 2;
        int cy = rootLayout.getHeight() - 70;

        float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight());

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, cx, cy, 0, finalRadius);
        }
        circularReveal.setDuration(1000);

        // make the view visible and start the animation
        rootLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
        initToolbar();
    }

    protected void initToolbar() {
        //Initiate toolbar to navigate back
        this.setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));
    }


    private void initSpinner() {
        //Rooms spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.rooms_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRooms.setAdapter(adapter);
        spinnerRooms.setOnItemSelectedListener(this);

        //Bathrooms spinner
        ArrayAdapter<CharSequence> adapterBathroom = ArrayAdapter.createFromResource(this,
                R.array.bathrooms_array, android.R.layout.simple_spinner_item);

        adapterBathroom.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerBathrooms.setAdapter(adapter);
        spinnerBathrooms.setOnItemSelectedListener(this);
    }

    private void initButton() {
        //Button
        btn_validate.setOnClickListener(v -> getInfoFromUI());
        //Spinners
        house.setOnClickListener(v -> getType("tag_house"));
        apartement.setOnClickListener(v -> getType("tag_apartement"));

        //Edit text type
        type_tv.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                house.setBackgroundResource(R.color.white);
                apartement.setBackgroundResource(R.color.white);
            }

            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        switch (parent.getId()) {
            case R.id.spinner_rooms:
                rooms = parent.getItemAtPosition(pos).toString();
                break;
            case R.id.spinner_bathrooms:
                bathrooms = parent.getItemAtPosition(pos).toString();
                break;
            default:
                break;
        }
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }


    private void getInfoFromUI() {
        agent = agent_tv.getText().toString();
        if (type.isEmpty()) {
            type = type_tv.getText().toString();
        }
        // photos = new ArrayList<>();
        price = price_tv.getText().toString();
        location = location_tv.getText().toString();
        description = description_tv.getText().toString();
        surface = surface_tv.getText().toString();
        address = address_tv.getText().toString();
        // startDate = "";

        //pointsOfInterest = null;
    }

    private void getType(String tag) {
        type_tv.setText("");
        switch (tag) {
            case "tag_house":
                type = "house";
                //CHANGE COLOR TO SHOW CLICKED
                house.setBackgroundResource(R.drawable.rounded_corners);
                apartement.setBackgroundResource(R.color.white);
                break;
            case "tag_apartement":
                type = "apartement";

                house.setBackgroundResource(R.color.white);
                apartement.setBackgroundResource(R.drawable.rounded_corners);
                break;
            default:
                break;

        }
    }
}
