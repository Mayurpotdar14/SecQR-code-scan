package edu.tamuc.secqrscanner;

import android.app.Activity;
import android.widget.TextView;

/**
 * Created by Tim on 4/7/2017.
 */

public class InitBothMessage extends InitWarningMessage{

    private TextView headline;
    private TextView msgBody;
    private TextView contLink;


    public InitBothMessage(Activity _activity, String _URL) {
        super(_activity, _URL);
    }


    @Override
    public void initHeadline() {
        headline = (TextView) getActivity().findViewById(R.id.heading_field);
        headline.setText("WARNING: Suspected of phishing and malware threats!");
        headline.setGravity(50);
    }


    @Override
    public void initMessageBody() {
        String uRLToDisplay = getScannedURL();

        msgBody = (TextView) getActivity().findViewById(R.id.message_body);
        msgBody.setText("The website " + uRLToDisplay + " has been found to have indications of " +
                "phishing and malware threats. Phishing sites try to trick you into thinking that their " +
                "site is legitimate in order to try to get you to reveal sensitive information " +
                "like your bank information or passwords. This site can potentialy harm you " +
                "by automatically installing harmful software which can damage your device or " +
                "steal sensitive information. Click 'Get Me Out' to avoid this potential threat.");
    }


    @Override
    public void initContAnyways() {

        contLink = (TextView) getActivity().findViewById(R.id.cont_anyways_link);
        contLink.setClickable(true);
        // Do something with cont anyways field link
    }
}
