/*
 * Copyright (c) 2023, Oracle and/or its affiliates.
 * Licensed under the Universal Permissive License v 1.0 as shown at https://oss.oracle.com/licenses/upl.
 */
package com.oracle.content.samples.gallery;

import android.util.Log;

/**
 * Very simple wrapper for internal logging.
 */
public class Logging {
    // whether to log more detail (can be set via UI settings)
    static boolean isEnabled = true;

    public static void log(String s) {
        if (isEnabled) {
            Log.d("OCEGallery", s);
        }
    }
}
