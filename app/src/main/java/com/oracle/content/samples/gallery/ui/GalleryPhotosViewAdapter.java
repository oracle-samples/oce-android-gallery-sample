/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.oracle.content.samples.gallery.R;
import com.oracle.content.sdk.model.digital.DigitalAsset;
import com.oracle.content.sdk.model.digital.RenditionType;

import java.util.List;


/**
 * ViewAdapter for the grid of gallery photos
 */
class GalleryPhotosViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<DigitalAsset> digitalAssets;
    private final RecyclerViewListener recyclerViewListener;
    private final GalleryPhotosFragment galleryFragment;

    /**
     * Interface with methods for handling interaction with recycler views
     */
    public interface RecyclerViewListener {

        /**
         * A recycled view row has been clicked upon
         *
         * @param position
         *          the position within the adapter of the row clicked upon
         */
        void onItemClicked(View view, int position);

    }

    GalleryPhotosViewAdapter(List<DigitalAsset> items, GalleryPhotosFragment fragment, RecyclerViewListener listener) {
        digitalAssets = items;
        recyclerViewListener = listener;
        galleryFragment = fragment;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_photo, parent, false);
        return new ImageHolder(view, recyclerViewListener);

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder recylerViewHolder, int position) {

        // bind values to the holder
        ImageHolder holder = (ImageHolder) recylerViewHolder;
        DigitalAsset digitalAsset = digitalAssets.get(position);

       if (digitalAsset != null) {
           holder.digitalAsset = digitalAsset;
           ImageUtil.renderAssetImage(holder.imageView, digitalAsset, RenditionType.Medium);
        }

    }

    @Override
    public int getItemCount() {
        return digitalAssets.size();
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
        final ImageView imageView;
        DigitalAsset digitalAsset;

        ImageHolder(View view, RecyclerViewListener recyclerViewListener) {
            super(view, recyclerViewListener);
            imageView = view.findViewById(R.id.photo_image);
        }
    }
}
