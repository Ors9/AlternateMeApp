package com.orsaban.alternateme.ui.theme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.orsaban.alternateme.R;

// This activity is where the user types a "what if" scenario.
public class CreateScenarioActivity extends AppCompatActivity {

    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Theme_AlternateMe);

        // Set the layout XML file for this screen.
        setContentView(R.layout.activity_create_scenario);

        // אתחול AdMob עם callback
        MobileAds.initialize(this, initializationStatus -> {
            // רק אחרי שהאתחול הצליח, טען את הבאנר
            AdView adView = findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
        });

        // Find the button and input field from the layout.
        Button button = findViewById(R.id.button);
        editText = findViewById(R.id.editTextText);

        // Set what happens when the user clicks the button.
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get and trim the text the user typed.
                String userInput = editText.getText().toString().trim();

                // Do not allow empty input or input that is only whitespace.
                if (userInput.isEmpty()) {
                    editText.setError("יש להקליד טקסט כלשהו");
                    return;
                }

                // Create an Intent to open the next screen (ScenarioResultActivity).
                Intent intent = new Intent(CreateScenarioActivity.this, ScenarioResultActivity.class);
                // Put the user input as extra data so the next screen can use it.
                intent.putExtra("USER_INPUT", userInput);
                // Start the next activity.
                startActivity(intent);
            }
        });
    }

    // Clear the EditText when this activity resumes.
    @Override
    protected void onResume() {
        super.onResume();
        if (editText != null) {
            editText.setText("");
        }
    }
}
