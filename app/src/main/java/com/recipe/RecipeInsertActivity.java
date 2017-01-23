package com.recipe;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.ChefPageMainActivity;
import com.MemberPageMainActivity;
import com.androidbelieve.drawerwithswipetabs.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.main.Common;
import com.member.MemberVO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static com.main.Common.showToast;


/**
 * Created by cyh on 2016/12/28.
 */

public class RecipeInsertActivity extends AppCompatActivity {
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private ProgressDialog progressDialog;
    private AsyncTask dataUploadTask;
    private File out;
    private static final String TAG = "新增食譜";
    private final static String ACTION = "insert";
    private Bitmap picture;
    //    private ImageView ivTakePicture;
    private TextView tvMessage;
    private MemberVO memberVO;
    private ImageView iv_recipe_pic;
    private EditText et_recipe_name;
    private EditText et_recipe_intro;
    private EditText et_recipe_mater;
    private byte[] image;
    private Button bt_recipe_pic_take_pic;
    private Button bt_recipe_pic_pick_pic;
    private String recipe_name;

    private String mem_no;
    private String recipe_intro;
    private String recipe_mater;
    private RecipeVO recipeVO = new RecipeVO();

    private ScrollView add_mater_container;
    static int totalEditTexts = 0;
    private int foodCount = 1;
    private String food_mater;
    LinearLayout containerLayout;
    private EditText etf1;
    private EditText etq1;
    private EditText etf2;
    private EditText etq2;
    private EditText etf3;
    private EditText etq3;
    private EditText etf4;
    private EditText etq4;
    private EditText etf5;
    private EditText etq5;
    private EditText etf6;
    private EditText etq6;
    private EditText etf7;
    private EditText etq7;

//    private  String url = Common.URL + "MemberServletAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_insert_activity);

        iv_recipe_pic = (ImageView) findViewById(R.id.iv_recipe_pic);
        et_recipe_name = (EditText) findViewById(R.id.et_recipe_name);
        et_recipe_intro = (EditText) findViewById(R.id.et_recipe_intro);
//        et_recipe_mater = (EditText) findViewById(R.id.et_recipe_mater1);
        add_mater_container = (ScrollView) findViewById(R.id.add_mater_container);
//        containerLayout = (LinearLayout) findViewById(R.id.mlayout);

        etf1 = (EditText) findViewById(R.id.et_recipe_mater1);
        etq1 = (EditText) findViewById(R.id.et_recipe_mater_quantity1);
        etf2 = (EditText) findViewById(R.id.et_recipe_mater2);
        etq2 = (EditText) findViewById(R.id.et_recipe_mater_quantity2);
        etf3 = (EditText) findViewById(R.id.et_recipe_mater3);
        etq3 = (EditText) findViewById(R.id.et_recipe_mater_quantity3);
        etf4 = (EditText) findViewById(R.id.et_recipe_mater4);
        etq4 = (EditText) findViewById(R.id.et_recipe_mater_quantity4);
        etf5 = (EditText) findViewById(R.id.et_recipe_mater5);
        etq5 = (EditText) findViewById(R.id.et_recipe_mater_quantity5);
        etf6 = (EditText) findViewById(R.id.et_recipe_mater6);
        etq6 = (EditText) findViewById(R.id.et_recipe_mater_quantity6);
        etf7 = (EditText) findViewById(R.id.et_recipe_mater7);
        etq7 = (EditText) findViewById(R.id.et_recipe_mater_quantity7);
//        tv_recipe_name = (TextView) findViewById(tv_recipe_name);

        mem_no = getIntent().getExtras().getString("mem_no");
        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        mem_no = getIntent().getExtras().getString("mem_no");
        String qq;
    }

    @Override
    protected void onPause() {
        if (dataUploadTask != null) {
            dataUploadTask.cancel(true);
        }
        super.onPause();
    }

    // 拍照
    public void onRecipeInsertTakePicClick(View view) {
//        takePicture();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 指定存檔路徑
        out = Environment.getExternalStorageDirectory();
        out = new File(out, "photo.jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
        if (isIntentAvailable(this, intent)) {
            startActivityForResult(intent, REQ_TAKE_PICTURE);
        } else {
            showToast(this, R.string.msg_NoCameraApp);
        }
    }

    //選照片
    public void onRecipePickPicClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    //update cancel
    public void onRecipeInsertCancelClick(View view) {
        finish();
    }

    // update
    public void onRecipeInsertClick(View view) {
        if (networkConnected()) {
            String url = Common.URL + "RecipeServletAndroid";
            Log.d(TAG, "url:" + url);
//            dataUploadTask = new DataUploadTask().execute(url, memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_image());
            try {
                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (et_recipe_name.getText().toString().trim() != null) {
                    recipe_name = et_recipe_name.getText().toString().trim();
//                    recipeVO.setRecipe_name((String)et_recipe_name.getText().toString().trim());
                }
                if (et_recipe_intro.getText().toString().trim() != null) {
                    recipe_intro = et_recipe_intro.getText().toString().trim();
                }
//                if (et_recipe_mater.getText().toString().trim() != null) {
//                    recipe_mater = et_recipe_mater.getText().toString().trim();
//                }
                if (!etf1.getText().toString().trim().equals("")) {
                    recipe_mater = etf1.getText().toString().trim();
                }

                if (!etq1.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "-" + etq1.getText().toString().trim();
                }

                if (!etf2.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "+" + etf2.getText().toString().trim();
                }

                if (!etq2.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "-" + etq2.getText().toString().trim();
                }

                if (!etf3.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "+" + etf3.getText().toString().trim();
                }

                if (!etq3.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "-" + etq3.getText().toString().trim();
                }

                if (!etf4.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "+" + etf4.getText().toString().trim();
                }

                if (!etq4.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "-" + etq4.getText().toString().trim();
                }

                if (!etf5.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "+" + etf5.getText().toString().trim();
                }

                if (!etq5.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "-" + etq5.getText().toString().trim();
                }

                if (!etf6.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "+" + etf6.getText().toString().trim();
                }

                if (!etq6.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "-" + etq6.getText().toString().trim();
                }

                if (!etf7.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "+" + etf7.getText().toString().trim();
                }

                if (!etq7.getText().toString().trim().equals("")) {
                    recipe_mater = recipe_mater + "-" + etq7.getText().toString().trim();
                }
                if (image != null) {
                    recipeVO.setRecipe_pic(image);
                }
//                else {
//                    recipeVO.setRecipe_pic();
//                }
                recipeVO.setMem_no(mem_no);
                recipeVO.setRecipe_name(recipe_name);
                recipeVO.setRecipe_intro(recipe_intro);
                recipeVO.setFood_mater(recipe_mater);

                dataUploadTask = new DataUploadTask().execute(url, recipeVO);

                if (dataUploadTask != null) {
                    Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "新增失敗", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }


        finish();

    }

    public void onRecipeInsert_AddRecipeMaterClick(View view) {
        foodCount++;

        switch (foodCount) {

            case 2:
                etf2.setVisibility(View.VISIBLE);
                etq2.setVisibility(View.VISIBLE);
                break;

            case 3:
                etf3.setVisibility(View.VISIBLE);
                etq3.setVisibility(View.VISIBLE);
                break;

            case 4:
                etf4.setVisibility(View.VISIBLE);
                etq4.setVisibility(View.VISIBLE);
                break;

            case 5:
                etf5.setVisibility(View.VISIBLE);
                etq5.setVisibility(View.VISIBLE);
                break;

            case 6:
                etf6.setVisibility(View.VISIBLE);
                etq6.setVisibility(View.VISIBLE);
                break;

            case 7:
                etf7.setVisibility(View.VISIBLE);
                etq7.setVisibility(View.VISIBLE);
                break;
        }
    }

//    public void onRecipeInsert_AddRecipeMaterClick(View view){
//        totalEditTexts++;
//        if (totalEditTexts > 100)
//            return;
//        EditText editText = new EditText(this);
//        editText.setWidth(450);
//        EditText editText1 = new EditText(this);
//        editText.setHint("食材");
//        editText.setWidth(450);
//        editText1.setHint("份量");
////        tET = totalEditTexts; editText.setId(tET);
////        layoutParams.addRule(RelativeLayout.BELOW, tET - 2); now it works!
////        containerLayout.setOrientation(LinearLayout.HORIZONTAL);
//        containerLayout.addView(editText);
//        containerLayout.addView(editText1);
//        editText.setGravity(Gravity.LEFT);
//        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) editText.getLayoutParams();
//        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
//        editText.setLayoutParams(layoutParams);
//        //if you want to identify the created editTexts, set a tag, like below
//        editText.setTag("food" + totalEditTexts);
//        editText1.setTag("quan" + totalEditTexts);
//
//    }


    class DataUploadTask extends AsyncTask<Object, Integer, RecipeVO> {

        @Override
        // invoked on the UI thread immediately after the task is executed.
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(RecipeInsertActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        // invoked on the background thread immediately after onPreExecute()
        protected RecipeVO doInBackground(Object... params) {
            String url = params[0].toString();
            recipeVO = (RecipeVO) params[1];
//            String mem_no = params[1].toString();
//            String mem_name = params[2].toString();
//            String password = params[2].toString();
//            byte[] image = (byte[]) params[2];
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", ACTION);
            jsonObject.addProperty("recipeVO", new Gson().toJson(recipeVO));
//            jsonObject.addProperty("mem_no", mem_no);
//            jsonObject.addProperty("mem_name", mem_name);
//            jsonObject.addProperty("password", password);
//            jsonObject.addProperty("mem_imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
            try {
                jsonIn = getRemoteData(url, jsonObject.toString());
                Log.d(TAG, "jsonIn(新增食譜): " + jsonIn);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
                return null;
            }
//            Gson gson = new Gson();
//            JsonObject jObject = gson.fromJson(jsonIn, JsonObject.class);
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
            recipeVO = gson.fromJson(jsonIn, RecipeVO.class);

//            MemberVO memberVO = new MemberVO(
//                    jObject.get("mem_no").getAsString(),
//                    jObject.get("mem_name").getAsString(),
//                    jObject.get("password").getAsString(),
//                    Base64.decode(jObject.get("mem_imageBase64").getAsString(), Base64.DEFAULT)
//            );
            return recipeVO;
        }

        @Override
        /*
         * invoked on the UI thread after the background computation finishes.
		 * The result of the background computation is passed to this step as a
		 * parameter.
		 */
        protected void onPostExecute(RecipeVO recipeVO) {

//            String mem_no = memberVO.getMem_no();
//            String mem_name = memberVO.getMem_name();
//            Bitmap bitmap = BitmapFactory.decodeByteArray(recipeVO.getRecipe_pic(), 0, recipeVO.getRecipe_pic().length); //////////////////
            ImageView iv_recipe_pic = (ImageView) findViewById(R.id.iv_recipe_pic);
//            String text = "name: " + name + "\npassword: " + password;
//            String text = mem_name;
//            et_member_detail_mem_name.setText(text);
//            iv_recipe_pic.setImageBitmap(bitmap);
            iv_recipe_pic.setImageBitmap(picture);
            progressDialog.cancel();

////            String mem_no = recipeVO.getMem_no();
////            String password = memberVO.getPassword();
//            Bitmap bitmap;
//            if (recipeVO.getRecipe_pic() !=null) {
//                bitmap = BitmapFactory.decodeByteArray(recipeVO.getRecipe_pic(), 0, recipeVO.getRecipe_pic().length);
//                ImageView iv_recipe_pic = (ImageView) findViewById(R.id.iv_recipe_pic);
//                iv_recipe_pic.setImageBitmap(bitmap);
//            } else {
//                iv_recipe_pic.setImageDrawable(getResources().getDrawable(R.drawable.default_image));
//            }
//
////            String text = "name: " + name + "\npassword: " + password;
////            String text = mem_name;
////            et_member_detail_mem_name.setText(text);
//
//            progressDialog.cancel();
        }
    }


    private String getRemoteData(String url, String jsonOut) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setDoInput(true); // allow inputs
        connection.setDoOutput(true); // allow outputs
        connection.setUseCaches(false); // do not use a cached copy
        connection.setRequestMethod("POST");
        connection.setRequestProperty("charset", "UTF-8");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
        bw.write(jsonOut);
        Log.d(TAG, "jsonOut: " + jsonOut);
        bw.close();

        int responseCode = connection.getResponseCode();
        StringBuffer jsonIn = new StringBuffer();
        if (responseCode == 200) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = br.readLine()) != null) {
                jsonIn.append(line);
            }
        } else {
            Log.d(TAG, "response code: " + responseCode);
        }
        connection.disconnect();
        Log.d(TAG, "jsonIn: " + jsonIn);
        return jsonIn.toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 手機拍照App拍照完成後可以取得照片圖檔
                case REQ_TAKE_PICTURE:
                    //picture = (Bitmap) data.getExtras().get("data"); //只取得小圖
                    picture = downSize(out.getPath());
                    iv_recipe_pic.setImageBitmap(picture);
                    break;

                case REQUEST_PICK_IMAGE:
                    Uri uri = data.getData();
                    String[] columns = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getContentResolver().query(uri, columns,
                            null, null, null);
                    if (cursor.moveToFirst()) {
                        String imagePath = cursor.getString(0);
                        cursor.close();
//                        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);Log.d(TAG,"imagePath:" + imagePath);
                        picture = downSize(imagePath);
                        iv_recipe_pic.setImageBitmap(picture);
//                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
////                        picture.compress(Bitmap.CompressFormat.JPEG, 100, out2);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
//                        image = out2.toByteArray();
                    }
                    break;
            }
        }
    }


    private Bitmap downSize(String path) {
        Bitmap picture = BitmapFactory.decodeFile(path);
        String text = "original size = " + picture.getWidth() + "x" + picture.getHeight();
//        tvMessage.setText(text);
        // 設定寬度不超過width，並利用Options.inSampleSize來縮圖
        int scaleSize = 400;
        int longer = Math.max(picture.getWidth(), picture.getHeight());
        if (longer > scaleSize) {
            Options options = new Options();
            // 若原始照片寬度無法整除width，則inSampleSize + 1，
            // 若則inSampleSize = 3，實際縮圖時為2，參看javadoc
            options.inSampleSize = longer % scaleSize == 0 ?
                    longer / scaleSize : longer / scaleSize + 1;
            if (out != null) {
                picture = BitmapFactory.decodeFile(out.getPath(), options);
            } else {
                picture = BitmapFactory.decodeFile(path, options);
            }
            System.gc();
            text = "\ninSampleSize = " + options.inSampleSize +
                    "\nnew size = " + picture.getWidth() + "x" + picture.getHeight();
//            tvMessage.append(text);
        }
        return picture;
    }


    // check if the device connect to the network
    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    public boolean isIntentAvailable(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    @Override
    // 螢幕轉向之前，先將資料儲存至Bundle，方便轉向完畢後取出
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (picture != null) {
            outState.putParcelable("picture", picture);
        }
    }

    @Override
    // 螢幕轉向完畢後，將轉向之前存的資料取出
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Bitmap picture = savedInstanceState.getParcelable("picture");
        if (picture != null) {
            this.picture = picture;
            iv_recipe_pic.setImageBitmap(picture);
        }
    }

}
