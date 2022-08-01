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

    private List<Integer> list;
    private String newsContent;

    public Model() {
        list = new ArrayList<Integer>(3);
        newsContent = "";

        list.add(0);
        list.add(0);
        list.add(0);
    }

    public int getInt(int index) throws IndexOutOfBoundsException{
        return list.get(index);
    }

    public void setList(int index) throws IndexOutOfBoundsException{
        list.set(index, list.get(index)+1);
        setChanged();
        notifyObservers();
    }

    public String getNewsContent() {
        return newsContent;
    }

    public void setNewsContent(String content){
        this.newsContent = content;
        setChanged();
        notifyObservers();
    }


}
