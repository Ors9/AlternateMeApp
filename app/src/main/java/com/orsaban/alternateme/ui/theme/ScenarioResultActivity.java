package com.orsaban.alternateme.ui.theme;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.orsaban.alternateme.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

// This screen shows the response from GPT.
public class ScenarioResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_result);

        TextView resultTextView = findViewById(R.id.resultTextView);
        Button backButton = findViewById(R.id.backButton);

        // Get the user input from the previous screen.
        String userInput = getIntent().getStringExtra("USER_INPUT");
        resultTextView.setText("טוען תוצאה...");

        // When the back button is clicked, just finish this activity.
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button shareButton = findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result = ((TextView) findViewById(R.id.resultTextView)).getText().toString();

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, result);

                startActivity(Intent.createChooser(shareIntent, "שתף את הסיפור שלך עם..."));
            }
        });

        // Send the user's prompt to GPT.
        ChatGptClient gptClient = new ChatGptClient();
        gptClient.sendPrompt(userInput, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                runOnUiThread(() -> resultTextView.setText("שגיאת רשת: " + e.getMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseStr = response.body().string();
                    String gptReply = extractGptReply(responseStr);
                    runOnUiThread(() -> resultTextView.setText(gptReply));
                } else if (response.code() == 429) {
                    runOnUiThread(() -> resultTextView.setText("שלחת יותר מדי בקשות. המתן רגע ונסה שוב."));
                } else {
                    runOnUiThread(() -> resultTextView.setText("שגיאה: " + response.code()));
                }
            }
        });
    }

    // Parse the GPT JSON response.
    private String extractGptReply(String jsonResponse) {
        try {
            JSONObject json = new JSONObject(jsonResponse);
            JSONArray choices = json.getJSONArray("choices");
            JSONObject message = choices.getJSONObject(0).getJSONObject("message");
            return message.getString("content").trim();
        } catch (Exception e) {
            return "שגיאה בפירוש התשובה";
        }
    }
}
