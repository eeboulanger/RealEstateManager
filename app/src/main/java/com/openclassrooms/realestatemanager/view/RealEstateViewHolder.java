package com.openclassrooms.realestatemanager.view;


import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.injections.Injection;
import com.openclassrooms.realestatemanager.injections.ViewModelFactory;
import com.openclassrooms.realestatemanager.models.Photo;
import com.openclassrooms.realestatemanager.models.RealEstate;
import com.openclassrooms.realestatemanager.realEstateList.RealEstateViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RealEstateViewHolder extends RecyclerView.ViewHolder {

    //VIEW
    @BindView(R.id.photo_realestate)
    ImageView photo;
    @BindView(R.id.item_type)
    TextView type;
    @BindView(R.id.item_location)
    TextView location;
    @BindView(R.id.item_price)
    TextView price;

    //Declare callback
    private OnItemClickedListener callback;

    private Context context;

    // Declare interface that will be implemented by any container activity
    public interface OnItemClickedListener {
        void onItemClick(long id);
        boolean onNavigationItemSelected(@NonNull MenuItem item);
    }

    // Create callback to parent activity
    private void createCallbackToParentActivity(Context context) {
        try {
            //Parent activity will automatically subscribe to callback
            callback = (OnItemClickedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e.toString() + " must implement OnItemClickedListener");
        }
    }

    public RealEstateViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    private void getPhotos(long realEstateId, Context context) {
        ViewModelFactory mViewModelFactory = Injection.provideViewModelFactory(context);
        RealEstateViewModel realEstateViewModel = ViewModelProviders.of((FragmentActivity) context, mViewModelFactory).get(RealEstateViewModel.class);

        realEstateViewModel.getPhotos(realEstateId).observe((LifecycleOwner) context, this::updateWithPhoto);
    }

    private void updateWithPhoto(List<Photo> photoList) {
        String defaultImg = "https://image.freepik.com/free-vector/house-illustration_23-2147500295.jpg";
             try {
                 if (photoList.size() > 0) {
                     String photoUrl = photoList.get(0).getUrl();

                     Glide.with(context)
                        .load(photoUrl)
                        .into(photo);
                     }
            } catch (Exception e) {
                Glide.with(context)
                        .load(defaultImg)
                        .into(photo);
            }

    }

    public void updateWithItem(RealEstate realEstateItem, Context context) {
        createCallbackToParentActivity(context);
        this.context = context;

        getPhotos(realEstateItem.getId(), context);
        type.setText(realEstateItem.getType());
        location.setText(realEstateItem.getAddress());
        price.setText(realEstateItem.getPrice());
        itemView.setOnClickListener(v -> {
            // Spread the click to the parent activity
            callback.onItemClick(realEstateItem.getId());
        });
    }
}
