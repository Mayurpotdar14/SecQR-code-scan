package edu.tamuc.secqrscanner;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Alt on 3/25/2017.
 */

public class InitPhishMessage extends InitWarningMessage{

    private TextView headline;
    private TextView msgBody;
    private TextView contLink;


    public InitPhishMessage(Activity _activity, String _URL) {
        super(_activity, _URL);
    }


    @Override
    public void initHeadline() {
        headline = (TextView) getActivity().findViewById(R.id.heading_field);
        headline.setText("WARNING: Suspected of phishing threats!");
        headline.setGravity(50);
    }


    @Override
    public void initMessageBody() {
        String uRLToDisplay = getScannedURL();

        msgBody = (TextView) getActivity().findViewById(R.id.message_body);
        msgBody.setText("The website " + uRLToDisplay + " has been flagged as a phishing site. " +
                "Phishing sites imitate legitimate websites in order to try to get you to reveal " +
                "sensitive information like your bank information or passwords. Click 'Get Me Out' " +
                "to avoid this potential threat, or 'Find Out More' to learn more about it.");
    }


    @Override
    public void initContAnyways() {

        contLink = (TextView) getActivity().findViewById(R.id.cont_anyways_link);
        contLink.setClickable(true);
        // Do something with cont anyways field link
    }
}
