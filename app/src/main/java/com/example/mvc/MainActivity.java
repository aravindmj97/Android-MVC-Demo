package com.example.mvc;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    Button b1, b2, b3, b4;
    TextView t;
    Model model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        model = new Model();
        model.addObserver(this);

        b1 = findViewById(R.id.button);
        b2 = findViewById(R.id.button2);
        b3 = findViewById(R.id.button3);
        b4 = findViewById(R.id.button4);
        t = findViewById(R.id.textView);

        b1.setOnClickListener(this);
        b2.setOnClickListener(this);
        b3.setOnClickListener(this);
        b4.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){

            case R.id.button:
                model.setList(0);
                break;

            case R.id.button2:
                model.setList(1);
                break;

            case R.id.button3:
                model.setList(2);
                break;

            case R.id.button4:
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
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        b1.setText("Count: "+model.getInt(0));
        b2.setText("Count: "+model.getInt(1));
        b3.setText("Count: "+model.getInt(2));
        t.setText(model.getNewsContent());
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