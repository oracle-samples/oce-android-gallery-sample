/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.viewpager.widget.PagerAdapter;

import com.oracle.content.samples.gallery.MainActivity;
import com.oracle.content.samples.gallery.R;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Simple ViewPager implementation for scrolling through photos.
 */
class PhotosViewPagerAdapter extends PagerAdapter {

    // list of image urls
    final ArrayList<String> imageUrlList;

    final Context context;

    final LayoutInflater mLayoutInflater;

    public PhotosViewPagerAdapter(Context context, ArrayList<String> imageUrlList) {
        this.context = context;
        this.imageUrlList = imageUrlList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imageUrlList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        View itemView = mLayoutInflater.inflate(R.layout.photo_view_image, container, false);
        ImageView imageView = itemView.findViewById(R.id.image_view_photo);


        // load the image based on position
        ImageUtil.renderImageUrl(imageView, imageUrlList.get(position));

        // update title of window (e.g. 4 / 71 )
        if (context instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) context;
            final String title = context.getString(
                    R.string.photos_detail_title, position, imageUrlList.size());
            if (mainActivity.getSupportActionBar() != null) {
                mainActivity.getSupportActionBar().setTitle(title);
            }
        }

        Objects.requireNonNull(container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}