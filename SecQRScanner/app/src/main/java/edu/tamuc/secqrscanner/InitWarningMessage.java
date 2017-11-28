package edu.tamuc.secqrscanner;

import android.app.Activity;
import android.view.View;

/**
 * Created by Alt on 3/25/2017.
 */

public abstract class InitWarningMessage {

    private static Activity myActivity;
    private static String scannedURL;


    public InitWarningMessage(Activity _activity, String _scannedURL) {

        myActivity = _activity;
        scannedURL = _scannedURL;
    }


    public void initMessage() {
        initHeadline();
        initMessageBody();
        initContAnyways();
    }


    public static Activity getActivity() {
        return myActivity;
    }

    public static String getScannedURL() {
        return scannedURL;
    }


    public abstract void initHeadline();
    public abstract void initMessageBody();
    public abstract void initContAnyways();
}
