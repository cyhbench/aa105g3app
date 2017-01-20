package com.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.google.gson.JsonObject;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.lang.Integer.parseInt;

/**
 * Created by cuser on 2016/12/14.
 */
//有用來抓私廚簡介的照片 (chef_image, chef_reci_image!~5): ChefInfoFragment
//沒有抓到DB的圖時，沒有設定default image，故有Exception
public class GetImageTask extends AsyncTask<Object, Integer, Bitmap> {
    private final static String TAG = "GetImageTask";
    private final static String ACTION = "getImage";
    private final WeakReference<ImageView> imageViewWeakReference;

    public GetImageTask(ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
        String url = params[0].toString();
        String no = params[1].toString();
        int imageSize = parseInt(params[2].toString());
        String var = params[3].toString();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);
        jsonObject.addProperty("no", no);
        jsonObject.addProperty("imageSize", imageSize);
        jsonObject.addProperty("var", var);

        Bitmap bitmap;
        try {
            bitmap = getRemoteImage(url, jsonObject.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
            return null;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }
        ImageView imageView = imageViewWeakReference.get();
        if (imageView != null) {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);
            } else {
//                imageView.setImageResource(R.drawable.default_image);
            }
        }
        super.onPostExecute(bitmap);
    }

    private Bitmap getRemoteImage(String url, String jsonOut) throws IOException {
        Bitmap bitmap = null;
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut:(GetImageTask.Bitmap.getRemoteImage.78line) " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();

        if (responseCode == 200) {
            bitmap = BitmapFactory.decodeStream(connection.getInputStream());
        } else {
            Log.d(TAG, "response code:(RecipeGetImageTask.Bitmap.getRemoteImage.else.86line)" + responseCode);
        }
        connection.disconnect();
        return bitmap;
    }
}
