package com.recipetype;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by cuser on 2016/12/14.
 */

public class RecipeLTypeGetAllTask extends AsyncTask<Object, Integer, List<Recipe_l_typeVO>>{
    private final static String TAG = "RecipeLTypeGetAllTask";
    private final static String ACTION = "getAll";

    @Override
    protected List<Recipe_l_typeVO> doInBackground(Object... params) {
        String jsonIn;
        String url = params[0].toString();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);

        try {
            jsonIn = getRemoteOneData(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        Type listType = new TypeToken<List<Recipe_l_typeVO>>() {
        }.getType();
        return gson.fromJson(jsonIn, listType);
    }

    private String getRemoteOneData(String url, String jsonOut) throws IOException {
        StringBuffer jsonIn = new StringBuffer();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut(RecipeLTypeGetAllTask.getRemoteData.62line): " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG, "response code(RecipeLTypeGetAllTask.getRemoteData.74line): " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn((RecipeLTypeGetAllTask.getRemoteDate.77line)): " + jsonIn);
        return jsonIn.toString();
    }
}
