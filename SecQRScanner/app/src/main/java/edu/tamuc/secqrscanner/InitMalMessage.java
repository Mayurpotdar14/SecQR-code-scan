package edu.tamuc.secqrscanner;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Alt on 4/7/2017.
 */

public class InitMalMessage extends InitWarningMessage {
    private TextView headline;
    private TextView msgBody;
    private TextView contLink;


    public InitMalMessage(Activity _activity, String _URL) {
        super(_activity, _URL);
    }


    @Override
    public void initHeadline() {
        headline = (TextView) getActivity().findViewById(R.id.heading_field);
        headline.setText("WARNING: Suspected of malware threats!");
        headline.setGravity(50);
    }


    @Override
    public void initMessageBody() {
        String uRLToDisplay = getScannedURL();

        msgBody = (TextView) getActivity().findViewById(R.id.message_body);
        msgBody.setText("The website " + uRLToDisplay + " likely contains malware. Google Safe " +
                "Browsing has indicated that this site hosts malware that can harm your device. " +
                "This site may automatically install harmful software that can damage your device " +
                "or steal sensitive information. Click 'Get Me Out' to avoid this threat.");
    }


    @Override
    public void initContAnyways() {

        contLink = (TextView) getActivity().findViewById(R.id.cont_anyways_link);
        contLink.setClickable(true);
        // Do something with cont anyways field link
    }
}
