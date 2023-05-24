/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.oracle.content.samples.gallery.ui.BaseFragment;
import com.oracle.content.samples.gallery.ui.SettingsFragment;
import com.oracle.content.sdk.CacheSettings;
import com.oracle.content.sdk.ContentDeliveryClient;
import com.oracle.content.sdk.ContentLogging;
import com.oracle.content.sdk.ContentSDK;
import com.oracle.content.sdk.ContentSettings;

/**
 * Main activity for gallery sample app
 */
public class MainActivity extends AppCompatActivity implements
        BaseFragment.OnFragmentListener,
        SettingsFragment.OnSettingsChangedListener {

    final static String TAG = "MainActivity";

    // main object for dealing with SDK calls
    private ContentDeliveryClient deliveryClient;


    // Progress bar when loading page
    public ProgressBar progressBar;
    boolean isProgressOn = false;

    /**
     * Get the Client API to make SDK calls
     */
    @Override
    public ContentDeliveryClient getDeliveryClient() {
        return deliveryClient;
    }

    /**
     * Construct a content delivery API to be used by the app.  This should be called again
     * if settings have changed (e.g. cache settings)
     */
    private void createDeliveryClient() {

        Context context = getApplicationContext();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(context);
        ContentSettings contentSettings = new ContentSettings();

        // is cache enabled?
        boolean cacheEnabled = settings.getBoolean("pref_key_cache_enabled", true);
        if (cacheEnabled) {
            // SDK: initialize the cache policy
            contentSettings.setCacheSettings(new CacheSettings(context.getCacheDir()));
        }

        // is logging enabled?
        Logging.isEnabled = settings.getBoolean("pref_key_logging_enabled", true);
        if (Logging.isEnabled) {
            ContentSDK.setLogLevel(ContentLogging.LogLevel.HTTP);
        }


        try {

            // if you want to make the url/channel token editable and get from prefs
            //HttpUrl host = HttpUrl.parse(settings.getString("pref_key_hostname_app", context.getString(R.string.default_server_url)));
            // String channelToken = settings.getString("pref_key_access_token_app", context.getString(R.string.default_access_token));

            // get the host channel token set in string resources (hard-coded)
            String channelToken = context.getString(R.string.default_channel_token);
            String host = context.getString(R.string.default_server_url);

            // SDK: create client API we'll use to make calls
            // this example shows overridden connection timeout to 30 seconds and enabling logging
            deliveryClient = ContentSDK.createDeliveryClient(host, channelToken, contentSettings);

        } catch (Exception e) {
            Log.d(TAG, "error creating delivery client" + e);
        }

        // if there is no delivery client, show error
        if (deliveryClient == null) {
            Toast.makeText(getApplicationContext(), R.string.client_api_error, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onSettingChanged(String key) {
        // settings changed, so re-create the delivery client
        createDeliveryClient();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // create the Client API to be used for all SDK cal;ls
        createDeliveryClient();
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progress_loader);

        // setup navigation
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);

    }

    @Override
    public boolean onSupportNavigateUp() {
        Navigation.findNavController(this, R.id.nav_host_fragment).navigateUp();
        return super.onSupportNavigateUp();
    }

    // displays simple progress spinner if op takes longer than 1 second
    @Override
    public void showProgress() {
        isProgressOn = true;
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (isProgressOn) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    @Override
    public void hideProgress() {
        isProgressOn = false;
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            // bring up settings page
            Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.action_go_to_settings);
        }

        return super.onOptionsItemSelected(item);
    }

}
