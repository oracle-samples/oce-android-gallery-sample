/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery.ui;

import static com.oracle.content.samples.gallery.ui.PhotoViewPagerFragment.PHOTO_INDEX;
import static com.oracle.content.samples.gallery.ui.PhotoViewPagerFragment.PHOTO_URL_LIST;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.oracle.content.samples.gallery.R;
import com.oracle.content.sdk.ContentResponse;
import com.oracle.content.sdk.model.AssetSearchResult;
import com.oracle.content.sdk.model.AssetType;
import com.oracle.content.sdk.model.digital.DigitalAsset;
import com.oracle.content.sdk.model.digital.RenditionType;
import com.oracle.content.sdk.request.SearchAssetsRequest;

import java.util.ArrayList;
import java.util.List;


/**
 * The main photos fragment showing a 2 column list of photos in a grid.
 */
public class GalleryPhotosFragment extends BaseFragment implements  GalleryPhotosViewAdapter.RecyclerViewListener {


    // number of columns in the grid
    static int NUM_COLUMNS = 2;

    static String CATEGORY_ID = "categoryId";
    static String CATEGORY_NAME = "categoryName";
    protected RecyclerView recyclerView;

    protected RecyclerView.LayoutManager layoutManager;

    // list of digial assets from the server
    protected List<DigitalAsset> digitalAssets;

    // category id and name being displayed
    private String categoryNodeId;
    private String categoryName;

    public GalleryPhotosFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            categoryNodeId = getArguments().getString(CATEGORY_ID);
            categoryName = getArguments().getString(CATEGORY_NAME);

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gallery_photo_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new GridLayoutManager(getContext(), NUM_COLUMNS);
        recyclerView.setLayoutManager(layoutManager);

        setTitle(categoryName);

        // make SDK call to server to get the items to display
        fetchDigitalAssets();

        return view;
    }



    /**
     * This method uses the SDK to make an asynchronous request to search for
     * all of the items to display.
     */
    public void fetchDigitalAssets() {

        // search for all matching image assets for the specific category node id
        SearchAssetsRequest assetsRequest = new SearchAssetsRequest(getDeliveryClient())
                .type(AssetType.TYPE_ASSET_IMAGE)
                .taxonomyCategoryNodeId(categoryNodeId);

        // fetch the results asynchronously and call the specified method when done
        assetsRequest.fetchAsync(this::showDigitalAssets);

    }

    /**
     * The SDK calls this method after the search is complete.
     */
    public void showDigitalAssets(ContentResponse<AssetSearchResult> response) {

        // if there was an error, handle it and return
        if (!response.isSuccess()){
            handleContentException(response.getException());
            return;
        }

        // deserialize the result
        AssetSearchResult searchResult= response.getResult();

        // from the search result, get the complete list of items
        digitalAssets = searchResult.getDigitalAssets();

        // if the list is empty show an error
        if (digitalAssets == null || digitalAssets.size() == 0) {
            Toast.makeText(getContext(), "Search returned NO items", Toast.LENGTH_LONG).show();
            return;
        }

        final String title = getString(
                R.string.photos_category_title, categoryName, digitalAssets.size());
        setTitle(title);

        // now that we have all of the items, setup our recycler view adapter
        recyclerView.setAdapter(new GalleryPhotosViewAdapter(
                digitalAssets, this, this));


    }



    /**
     * A recycled view row has been clicked upon
     *
     * @param view     The recycled view which was clicked
     * @param position position of the recycle view
     */
    @Override
    public void onItemClicked(View view, int position) {
        GalleryPhotosViewAdapter.ImageHolder imageHolder =
                (GalleryPhotosViewAdapter.ImageHolder) recyclerView.findViewHolderForAdapterPosition(position);

        if (imageHolder != null && imageHolder.digitalAsset != null) {
            Bundle args = new Bundle();
            args.putInt(PHOTO_INDEX, position);
            // pass in full size native rendition urls
            ArrayList<String> photoUrls = new ArrayList<>();
            for (DigitalAsset digitalAsset : digitalAssets) {
                photoUrls.add(digitalAsset.getRenditionUrl(RenditionType.Native));
            }
            args.putStringArrayList(PHOTO_URL_LIST, photoUrls);

            Navigation.findNavController(imageHolder.view).navigate(R.id.action_to_photo_detail,args);

        }

    }

}
