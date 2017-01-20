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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

import static com.main.Common.showToast;


/**
 * Created by cyh on 2016/12/28.
 */

public class RecipeContInsertActivity extends AppCompatActivity {
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private ProgressDialog progressDialog;
    private AsyncTask dataUploadTask;
    private File out;
    private static final String TAG = "新增食譜步驟";
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
    private ImageView ivsp1;
    private EditText etrs1;
    private ImageView ivsp2;
    private EditText etrs2;
    private ImageView ivsp3;
    private EditText etrs3;
    private ImageView ivsp4;
    private EditText etrs4;
    private ImageView ivsp5;
    private EditText etrs5;
    private ImageView ivsp6;
    private EditText etrs6;
    private ImageView ivsp7;
    private EditText etrs7;

    private List<String> etrsList = new ArrayList<String>();
    private List<byte[]> stepPicList = new ArrayList<byte[]>();
    private Recipe_contVO recipe_contVO = new Recipe_contVO();

    private String recipe_no;

    private Button bt_recipe_cont_insert_sure;
    private Button bt_recipe_cont_insert_cancel;

    private RelativeLayout rls2;
    private RelativeLayout rls3;
    private RelativeLayout rls4;
    private RelativeLayout rls5;
    private RelativeLayout rls6;
    private RelativeLayout rls7;

    private int i = 0;
    private int j = 1;

//    private Button btstp1;
//    private Button btspp1;
//    private Button btstp2;
//    private Button btspp2;
//    private Button btstp3;
//    private Button btspp3;
//    private Button btstp4;
//    private Button btspp4;
//    private Button btstp5;
//    private Button btspp5;
//    private Button btstp6;
//    private Button btspp6;
//    private Button btstp7;
//    private Button btspp7;


//    private  String url = Common.URL + "MemberServletAndroid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_cont_insert_activity);

        iv_recipe_pic = (ImageView) findViewById(R.id.iv_recipe_pic);
        et_recipe_name = (EditText) findViewById(R.id.et_recipe_name);
        et_recipe_intro = (EditText) findViewById(R.id.et_recipe_intro);
//        et_recipe_mater = (ImageView) findViewById(R.id.ivsp1);
        add_mater_container = (ScrollView) findViewById(R.id.add_mater_container);
//        containerLayout = (LinearLayout) findViewById(R.id.mlayout);

        ivsp1 = (ImageView) findViewById(R.id.ivsp1);
        etrs1 = (EditText) findViewById(R.id.etrs1);
        ivsp2 = (ImageView) findViewById(R.id.ivsp2);
        etrs2 = (EditText) findViewById(R.id.etrs2);
        ivsp3 = (ImageView) findViewById(R.id.ivsp3);
        etrs3 = (EditText) findViewById(R.id.etrs3);
        ivsp4 = (ImageView) findViewById(R.id.ivsp4);
        etrs4 = (EditText) findViewById(R.id.etrs4);
        ivsp5 = (ImageView) findViewById(R.id.ivsp5);
        etrs5 = (EditText) findViewById(R.id.etrs5);
        ivsp6 = (ImageView) findViewById(R.id.ivsp6);
        etrs6 = (EditText) findViewById(R.id.etrs6);
        ivsp7 = (ImageView) findViewById(R.id.ivsp7);
        etrs7 = (EditText) findViewById(R.id.etrs7);
//        tv_recipe_name = (TextView) findViewById(tv_recipe_name);

        bt_recipe_cont_insert_sure = (Button) findViewById(R.id.bt_recipe_cont_insert_sure);
        bt_recipe_cont_insert_cancel = (Button) findViewById(R.id.bt_recipe_cont_insert_cancel);

        rls2 = (RelativeLayout) findViewById(R.id.rls2);
        rls3 = (RelativeLayout) findViewById(R.id.rls3);
        rls4 = (RelativeLayout) findViewById(R.id.rls4);
        rls5 = (RelativeLayout) findViewById(R.id.rls5);
        rls6 = (RelativeLayout) findViewById(R.id.rls6);
        rls7 = (RelativeLayout) findViewById(R.id.rls7);

        recipe_no = getIntent().getExtras().getString("recipe_no");
        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onPause() {
        if (dataUploadTask != null) {
            dataUploadTask.cancel(true);
        }
        super.onPause();
    }

    // 拍照
    public void onRecipeContInsertTakePicClick1(View view) {
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
    public void onRecipeContPickPicClick1(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // 拍照
    public void onRecipeContInsertTakePicClick2(View view) {
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
    public void onRecipeContPickPicClick2(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // 拍照
    public void onRecipeContInsertTakePicClick3(View view) {
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
    public void onRecipeContPickPicClick3(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // 拍照
    public void onRecipeContInsertTakePicClick4(View view) {
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
    public void onRecipeContPickPicClick4(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // 拍照
    public void onRecipeContInsertTakePicClick5(View view) {
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
    public void onRecipeContPickPicClick5(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // 拍照
    public void onRecipeContInsertTakePicClick6(View view) {
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
    public void onRecipeContPickPicClick6(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    // 拍照
    public void onRecipeContInsertTakePicClick7(View view) {
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
    public void onRecipeContPickPicClick7(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    //取消新增食譜步驟click
    public void onRecipeContInsertCancelClick(View view) {
        finish();
    }

    // 開始上傳:新增食譜步驟click
    public void onRecipeContInsertClick(View view) {
        Toast.makeText(this, "開始新增", Toast.LENGTH_SHORT).show();
        if (networkConnected()) {
            progressDialog = new ProgressDialog(RecipeContInsertActivity.this);
            progressDialog.setMessage("新增中...");
            progressDialog.show();
            String url = Common.URL + "recipe_cont/recipe_cont_android.do";
            Log.d(TAG, "url:" + url);
//            dataUploadTask = new DataUploadTask().execute(url, memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_image());
            try {
//                if (picture != null) {
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    image = out.toByteArray();
//                }
//                if (etrs1.getText().toString().trim() != null) {
//                    recipe_name = etrs1.getText().toString().trim();
//                }
//
//                if (ivsp2.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "+" + ivsp2.getText().toString().trim();
//                }
//
//                if (etrs2.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "-" + etrs2.getText().toString().trim();
//                }
//
//                if (ivsp3.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "+" + ivsp3.getText().toString().trim();
//                }
//
//                if (etrs3.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "-" + etrs3.getText().toString().trim();
//                }
//
//                if (ivsp4.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "+" + ivsp4.getText().toString().trim();
//                }
//
//                if (etrs4.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "-" + etrs4.getText().toString().trim();
//                }
//
//                if (ivsp5.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "+" + ivsp5.getText().toString().trim();
//                }
//
//                if (etrs5.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "-" + etrs5.getText().toString().trim();
//                }
//
//                if (ivsp6.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "+" + ivsp6.getText().toString().trim();
//                }
//
//                if (etrs6.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "-" + etrs6.getText().toString().trim();
//                }
//
//                if (ivsp7.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "+" + ivsp7.getText().toString().trim();
//                }
//
//                if (etrs7.getText().toString().trim() != null) {
//                    recipe_mater = recipe_mater + "-" + etrs7.getText().toString().trim();
//                }
//                if (image != null) {
//                    recipeVO.setRecipe_pic(image);
//                }
//                else {
//                    recipeVO.setRecipe_pic();
////                }
//                recipeVO.setMem_no(mem_no);
//                recipeVO.setRecipe_name(recipe_name);
//                recipeVO.setRecipe_intro(recipe_intro);
//                recipeVO.sivspood_mater(recipe_mater);

                if (foodCount == 1) {   //只有1個步驟,所以要把第一個recipe_cont放入etrsList
                    etrsList.add(etrs1.getText().toString().trim());
                } else if (foodCount == 2) {
                    if (!etrs2.getText().toString().trim().equals("")) {
                        etrsList.add(etrs2.getText().toString().trim());
                    }
                } else if (foodCount == 3) {
                    if (!etrs3.getText().toString().trim().equals("")) {
                        etrsList.add(etrs3.getText().toString().trim());
                    }
                } else if (foodCount == 4) {
                    if (!etrs4.getText().toString().trim().equals("")) {
                        etrsList.add(etrs4.getText().toString().trim());
                    }
                } else if (foodCount == 5) {
                    if (!etrs5.getText().toString().trim().equals("")) {
                        etrsList.add(etrs5.getText().toString().trim());
                    }
                } else if (foodCount == 6) {
                    if (!etrs6.getText().toString().trim().equals("")) {
                        etrsList.add(etrs6.getText().toString().trim());
                    }
                } else if (foodCount == 7) {
                    if (!etrs7.getText().toString().trim().equals("")) {
                        etrsList.add(etrs7.getText().toString().trim());
                    }
                }


                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (image != null) {
                    stepPicList.add(image);
                }

                recipe_contVO.setRecipe_no(recipe_no);

                for (i = 1; i <= foodCount; i++) {
                    recipe_contVO.setStep(i);
                    recipe_contVO.setStep_cont(etrsList.get(i - 1));
                    recipe_contVO.setStep_pic(stepPicList.get(i - 1));
                    new DataUploadTask().execute(url, recipe_contVO).get();
                }

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }
        Toast.makeText(this, "新增成功", Toast.LENGTH_LONG).show();
//        progressDialog.cancel();
        finish();
    }

    //新增步驟
    public void onRecipeContInsert_AddStepClick(View view) {
        foodCount++;

        switch (foodCount) {

            case 2:
                if (etrs1.getText().toString().trim() != null) {
                    etrsList.add(etrs1.getText().toString().trim());
                }
                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (image != null) {
                    stepPicList.add(image);
                }
                rls2.setVisibility(View.VISIBLE);

//                ivsp2.setVisibility(View.VISIBLE);
//                etrs2.setVisibility(View.VISIBLE);
                break;

            case 3:
                if (etrs2.getText().toString().trim() != null) {
                    etrsList.add(etrs2.getText().toString().trim());
                }
                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (image != null) {
                    stepPicList.add(image);
                }
                rls3.setVisibility(View.VISIBLE);
//                ivsp3.setVisibility(View.VISIBLE);
//                etrs3.setVisibility(View.VISIBLE);
                break;

            case 4:
                if (etrs3.getText().toString().trim() != null) {
                    etrsList.add(etrs3.getText().toString().trim());
                }
                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (image != null) {
                    stepPicList.add(image);
                }
                rls4.setVisibility(View.VISIBLE);
//                ivsp4.setVisibility(View.VISIBLE);
//                etrs4.setVisibility(View.VISIBLE);
                break;

            case 5:
                if (etrs4.getText().toString().trim() != null) {
                    etrsList.add(etrs4.getText().toString().trim());
                }
                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (image != null) {
                    stepPicList.add(image);
                }
                rls5.setVisibility(View.VISIBLE);
//                ivsp5.setVisibility(View.VISIBLE);
//                etrs5.setVisibility(View.VISIBLE);
                break;

            case 6:
                if (etrs5.getText().toString().trim() != null) {
                    etrsList.add(etrs5.getText().toString().trim());
                }
                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (image != null) {
                    stepPicList.add(image);
                }
                rls6.setVisibility(View.VISIBLE);
//                ivsp6.setVisibility(View.VISIBLE);
//                etrs6.setVisibility(View.VISIBLE);
                break;

            case 7:
                if (etrs6.getText().toString().trim() != null) {
                    etrsList.add(etrs6.getText().toString().trim());
                }
                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (image != null) {
                    stepPicList.add(image);
                }
                rls7.setVisibility(View.VISIBLE);
//                ivsp7.setVisibility(View.VISIBLE);
//                etrs7.setVisibility(View.VISIBLE);
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


    class DataUploadTask extends AsyncTask<Object, Integer, Recipe_contVO> {

        @Override
        // invoked on the UI thread immediately after the task is executed.
        protected void onPreExecute() {
            super.onPreExecute();
//            if (j == 1) {
//                progressDialog = new ProgressDialog(RecipeContInsertActivity.this);
//                progressDialog.setMessage("Loading...");
//                progressDialog.show();
//            }
//            j++;

        }

        @Override
        // invoked on the background thread immediately after onPreExecute()
        protected Recipe_contVO doInBackground(Object... params) {
            String url = params[0].toString();
            recipe_contVO = (Recipe_contVO) params[1];
//            String mem_no = params[1].toString();
//            String mem_name = params[2].toString();
//            String password = params[2].toString();
//            byte[] image = (byte[]) params[2];
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", ACTION);
            jsonObject.addProperty("recipe_contVO", new Gson().toJson(recipe_contVO));
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
            recipe_contVO = gson.fromJson(jsonIn, Recipe_contVO.class);

//            MemberVO memberVO = new MemberVO(
//                    jObject.get("mem_no").getAsString(),
//                    jObject.get("mem_name").getAsString(),
//                    jObject.get("password").getAsString(),
//                    Base64.decode(jObject.get("mem_imageBase64").getAsString(), Base64.DEFAULT)
//            );
            return recipe_contVO;
        }

        @Override
        /*
         * invoked on the UI thread after the background computation finishes.
		 * The result of the background computation is passed to this step as a
		 * parameter.
		 */
        protected void onPostExecute(Recipe_contVO recipe_contVO) {
//            String mem_no = recipeVO.getMem_no();
//            String password = memberVO.getPassword();
            Bitmap bitmap;
            if (recipe_contVO.getStep_pic() != null) {
                bitmap = BitmapFactory.decodeByteArray(recipe_contVO.getStep_pic(), 0, recipe_contVO.getStep_pic().length);
//                ImageView iv_recipe_pic = (ImageView) findViewById(R.id.iv_recipe_pic);
//                iv_recipe_pic.setImageBitmap(bitmap);
            } else {
//                iv_recipe_pic.setImageDrawable(getResources().getDrawable(R.drawable.default_image));
            }

//            String text = "name: " + name + "\npassword: " + password;
//            String text = mem_name;
//            et_member_detail_mem_name.setText(text);
//            j--;
//            if (j == 1) {
//                progressDialog.cancel();
//            }
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
                    switch (foodCount) {
                        case 1:
                            ivsp1.setImageBitmap(picture);
                            break;
                        case 2:
                            ivsp2.setImageBitmap(picture);
                            break;
                        case 3:
                            ivsp3.setImageBitmap(picture);
                            break;
                        case 4:
                            ivsp4.setImageBitmap(picture);
                            break;
                        case 5:
                            ivsp5.setImageBitmap(picture);
                            break;
                        case 6:
                            ivsp6.setImageBitmap(picture);
                            break;
                        case 7:
                            ivsp7.setImageBitmap(picture);
                            break;
                    }
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
                        switch (foodCount) {
                            case 1:
                                ivsp1.setImageBitmap(picture);
                                break;
                            case 2:
                                ivsp2.setImageBitmap(picture);
                                break;
                            case 3:
                                ivsp3.setImageBitmap(picture);
                                break;
                            case 4:
                                ivsp4.setImageBitmap(picture);
                                break;
                            case 5:
                                ivsp5.setImageBitmap(picture);
                                break;
                            case 6:
                                ivsp6.setImageBitmap(picture);
                                break;
                            case 7:
                                ivsp7.setImageBitmap(picture);
                                break;
                        }
//                        iv_recipe_pic.setImageBitmap(picture);
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
