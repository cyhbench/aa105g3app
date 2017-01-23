package com.chef_order_list;

import android.os.AsyncTask;
import android.util.Log;

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

class ChefOrderListInsertTask extends AsyncTask<Object, Integer, Integer> {
    private final static String TAG = "ChefOrderListTask";

    @Override
    protected Integer doInBackground(Object... params) {
//        mem_no, chef_no, chef_ord_cost, chef_act_date_timestamp, chef_ord_place, chef_ord_cnt
        String url = params[0].toString();
        String action = params[1].toString();
        String mem_no = params[2].toString();
        String chef_no = params[3].toString();
        String chef_ord_cost = params[4].toString();
        String chef_act_date = params[5].toString();
        String chef_ord_place = params[6].toString();
        String chef_ord_cnt = params[7].toString();
//        String chef_ord_con = params[8].toString();


        String result;
        JsonObject jsonObject = new JsonObject();

//        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").registerTypeAdapter(String.class, new EscapeStringSerializer()).create();
//        String json = gson.toJson(memberVO);

        jsonObject.addProperty("action", action);
        jsonObject.addProperty("mem_no", mem_no);
        jsonObject.addProperty("chef_no", chef_no);
        jsonObject.addProperty("chef_ord_cost", chef_ord_cost);
        jsonObject.addProperty("chef_act_date", chef_act_date);
        jsonObject.addProperty("chef_ord_place", chef_ord_place);
        jsonObject.addProperty("chef_ord_cnt", chef_ord_cnt);


//        jsonObject.addProperty("memberVO", new GsonBuilder().setDateFormat("yyyy-MM-dd").create().toJson(memberVO));

//        if (params[3] != null) {
//            String imageBase64 = params[3].toString();
//            jsonObject.addProperty("imageBase64", imageBase64);
//        }
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
        Log.d(TAG, "jsonOut:(私廚訂單getRemoteDate) " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
        } else {
            Log.d(TAG, "response code:(私廚訂單.getRemoteDate.else) " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn:(私廚訂單.getRemoteDate.84line)" + sb);
        return sb.toString();
    }
}
