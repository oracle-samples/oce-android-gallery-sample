/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.data;

import static com.oracle.content.samples.gallery.Logging.log;

import android.os.Handler;
import android.os.Looper;

import com.oracle.content.sdk.ContentDeliveryClient;
import com.oracle.content.sdk.ContentException;
import com.oracle.content.sdk.ContentResponse;
import com.oracle.content.sdk.model.AssetSearchResult;
import com.oracle.content.sdk.model.AssetType;
import com.oracle.content.sdk.model.digital.DigitalAsset;
import com.oracle.content.sdk.model.taxonomy.Taxonomy;
import com.oracle.content.sdk.model.taxonomy.TaxonomyCategory;
import com.oracle.content.sdk.model.taxonomy.TaxonomyCategoryList;
import com.oracle.content.sdk.model.taxonomy.TaxonomyList;
import com.oracle.content.sdk.request.GetTaxonomiesRequest;
import com.oracle.content.sdk.request.GetTaxonomyCategoriesRequest;
import com.oracle.content.sdk.request.SearchAssetsRequest;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Contains SDK calls to get data to display the home page.
 */
public class GalleryHomePageRepository {

    public interface Callback {
        void onSuccess(GalleryCategoryList categoryData);
        void onError(ContentException e);
    }

    private final static  Executor executor = Executors.newCachedThreadPool();

    final Callback callback;
    final ContentDeliveryClient deliveryClient;

    public GalleryHomePageRepository(ContentDeliveryClient deliveryClient, Callback callback) {
        this.callback = callback;
        this.deliveryClient = deliveryClient;
    }

    /**
     * Fetch all the data in an asynchronous thread and results come back through Callback interface.
     */
    public void fetchHomePageData() {
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                // since we've created our own thread we can use a succession of synchronous
                // calls to make all of the SDK calls.

                log("fetching all taxonomies");

                // create request to get list of taxonomies, limiting the number of results to 50 max
                GetTaxonomiesRequest request = new GetTaxonomiesRequest(deliveryClient).limit(50);
                // get the list of taxonomies synchronously
                ContentResponse<TaxonomyList> response = request.fetch();

                if (!response.isSuccess()) {
                    postError(response.getException());
                    return;
                }


                // show error if no taxonomy results came back
                TaxonomyList taxonomyList = response.getResult();
                if (taxonomyList.isEmpty()) {
                    postError(new ContentException(ContentException.REASON.itemNotFound, "No taxonomies found"));
                    return;
                }

                // use first taxonomy entry and fetch all categories in that
                Taxonomy taxonomy = taxonomyList.first();
                log("fetching categories for taxonomy:" + taxonomy.getName());

                // get list of taxonomy categories based on the taxonomy id
                GetTaxonomyCategoriesRequest categoriesRequest = new GetTaxonomyCategoriesRequest(deliveryClient, taxonomy.getId());
                // get list synchronously
                ContentResponse<TaxonomyCategoryList> categoriesResponse = categoriesRequest.fetch();

                if (!categoriesResponse.isSuccess()) {
                    postError(categoriesResponse.getException());
                    return;
                }

                TaxonomyCategoryList categoryList = categoriesResponse.getResult();
                if (categoryList.isEmpty()) {
                    postError(new ContentException(ContentException.REASON.itemNotFound, "No taxonomy categories found"));
                    return;
                }

                GalleryCategoryList galleryCategoryList = new GalleryCategoryList();

                // go through each category to get list of assets for that category
                for(TaxonomyCategory category : categoryList.getItems()) {

                    // setup request to match "Image" type and the category node id
                    SearchAssetsRequest assetsRequest = new SearchAssetsRequest(deliveryClient)
                            .type(AssetType.TYPE_ASSET_IMAGE)
                            .taxonomyCategoryNodeId(category.getId());

                    // synchronous request to get matching assets
                    ContentResponse<AssetSearchResult> searchResponse = assetsRequest.fetch();
                    if (!searchResponse.isSuccess()) {
                        postError(searchResponse.getException());
                        return;
                    }

                    AssetSearchResult searchResult = searchResponse.getResult();
                    if (searchResult.isEmpty()) {
                        postError(new ContentException(ContentException.REASON.itemNotFound, "No assets found that match taxonomy category " + category.getName()));
                        return;
                    }
                    List<DigitalAsset> digitalAssetList = searchResult.getDigitalAssets();
                    log("..found " + digitalAssetList.size() + " digital assets");

                    // add to data
                    galleryCategoryList.add(new GalleryCategoryData(
                            category.getName(),
                            category.getId(),
                            digitalAssetList));

                }

                // if we got here all the calls were successful
                handler.post(() -> callback.onSuccess(galleryCategoryList));

            }

            void postError(ContentException e) {
                log("SDK Error getting home page categories:" + e.getVerboseErrorMessage());
                handler.post(() -> callback.onError(e));
            }
        });
    }

}
