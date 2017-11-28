package edu.tamuc.secqrscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * This is the main class of the application. It creates the main activity
 *  window when the program starts.
 */
public class MainActivity extends Activity {

    // Declares the scan button object.
    Button scanButton;
    Button userEdButton;


    /**
     * This method overrides the inherited method from the Activity class.
     *  It creates the main activity window.
     * @param savedInstanceState a Bundle object of a collection of parameters.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Passes the collection of paramters to the super Activity class.
        super.onCreate(savedInstanceState);

        // Sets the view of this activity window to the XML layout settings.
        setContentView(R.layout.activity_main);

        // Initializes the button pointer to the scan_button object in the XML layout.
        scanButton = (Button)findViewById(R.id.scan_button);
        // Sets its onClickListener to an annonymous class which opens the ScannerActivity view.
        scanButton.setOnClickListener(new View.OnClickListener(){

            /**
             * This is an overridden method from the OnClickListener class which creates an action
             *  when the button is clicked.
             * @param v the view object that is passed.
             */
            @Override
            public void onClick(View v) {

                // Creates an intent object of the ScannerActivity class to pass
                //  to startActivity() in order to create the view.
                Intent scannerIntent = new Intent(MainActivity.this, ScannerActivity.class);
                startActivity(scannerIntent);
            }
        });

        // Initializes the button pointer to the ed_main_button object in the XML layout.
        userEdButton = (Button)findViewById(R.id.ed_main_button);
        // Sets its onClickListener to an annonymous class which opens the UserEdActivity view.
        userEdButton.setOnClickListener(new View.OnClickListener(){

            /**
             * This is an overridden method from the OnClickListener class which creates an action
             *  when the button is clicked.
             * @param v the view object that is passed.
             */
            @Override
            public void onClick(View v) {

                // Creates an intent object of the UserEdActivity class to pass
                //  to startActivity() in order to create the view.
                Intent userEdIntent = new Intent(MainActivity.this, UserEdActivity.class);
                startActivity(userEdIntent);
            }
        });
    }
}
