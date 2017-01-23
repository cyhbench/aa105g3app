package com.member;

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

/**
 * Created by cuser on 2016/12/14.
 */

public class MemberGetOneByACTask extends AsyncTask<Object, Integer, MemberVO> {
    private final static String TAG = "logMemGetOneBYACTask";
//    private final static String ACTION = "login";

    @Override
    protected MemberVO doInBackground(Object... params) {
        String jsonIn;
        String url = params[0].toString();
        String action = params[1].toString();
        String mem_ac = params[2].toString();
        String mem_pw = params[3].toString();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", action);
        jsonObject.addProperty("mem_ac", mem_ac);
        jsonObject.addProperty("mem_pw", mem_pw);

        try {
            jsonIn = getRemoteOneData(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }

        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Type listType = new TypeToken<MemberVO>() {
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
        Log.d(TAG, "jsonOut(MemberGetOneTask.getRemoteData.62line): " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG, "response code(MemberGetOneTask.getRemoteData.74line): " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn((MemberGetOneTask.getRemoteDate.77line)): " + jsonIn);
        return jsonIn.toString();
    }
}
