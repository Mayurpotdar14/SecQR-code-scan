package edu.tamuc.secqrscanner;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserEdActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_ed);

        View.OnClickListener goHomeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //finish();
                Intent returnHome = new Intent(UserEdActivity.this, MainActivity.class);
                startActivity(returnHome);
            }
        };

        Button goHomeButton = (Button) this.findViewById(R.id.go_home_button);
        goHomeButton.setOnClickListener(goHomeListener);
    }
}
