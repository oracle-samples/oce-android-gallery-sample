/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Encapsulates the gallery category list we need for each category including
 * name, id, number of photos and first 4 rendition urls for "preview".
 */
public class GalleryCategoryList {
    // list of categories for home page screen
    private final List<GalleryCategoryData> list = new ArrayList<>();

    public GalleryCategoryList() {}

    public int size() {
        return list.size();
    }

    public GalleryCategoryData getCategory(int pos) {
        // verify valid index
        if (pos < 0 || pos >= size()) {
            return null;
        } else {
            return list.get(pos);
        }
    }

    // add category data to list
    public void add(GalleryCategoryData data) {
        list.add(data);
    }
}
