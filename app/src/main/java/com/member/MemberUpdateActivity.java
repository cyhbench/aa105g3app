package com.member;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ChefPageMainActivity;
import com.MemberPageMainActivity;
import com.androidbelieve.drawerwithswipetabs.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.main.Common;

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

public class MemberUpdateActivity extends AppCompatActivity {
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private ProgressDialog progressDialog;
    private AsyncTask dataUploadTask;
    private File out;
    private static final String TAG = "logMemberUpdateActivity";
    private final static String ACTION = "update";
    private Bitmap picture;
    //    private ImageView ivTakePicture;
    private TextView tvMessage;
    private MemberVO memberVO;
    private ImageView iv_member_detail_mem_image;
    private EditText et_member_detail_mem_name;
    private EditText et_member_detail_mem_phone;
    private EditText et_member_detail_mem_email;
    private EditText et_member_detail_mem_adrs;
    private TextView tv_member_detail_mem_ac;
    private TextView tv_member_detail_mem_pw;
    private TextView tv_member_detail_mem_own;
    private TextView tv_member_detail_mem_online;
    private byte[] image;
//    private  String url = Common.URL + "MemberServletAndroid";
//    private Intent intent = getIntent().getSerializableExtra("memberVO");
//    private String mem_name = intent.getStringExtra(memberVO.getMem_name());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.member_update_activity);

        iv_member_detail_mem_image = (ImageView) findViewById(R.id.iv_member_detail_mem_image);
        et_member_detail_mem_name = (EditText) findViewById(R.id.et_member_detail_mem_name);
        et_member_detail_mem_adrs = (EditText) findViewById(R.id.et_member_detail_mem_adrs);
        et_member_detail_mem_email = (EditText) findViewById(R.id.et_member_detail_mem_email);
        et_member_detail_mem_phone = (EditText) findViewById(R.id.et_member_detail_mem_phone);
//        ivTakePicture = (ImageView) findViewById(R.id.ivTakePicture);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }


    @Override
    protected void onStart() {
        super.onStart();
//        Bundle bundle =  getIntent().getExtras();
        memberVO = (MemberVO) getIntent().getExtras().getSerializable("memberVO");
        Log.d(TAG, "line 45: " + memberVO.getMem_name());
        if (picture == null) {
            Log.d(TAG, "picture is not null");
            Bitmap bitmap = BitmapFactory.decodeByteArray(memberVO.getMem_image(), 0, memberVO.getMem_image().length);
            iv_member_detail_mem_image.setImageBitmap(bitmap);
        } else {
            Log.d(TAG, "picture is null");
        }
        et_member_detail_mem_name.setText(memberVO.getMem_name());
        et_member_detail_mem_phone.setText(memberVO.getMem_phone());
        et_member_detail_mem_email.setText(memberVO.getMem_email());
        et_member_detail_mem_adrs.setText(memberVO.getMem_adrs());
    }

    @Override
    protected void onPause() {
        if (dataUploadTask != null) {
            dataUploadTask.cancel(true);
        }
        super.onPause();
    }

    // 拍照
    public void onMemberUpdateTakePicClick(View view) {
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

//    private void takePicture() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        // 指定存檔路徑
//        out = Environment.getExternalStorageDirectory();
//        out = new File(out, "photo.jpg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(out));
//        if (isIntentAvailable(this, intent)) {
//            startActivityForResult(intent, REQ_TAKE_PICTURE);
//        } else {
//            showToast(this, R.string.msg_NoCameraApp);
//        }
//    }

    //pick picture from storage
    public void onMemberPickPicClick(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    //update cancel
    public void onMemberCancelUpdateClick(View view) {
        finish();
    }

    // update
    public void onMemberDetailUpdateClick(View view) {
        if (networkConnected()) {
            String url = Common.URL + "MemberServletAndroid";
//            Log.d(TAG, "url:" + url);
//            dataUploadTask = new DataUploadTask().execute(url, memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_image());
            try {
                if (picture != null) {
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    image = out.toByteArray();
                }
                if (!et_member_detail_mem_name.getText().toString().trim().equals("")) {
                    memberVO.setMem_name(et_member_detail_mem_name.getText().toString().trim());
                }
                if (!et_member_detail_mem_adrs.getText().toString().trim().equals("")) {
                    memberVO.setMem_adrs(et_member_detail_mem_adrs.getText().toString().trim());
                }
                if (!et_member_detail_mem_email.getText().toString().trim().equals("")) {
                    memberVO.setMem_email(et_member_detail_mem_email.getText().toString().trim());
                }
                if (!et_member_detail_mem_phone.getText().toString().trim().equals("")) {
                    memberVO.setMem_phone(et_member_detail_mem_phone.getText().toString().trim());
                }
                if (image != null) {
                    memberVO.setMem_image(image);
                }
                dataUploadTask = new DataUploadTask().execute(url, memberVO);

                if (dataUploadTask != null) {
                    Toast.makeText(this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(this, "修改失敗", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }

//        if (memberVO.getMem_own().equals("0")) {
//            Intent intent = new Intent(this, MemberPageMainActivity.class);
//            startActivity(intent);
//            finish();
//        } else if (memberVO.getMem_own().equals("1")) {
//            Intent intent = new Intent(this, ChefPageMainActivity.class);
//            startActivity(intent);
//            finish();
//        }

    }


    class DataUploadTask extends AsyncTask<Object, Integer, MemberVO> {

        @Override
        // invoked on the UI thread immediately after the task is executed.
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MemberUpdateActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        // invoked on the background thread immediately after onPreExecute()
        protected MemberVO doInBackground(Object... params) {
            String url = params[0].toString();
            memberVO = (MemberVO) params[1];
//            String mem_no = params[1].toString();
//            String mem_name = params[2].toString();
//            String password = params[2].toString();
//            byte[] image = (byte[]) params[2];
            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", ACTION);
            jsonObject.addProperty("memberVO", new Gson().toJson(memberVO));
//            jsonObject.addProperty("mem_no", mem_no);
//            jsonObject.addProperty("mem_name", mem_name);
//            jsonObject.addProperty("password", password);
//            jsonObject.addProperty("mem_imageBase64", Base64.encodeToString(image, Base64.DEFAULT));
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
            memberVO = gson.fromJson(jsonIn, MemberVO.class);

//            MemberVO memberVO = new MemberVO(
//                    jObject.get("mem_no").getAsString(),
//                    jObject.get("mem_name").getAsString(),
//                    jObject.get("password").getAsString(),
//                    Base64.decode(jObject.get("mem_imageBase64").getAsString(), Base64.DEFAULT)
//            );
            return memberVO;
        }

        @Override
        /*
         * invoked on the UI thread after the background computation finishes.
		 * The result of the background computation is passed to this step as a
		 * parameter.
		 */
        protected void onPostExecute(MemberVO memberVO) {
            String mem_no = memberVO.getMem_no();
            String mem_name = memberVO.getMem_name();
            Bitmap bitmap = BitmapFactory.decodeByteArray(memberVO.getMem_image(), 0, memberVO.getMem_image().length);
            ImageView iv_member_detail_mem_image = (ImageView) findViewById(R.id.iv_member_detail_mem_image);
//            String text = "name: " + name + "\npassword: " + password;
//            String text = mem_name;
//            et_member_detail_mem_name.setText(text);
            iv_member_detail_mem_image.setImageBitmap(bitmap);
            progressDialog.cancel();
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
            Log.d(TAG, "237 line: 200 response code");
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
                    iv_member_detail_mem_image.setImageBitmap(picture);
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
                        iv_member_detail_mem_image.setImageBitmap(picture);
//                        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
////                        picture.compress(Bitmap.CompressFormat.JPEG, 100, out2);
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out2);
//                        image = out2.toByteArray();
                    }
                    break;
                // 也可取得自行設計登入畫面的帳號密碼
//                case REQ_LOGIN:
//                    SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
//                            MODE_PRIVATE);
//                    String name = pref.getString("user", "");
//                    String password = pref.getString("password", "");
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    byte[] image = out.toByteArray();
//                    if (networkConnected()) {
//                        dataUploadTask = new DataUploadTask().execute(Common.URL, name, password, image);
//                    } else {
//                        showToast(this, R.string.msg_NoNetwork);
//                    }
//                    break;
            }
        }
    }


    private Bitmap downSize(String path) {
        Bitmap picture = BitmapFactory.decodeFile(path);
        String text = "original size = " + picture.getWidth() + "x" + picture.getHeight();
//        tvMessage.setText(text);
        // 設定寬度不超過width，並利用Options.inSampleSize來縮圖
        int scaleSize = 300;
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
            iv_member_detail_mem_image.setImageBitmap(picture);
        }
    }

}
