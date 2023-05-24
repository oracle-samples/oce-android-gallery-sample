/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.oracle.content.samples.gallery.R;
import com.oracle.content.samples.gallery.data.GalleryCategoryData;
import com.oracle.content.samples.gallery.data.GalleryCategoryList;
import com.oracle.content.sdk.model.digital.RenditionType;


/**
 * ViewAdapter for the home page
 */
class HomePageViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // data for all the categories display on home page
    private final GalleryCategoryList list;
    private final RecyclerViewListener recyclerViewListener;
    private final HomePageFragment galleryFragment;


    public interface RecyclerViewListener {
        void onItemClicked(View view, int position);
    }

    HomePageViewAdapter(GalleryCategoryList categoryData, HomePageFragment fragment, RecyclerViewListener listener) {
        list = categoryData;
        recyclerViewListener = listener;
        galleryFragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_page_category_item, parent, false);
        return new ImageHolder(view, recyclerViewListener);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder recylerViewHolder, int position) {

        // bind values to the holder
        ImageHolder holder = (ImageHolder) recylerViewHolder;
        GalleryCategoryData categoryData = list.getCategory(position);
        if (categoryData != null) {
            holder.categoryData = categoryData;
            holder.categoryNameTextView.setText(categoryData.getCategoryName());
            final String numberOfImages = galleryFragment.getContext().getString(R.string.number_images, categoryData.numberOfPhotos());
            holder.numberOfPhotosTextView.setText(numberOfImages);

            // use first 4 photos to render - 1st one is the main image to use Medium rendition
            ImageUtil.renderAssetImage(holder.imageViewMain, categoryData.getDigitalAsset(0), RenditionType.Medium);

            // use thumbnail for 3 small images
            final RenditionType renditionType = RenditionType.Thumbnail;

            ImageUtil.renderAssetImage(holder.imageView1, categoryData.getDigitalAsset(1), renditionType);
            ImageUtil.renderAssetImage(holder.imageView2, categoryData.getDigitalAsset(2), renditionType);
            ImageUtil.renderAssetImage(holder.imageView3, categoryData.getDigitalAsset(3), renditionType);

        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        View view;
        RecyclerViewListener viewListener;

        BaseViewHolder(View view, RecyclerViewListener recyclerViewListener) {
            super(view);
            this.view = view;
            viewListener = recyclerViewListener;
            this.view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            viewListener.onItemClicked(view, getAdapterPosition());
        }
    }


    class ImageHolder extends BaseViewHolder {
        final ImageView imageViewMain;
        final ImageView imageView1;
        final ImageView imageView2;
        final ImageView imageView3;

        final TextView categoryNameTextView;
        final TextView numberOfPhotosTextView;
        GalleryCategoryData categoryData;

        ImageHolder(View view, RecyclerViewListener recyclerViewListener) {
            super(view, recyclerViewListener);
            imageViewMain = view.findViewById(R.id.mainImage);
            imageView1 = view.findViewById(R.id.smallImage1);
            imageView2 = view.findViewById(R.id.smallImage2);
            imageView3 = view.findViewById(R.id.smallImage3);
            categoryNameTextView = view.findViewById(R.id.category_name);
            numberOfPhotosTextView = view.findViewById(R.id.number_of_photos);
        }
    }
}
