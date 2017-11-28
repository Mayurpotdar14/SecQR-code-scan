package edu.tamuc.secqrscanner;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MessageActivity extends Activity {

    ImageButton learnMoreButton;
    Button getOutButton;
    TextView contAnywaysLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warning_message);


        Intent intent = getIntent();
        String scannedURL = intent.getExtras().getString("scannedURL");
        boolean isPhishing = intent.getExtras().getBoolean("isPhishing");
        boolean isMalware = intent.getExtras().getBoolean("isMalware");


        if (isPhishing && isMalware) {

            InitWarningMessage thisMessage = new InitBothMessage(this, scannedURL);
            thisMessage.initMessage();

        } else if (isPhishing && !isMalware) {

            InitWarningMessage thisMessage = new InitPhishMessage(this, scannedURL);
            thisMessage.initMessage();
        } else if (isMalware && !isPhishing) {

            InitWarningMessage thisMessage = new InitMalMessage(this, scannedURL);
            thisMessage.initMessage();
        }


        View.OnClickListener learnMoreListener = createLearnMoreListener();
        View.OnClickListener getOutListener = createGetMeOutListener();
        View.OnClickListener contListener = createContinueAnywaysListener(scannedURL);

        learnMoreButton = (ImageButton) this.findViewById(R.id.learn_more_button);
        learnMoreButton.setOnClickListener(learnMoreListener);

        getOutButton = (Button) this.findViewById(R.id.get_me_out_button);
        getOutButton.setOnClickListener(getOutListener);

        contAnywaysLink = (TextView) this.findViewById(R.id.cont_anyways_link);
        contAnywaysLink.setOnClickListener(contListener);
    }


    private View.OnClickListener createLearnMoreListener() {
        View.OnClickListener learnMoreListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToUserEd = new Intent(MessageActivity.this, UserEdActivity.class);
                startActivity(goToUserEd); // Goes to the education page.
                //finish();
            }
        };

        return learnMoreListener;
    }


    private View.OnClickListener createGetMeOutListener() {
        View.OnClickListener getMeOutListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //finish();
                Intent returnHome = new Intent(MessageActivity.this, MainActivity.class);
                startActivity(returnHome);
            }
        };

        return getMeOutListener;
    }


    private View.OnClickListener createContinueAnywaysListener(final String _url) {
        View.OnClickListener contAnywaysListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Creates and starts the intent object to open the URL in the user's browser.
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(_url));
                startActivity(browserIntent); // Redirects to the URL in the browser.
                //finish();
            }
        };

        return contAnywaysListener;
    }
}
