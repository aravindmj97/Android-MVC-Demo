package com.example.mvc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements Observer, View.OnClickListener{

    Button getNewsBtn;
    TextView newsTextView, sumResultTextView;
    EditText num1EditText, num2EditText;
    ImageView plusImageView;
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getNewsBtn = findViewById(R.id.getNewsBtn);
        newsTextView = findViewById(R.id.newsTextView);
        sumResultTextView = findViewById(R.id.sumResultTextView);
        num1EditText = findViewById(R.id.num1EditText);
        num2EditText = findViewById(R.id.num2EditText);
        plusImageView = findViewById(R.id.plusImageView);

        model = new Model();
        model.addObserver(this);
        model.refreshModel();

        getNewsBtn.setOnClickListener(this);
        plusImageView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.getNewsBtn:
                try {
                     runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                fetchNews();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.plusImageView:
                model.setNumForAddition(Integer.parseInt(num1EditText.getText().toString()), Integer.parseInt(num2EditText.getText().toString()));
                break;
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        newsTextView.setText(model.getNewsContent());
        sumResultTextView.setText(model.getSum());
        num1EditText.setText(String.valueOf(model.getNum1()));
        num2EditText.setText(String.valueOf(model.getNum2()));
    }

    public void fetchNews() throws IOException{
        OkHttpClient client = new OkHttpClient();
        String url = "https://inshorts.deta.dev/news?category=science";
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    try {
                        JSONObject jObject = new JSONObject(responseBody.string());
                        JSONArray jArray = jObject.getJSONArray("data");
                        Random random = new Random();
                        int index = random.ints(0, jArray.length()-1)
                                .findFirst()
                                .getAsInt();
                        JSONObject newsObject = jArray.getJSONObject(index);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    model.setNewsContent(newsObject.getString("title"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}