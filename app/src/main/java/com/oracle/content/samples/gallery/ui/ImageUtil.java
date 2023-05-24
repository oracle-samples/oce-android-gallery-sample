/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.ui;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.oracle.content.sdk.model.digital.DigitalAsset;
import com.oracle.content.sdk.model.digital.RenditionType;

/**
 * Utility methods for rendering images based on asset object or image url.
 * Uses Glide, but could replace which whatever image library you prefer.
 */
public class ImageUtil {

    /***
     * Will render the specific asset and rendition type into the image view.
     */
    static void renderAssetImage(ImageView imageView, DigitalAsset asset, RenditionType renditionType) {
        if (asset != null) {
            renderImageUrl(imageView, asset.getRenditionUrl(renditionType));
        }
    }

    /**
     * Will render the specific url into the image view using Glide.
     */
    static void renderImageUrl(ImageView imageView, String imageUrl) {
        if (imageUrl != null) {

            // use Glide to load images
            Glide.with(imageView.getContext())
                    .load(imageUrl)
                    .into(imageView);
        }
    }
}
