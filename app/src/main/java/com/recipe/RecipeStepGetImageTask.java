package com.recipe;

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

public class RecipeStepGetImageTask extends AsyncTask<Object, Integer, Bitmap> {
    private final static String TAG = "RecipeStepGetImageTask";
    private final static String ACTION = "showRecipe_cont_pic";
    private final WeakReference<ImageView> imageViewWeakReference;

    RecipeStepGetImageTask(ImageView imageView) {
        this.imageViewWeakReference = new WeakReference<>(imageView);
    }

    @Override
    protected Bitmap doInBackground(Object... params) {
//        url, recipe_no, step, imageSize
        String url = params[0].toString();
        String recipe_no = params[1].toString();
        int step = parseInt(params[2].toString());
        int imageSize = parseInt(params[3].toString());
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("action", ACTION);
        jsonObject.addProperty("recipe_no", recipe_no);
        jsonObject.addProperty("step", step);
        jsonObject.addProperty("imageSize", imageSize);

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
                imageView.setImageResource(R.drawable.default_image);
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
        Log.d(TAG, "jsonOut:(RecipeGetImageTask.Bitmap.getRemoteImage.78line) " + jsonOut);
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
