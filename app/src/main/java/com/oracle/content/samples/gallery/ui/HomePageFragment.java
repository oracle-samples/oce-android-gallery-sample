/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.ui;

import static com.oracle.content.samples.gallery.ui.GalleryPhotosFragment.CATEGORY_ID;
import static com.oracle.content.samples.gallery.ui.GalleryPhotosFragment.CATEGORY_NAME;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oracle.content.samples.gallery.R;
import com.oracle.content.samples.gallery.data.GalleryCategoryList;
import com.oracle.content.samples.gallery.data.GalleryHomePageRepository;
import com.oracle.content.sdk.ContentException;


/**
 * The main "Home Page" fragment showing a list of categories with 4 images
 */
public class HomePageFragment extends BaseFragment implements  HomePageViewAdapter.RecyclerViewListener, GalleryHomePageRepository.Callback {


    protected RecyclerView recyclerView;

    public HomePageFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_page_category_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

            // op could take a while so show spinner
            showProgress();

            // make call to server using SDK  to get categories for gallery
            GalleryHomePageRepository repository = new GalleryHomePageRepository(getDeliveryClient(), this);

            // async call will call onError or onSuccess methods
            repository.fetchHomePageData();

        }

        return view;
    }

    @Override
    public void onError(ContentException e) {
        // error getting home page
        hideProgress();
        handleContentException(e);
    }

    @Override
    public void onSuccess(GalleryCategoryList categoryData) {
        hideProgress();

        // now that we have all of the items, setup our recycler view adapter with the data
        recyclerView.setAdapter(new HomePageViewAdapter(
                categoryData, this, this));

    }


    @Override
    public void onItemClicked(View view, int position) {
        HomePageViewAdapter.ImageHolder imageHolder =
                (HomePageViewAdapter.ImageHolder) recyclerView.findViewHolderForAdapterPosition(position);

        if (imageHolder != null && imageHolder.categoryData != null) {
            Bundle args = new Bundle();
            args.putString(CATEGORY_ID, imageHolder.categoryData.getCategoryId());
            args.putString(CATEGORY_NAME, imageHolder.categoryData.getCategoryName());

            // go to the photo grid for this category
            Navigation.findNavController(imageHolder.view).navigate(R.id.action_to_photos,args);
        }

    }

}
