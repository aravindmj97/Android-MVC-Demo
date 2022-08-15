package com.example.mvc;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Model extends Observable {

    private Integer num1, num2;
    private String newsContent;

    public Model() {
        this.newsContent = "News will appear here";
        this.num1 = 0;
        this.num2 = 0;
    }

    public void setNumForAddition(Integer num1, Integer num2) {
        this.num1 = num1;
        this.num2 = num2;
        setChanged();
        notifyObservers();
    }

    public Integer getNum1() {
        return num1;
    }

    public Integer getNum2() {
        return num2;
    }

    public String getSum() {
        Integer sum = num1 + num2;
        return "Sum: " + sum;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String content){
        this.newsContent = content;
        setChanged();
        notifyObservers();
    }

    public void refreshModel(){
        setChanged();
        notifyObservers();
    }

}
