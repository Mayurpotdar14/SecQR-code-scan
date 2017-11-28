package edu.tamuc.secqrscanner;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import me.dm7.barcodescanner.zxing.ZXingScannerView;


/**
 * This class creates and handles the camera scanner view of the app. It creates the window
 * to scan the QR code and then processes the results and displays the appropriate dialog
 * box to the user with whether the URL was malicious or not.
 */
public class ScannerActivity extends Activity implements ZXingScannerView.ResultHandler {

    // Private class field for the scanner view object.
    private ZXingScannerView zXingScannerView;


    /**
     * Creates the main window of the scanning window.
     *
     * @param state bundle collection of state parameters.
     */
    @Override
    public void onCreate(Bundle state) {

        // Passes parameters to the super Activity class.
        super.onCreate(state);

        // Initializes the scanner view.
        zXingScannerView = new ZXingScannerView(this);

        // Sets the scanner to scan QR Codes only.
        List<BarcodeFormat> formats = new ArrayList<>();
        formats.add(BarcodeFormat.QR_CODE);
        zXingScannerView.setFormats(formats);

        // Sets the content view to the scanner view.
        setContentView(zXingScannerView);

        // Starts the camera when this activity.
        zXingScannerView.startCamera();
    }


    /**
     * This method overrides the onResume() method from the Activity class. The actions taken when
     * the activity is resumed.
     */
    @Override
    public void onResume() {
        // Calls the super classes on resume method.
        super.onResume();

        // Sets this class as the result handler for the scanner.
        zXingScannerView.setResultHandler(this);

        // Starts the camera when this activity is resumed.
        //zXingScannerView.startCamera();
    }


    /**
     * This method overrides and  sets the actions to perform when the activity is paused.
     */
    @Override
    public void onPause() {

        // Invokes the super class Activity's onPause method.
        super.onPause();

        // Stops the camera when the activity is paused.
        zXingScannerView.stopCamera();
    }


    /**
     * This overridden method creates the actions to process the results of the scan.
     *
     * @param rawResult the result object containing the results of the scan.
     */
    @Override
    public void handleResult(Result rawResult) {

        // Stops the camera preview when it receives results.
        zXingScannerView.stopCameraPreview();

        setIsMalware(false);
        setIsPhishing(false);

        String scannedResult = rawResult.getText();

        // For Testing; Replace with scanned result.
        //result = "http://malware.testing.google.test/testing/malware/*"; // Malware URL
        //result = "http://paypal-info.perosnverify.com/webapps/a1da5/websrc"; // Phishing URL

        final String FINAL_URL = scannedResult;


        // Creates a Thread object to run the server communication.
        Thread networkThread = new Thread(new Runnable() {

            /**
             * This method overrides the Runnable method run() to run the server
             * connection and processing in it's own thread.
             */
            @Override
            public void run() {

                final String GSB_API_KEY = "AIzaSyAE3DFw6FFpTezYXY7NdSZEIWt2ezCAVfw";
                final String GSB_SERVER_URL = "https://safebrowsing.googleapis.com/v4/threatMatches:find?key=" + GSB_API_KEY;

                final String GSB_POST_BODY = "{\"client\":{"
                        + "\"clientId\":\"secqrscan\","
                        + "\"clientVersion\":\"1.3\"},"
                        + "\"threatInfo\":{"
                        + "\"threatTypes\":[\"MALWARE\", \"SOCIAL_ENGINEERING\"],"
                        + "\"platformTypes\":[\"ANY_PLATFORM\"],"
                        + "\"threatEntryTypes\":[\"URL\", \"THREAT_ENTRY_TYPE_UNSPECIFIED\", \"EXECUTABLE\"],"
                        + "\"threatEntries\":[{\"url\":\"" + FINAL_URL + "\"}]"
                        + "}}";


                try {
                    URL url = new URL(GSB_SERVER_URL);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setConnectTimeout(5000);
                    con.setReadTimeout(10000);
                    con.setDoOutput(true);

                    try (DataOutputStream outStream = new DataOutputStream(con.getOutputStream())) {
                        outStream.writeBytes(GSB_POST_BODY);
                        outStream.flush();
                    }

                    int responseCode = con.getResponseCode();
                    String responseMessage = con.getResponseMessage();

                    String responseString = "";
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))) {
                        for (int i = 0; i < 4; i++) {
                            responseString = in.readLine();
                        }
                    }

                    if (responseString.contains("SOCIAL_ENGINEERING")) {
                        System.out.println("Phishing");
                        setIsPhishing(true);

                    } else if (responseString.contains("MALWARE")) {
                        System.out.println("Malware");
                        setIsMalware(true);
                    }

                    //System.out.println(responseCode + ", " + responseMessage);

                } catch (Exception e) {
                    System.err.println(e);
                    e.printStackTrace();
                }


                final String PT_URL = "http://checkurl.phishtank.com/checkurl/index.php?url=";
                final String PT_APP_KEY = "'7b0ab5efea04aadd87405aebffbb52bf7a15ce48c2a5556a1266fdd117dc5845'";
                final String PT_POST_BODY = "format='json'&app_key=" + PT_APP_KEY;

                try {
                    URL url2 = new URL(PT_URL + FINAL_URL);
                    HttpURLConnection con2 = (HttpURLConnection) url2.openConnection();

                    con2.setRequestMethod("POST");
                    con2.setConnectTimeout(5000);
                    con2.setReadTimeout(10000);
                    con2.setDoOutput(true);

                    try (DataOutputStream outStream = new DataOutputStream(con2.getOutputStream())) {
                        outStream.writeBytes(PT_POST_BODY);
                        outStream.flush();
                    }

                    String responseString = "";
                    try (BufferedReader in = new BufferedReader(new InputStreamReader(con2.getInputStream()))) {
                        for (int i = 0; i < 11; i++) {
                            responseString = in.readLine();
                        }
                    }

                    if (responseString.contains("true")) {
                        System.out.println("Phishing");
                        setIsPhishing(true);
                    }

                    // Debugging code:
                    //System.out.println(con2.getHeaderField(3));
                    //System.out.println(con2.getHeaderField(4));
                    //System.out.println(con2.getHeaderField(5));
                    //int rc = con2.getResponseCode();
                    //System.out.println(rc + ", " + con2.getResponseMessage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                //setIsWaiting(false);
            }
        });

        // Sets the waiting flag as true until the thread finishes.
        //isWaiting = true;
        //isWaiting = false;

        // Starts the thread to connect to the server and retrieve the result.
        networkThread.start();

        try {
            networkThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Empty loop used to synchronize this activity with the networking thread.
//        while (isWaiting) {
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } // Waits on the networkThread to finish getting the result.


        // If the URL is flagged as phishing or malicious.
        if (isPhishing || isMalware) {

            Intent openWarningMessage = new Intent(ScannerActivity.this, MessageActivity.class);
            openWarningMessage.putExtra("scannedURL", FINAL_URL);
            openWarningMessage.putExtra("isPhishing", isPhishing);
            openWarningMessage.putExtra("isMalware", isMalware);
            startActivity(openWarningMessage);
        }
        // If the URL is flagged as safe.
        else {

            // Creates the dialog box for safe URLs to display to the user.
            final Dialog dialogGood = new Dialog(ScannerActivity.this);

            // Sets the title message.
            dialogGood.setTitle("Looks Safe!");

            // Sets the content view to the custom created XML file dialog box layout.
            dialogGood.setContentView(R.layout.layout_custom_dialog_good);
            // Sets the background border color of the window to dark gray.
            dialogGood.getWindow().setBackgroundDrawableResource(R.color.colorPrimaryDark);

            // Creates a pointer to the text field in the dialog box and sets its text to the URL.
            TextView textURL = (TextView) dialogGood.findViewById(R.id.textURLDisplayGood);
            textURL.setText(FINAL_URL);

            // Creates pointers to the buttons in the dialog box.
            Button buttonOkay = (Button) dialogGood.findViewById(R.id.buttonOpenURLGood);
            Button buttonCancel = (Button) dialogGood.findViewById(R.id.buttonCancelGood);

            // Sets the onClickListener for the Open URL button.
            buttonOkay.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Creates and starts the intent object to open the URL in the user's browser.
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(FINAL_URL));
                    startActivity(browserIntent);
                }
            });

            // Sets the onClickListener for the Cancel button.
            buttonCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Stops the camera.
                    zXingScannerView.stopCamera();

                    // Closes the dialog box.
                    dialogGood.dismiss();

                    // Closes the ScannerActivity activity.
                    finish();
                }
            });

            // Shows the dialog box for URLs that are flagged as safe.
            dialogGood.show();
        }

        // Resumes scanner preview.
        zXingScannerView.resumeCameraPreview(this);
    }


    /**
     * This method is a static method used by the inner class networkingThread to synchronize the
     * code, so that the resulting dialog boxes are shown after the thread receives the response
     * from the server.
     *
     * @param _isPhishing a boolean value whether to set the isPhishing flag to true or false.
     */
    static void setIsPhishing(boolean _isPhishing) {

        // Saves the boolean value to its corresponding class field.
        isPhishing = _isPhishing;
    }


    static void setIsMalware(boolean _isMalware) {

        // Saves the boolean value to its corresponding class field.
        isMalware = _isMalware;
    }


    static void setIsWaiting(boolean _isWaiting) {

        // Saves the boolean value to its corresponding class field.
        isWaiting = _isWaiting;
    }


    // Class fields.
    static boolean isPhishing; // Flag for whether a URL is malicious or not.
    static boolean isMalware;
    static boolean isWaiting; // Flag for whether the network thread has received the result.
}