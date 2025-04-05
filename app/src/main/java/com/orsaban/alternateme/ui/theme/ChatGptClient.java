package com.orsaban.alternateme.ui.theme;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class ChatGptClient {
    private static final String API_URL = "CHANGE TO YOURS!";
    private static final String API_KEY = "CHANGE TO YOURS!";

    private final OkHttpClient client = new OkHttpClient();

    public void sendPrompt(String userInput, Callback callback) {
        try {
            // System message to guide the AI behavior
            String systemMessage = "You're a storyteller. Respond with a *short and realistic* story based *only* on the user's input. Do not invent any names, dates, places, or extra context. Do not assume anything. Only use the exact scenario the user described. Your tone should feel like a personal memory or thought. Never add titles or extra commentary.";

            // Build JSON messages
            JSONArray messages = new JSONArray()
                    .put(new JSONObject()
                            .put("role", "system")
                            .put("content", systemMessage))
                    .put(new JSONObject()
                            .put("role", "user")
                            .put("content", userInput));

            // Build the body
            JSONObject body = new JSONObject();
            body.put("model", "mistralai/mistral-7b-instruct:free");
            body.put("messages", messages);

            // Build the request
            Request request = new Request.Builder()
                    .url(API_URL)
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .addHeader("HTTP-Referer", "https://openrouter.ai") // <- safer for testing
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(body.toString(), MediaType.parse("application/json")))
                    .build();

            Log.d("ChatGPT", "Sending prompt: " + userInput);
            client.newCall(request).enqueue(callback);

        } catch (Exception e) {
            e.printStackTrace(); // You can also show this error to the user if needed
        }
    }
}
