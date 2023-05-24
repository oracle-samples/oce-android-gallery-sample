/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */

package com.oracle.content.samples.gallery.ui;

import android.content.Context;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.oracle.content.samples.gallery.R;
import com.oracle.content.sdk.ContentDeliveryClient;
import com.oracle.content.sdk.ContentError;
import com.oracle.content.sdk.ContentException;


/**
 * Base fragment class to handle common functionality for fragments
 */
public class BaseFragment extends Fragment {

    // to communicate back to the main activity
    protected OnFragmentListener fragmentListener;

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentListener {
        // get the content delivery SDK object to make API calls
        ContentDeliveryClient getDeliveryClient();

        void showProgress();
        void hideProgress();
    }

    /**
     ** Get the API instance from main activity
     */
    protected ContentDeliveryClient getDeliveryClient() {
        return fragmentListener.getDeliveryClient();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            // set listener back to main activity
            fragmentListener = (OnFragmentListener) context;
        } else {
            throw new RuntimeException(context.getClass()
                    + " must implement OnGalleryFragmentInteractionListener");
        }
    }

    /**
     * Handle any SDK errors by displaying a dialog with exception details
     *
     * @param exception The ContentException that was thrown by the SDK
     */
    public void handleContentException(ContentException exception) {

        String title = getString(R.string.sdk_error);
        ContentError contentError = exception.getContentError();
        // if there is an error set in exception, use that for the title
        if (contentError != null) {
            title = contentError.getTitle();
        }

        // This alert displays the internal verbose error message that provides technical
        // detail generally used for debugging.
        new AlertDialog.Builder(getActivity())
                .setMessage(exception.getVerboseErrorMessage())
                .setTitle(title)
                .setPositiveButton("OK", null)
                .show();
    }


    // shows a spinner while waiting for async calls
    public void showProgress() {
        fragmentListener.showProgress();
    }

    // hides progress spinner
    public void hideProgress() {
        fragmentListener.hideProgress();
    }


    // set title for action bar
    protected void setTitle(String s) {
        ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(s);

    }

}
