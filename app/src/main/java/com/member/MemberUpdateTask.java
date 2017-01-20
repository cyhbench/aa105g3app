package com.member;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by cuser on 2016/12/20.
 */

class MemberUpdateTask extends AsyncTask<Object, Integer, Integer> {
    private final static String TAG = "MemberUpdateTask";

    @Override
    protected Integer doInBackground(Object... params) {
        String url = params[0].toString();
        String action = params[1].toString();
        MemberVO memberVO = (MemberVO) params[2];
        String result;
        JsonObject jsonObject = new JsonObject();

//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(String.class, new EscapeStringSerializer()).create();
//        String json = gson.toJson(memberVO);

        jsonObject.addProperty("action", action);
//        jsonObject.addProperty("memberVO", json);
    //    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        Log.d(TAG, "qqq memberVO:" + memberVO.toString());
        jsonObject.addProperty("memberVO", new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(memberVO));
//        jsonObject.addProperty("memberVO", "prod_no":"1","prod_name":"米","prod_type":"SPACE BAG","sales_volume":100,"stor_capacity":50,"unit_price":300,"prod_description":"THIS IS A RICE","prod_status":"1","disc_status":"1","sell_status":"0","shelf_date":"Jan 1, 2016 12:00:00 AM","remove_date":"Dec 31, 2016 12:00:00 AM","disc_price":200,"disc_start_date":"Feb 1, 2016 12:00:00 AM","disc_end_date":"May 31, 2016 12:00:00 AM");
  //      jsonObject.addProperty("memberVO", new Gson().toJson(memberVO));
        if (params[3] != null) {
            String imageBase64 = params[3].toString();
            jsonObject.addProperty("imageBase64", imageBase64);
        }
        try {
            result = getRemoteData(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }
//        if((result) == null) {Log.d(TAG, "result:"+result);}
//        else{Log.d(TAG, "result:"+result);}
//        Log.d(TAG, "Integer.parseInt(result):"+Integer.parseInt(result));
//        return Integer.parseInt(result);
        return 0;
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuilder sb = new StringBuilder();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut:(MemberUpdateTask.getRemoteDate) " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } else {
            Log.d(TAG, "response code:(MemberUpdateTask.getRemoteDate.else) " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn:(MemberUpdateTask.getRemoteDate.84line)" + sb);
        return sb.toString();
    }
}
