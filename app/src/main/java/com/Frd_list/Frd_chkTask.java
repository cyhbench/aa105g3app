package com.Frd_list;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
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
 * Created by cyh on 2017/1/2.
 */

public class Frd_chkTask extends AsyncTask<Object, Integer, Frd_listVO> {
    String TAG = "Frd_listTask";
    //    private final static String ACTION = "getAllByMem_no_For_Display";
    private Frd_listVO frd_listVO;

    @Override
    protected Frd_listVO doInBackground(Object... params) {
        String url = params[0].toString();
        String ACTION = params[1].toString();
        frd_listVO = (Frd_listVO) params[2];
        Log.d(TAG, "ACTION~~" + ACTION);
        String jsonIn;
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);
        jsonObject.addProperty("frd_listVO", new Gson().toJson(frd_listVO));

        try {
            jsonIn = getRemoteData(url, jsonObject.toString());
            Log.d(TAG, "jsonIn(MemberUpdateActivity.getRemoteData.180 line): " + jsonIn);
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }
//            Gson gson = new Gson();
//            JsonObject jObject = gson.fromJson(jsonIn, JsonObject.class);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        frd_listVO = gson.fromJson(jsonIn, Frd_listVO.class);
        return frd_listVO;
    }

    private String getRemoteData(String url, String jsonOut) throws IOException {
        StringBuffer jsonIn = new StringBuffer();
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut(RcipGetOneByRcipNOTask.getRemoteData.62line): " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG, "response code(RcipGetOneByRcipNOTask.getRemoteData.74line): " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn((RcipGetOneByRcipNOTask.getRemoteDate.77line)): " + jsonIn);
        return jsonIn.toString();
    }
}
