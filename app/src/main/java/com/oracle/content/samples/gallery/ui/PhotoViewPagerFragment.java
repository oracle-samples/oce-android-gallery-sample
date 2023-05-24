/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.oracle.content.samples.gallery.R;

import java.util.ArrayList;


public class PhotoViewPagerFragment extends BaseFragment {

    protected static final String PHOTO_URL_LIST = "photoUrlList";
    protected static final String PHOTO_INDEX = "photoIndex";

    // list of image urls
    ArrayList<String> photoUrlList;

    // starting photoIndex
    int photoStartIndex;
    ViewPager mViewPager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            photoUrlList = getArguments().getStringArrayList(PHOTO_URL_LIST);
            photoStartIndex = getArguments().getInt(PHOTO_INDEX);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.photo_view_pager, container, false);
        mViewPager = v.findViewById(R.id.view_pager);

        PhotosViewPagerAdapter adapter = new PhotosViewPagerAdapter(getContext(), photoUrlList);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(photoStartIndex);

        return v;
    }


}
