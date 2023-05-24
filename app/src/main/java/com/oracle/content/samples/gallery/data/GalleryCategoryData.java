/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.data;

import com.oracle.content.sdk.model.digital.DigitalAsset;

import java.util.List;

/**
 * Encapsulates key data about the gallery category we need for the UI including
 * the category name/id and all of the matching digital assets.
 */
public class GalleryCategoryData {
    // name of the category such as "Breakfast"
    final private String categoryName;

    // category id used for queries
    final private String categoryId;

    // full list of assets associated with this category
    final private List<DigitalAsset> digitalAssetList;

    public GalleryCategoryData(String categoryName, String categoryId, List<DigitalAsset> digitalAssetList) {
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.digitalAssetList = digitalAssetList;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int numberOfPhotos() {
        return digitalAssetList.size();
    }

    public DigitalAsset getDigitalAsset(int pos) {
        // if index is invalid return null
        if (pos < 0 || pos >= digitalAssetList.size()) {
            return null;
        } else {
            return digitalAssetList.get(pos);
        }
    }
}
