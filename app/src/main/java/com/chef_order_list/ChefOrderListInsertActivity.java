package com.chef_order_list;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static com.main.Common.showToast;


/**
 * Created by cyh on 2016/12/28.
 */

public class ChefOrderListInsertActivity extends AppCompatActivity {
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private ProgressDialog progressDialog;
    private AsyncTask dataUploadTask;
    private File out;
    private static final String TAG = "ChefOrdListActivity";
    private final static String ACTION = "insert";
    private Bitmap picture;
    //    private ImageView ivTakePicture;
    private TextView tvMessage;
    private MemberVO memberVO;
    private ImageView iv_member_detail_mem_image;
    private TextView tv_member_detail_mem_no;
    private EditText et_chef_ord_cnt;
    private EditText et_chef_act_date;
    private TextView tv_chef_ord_cost;
    private EditText et_chef_ord_place;
    private TextView tv_chef_act_date;
    private DatePicker dp_chef_act_date;
    private Chef_order_listVO chef_order_listVO;
    private String mem_no;
    private String chef_no;
    private String personal_mem_no;

    private String chef_ord_cost;
    private String chef_act_date;
    private String chef_ord_place;
    private String chef_ord_cnt;
    private RadioButton rg_rb_noon;
    private RadioButton rg_rb_evening;

    private TextView tv_member_detail_mem_ac;
    private TextView tv_member_detail_mem_pw;
    private TextView tv_member_detail_mem_own;
    private TextView tv_member_detail_mem_online;
    private byte[] image;
    private String chooseDate;
//    private  String url = Common.URL + "MemberServletAndroid";
//    private Intent intent = getIntent().getSerializableExtra("memberVO");
//    private String mem_name = intent.getStringExtra(memberVO.getMem_name());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chef_order_list_insert_activity);

        et_chef_ord_cnt = (EditText) findViewById(R.id.et_chef_ord_cnt);
        et_chef_act_date = (EditText) findViewById(R.id.et_chef_act_date);
        tv_chef_ord_cost = (TextView) findViewById(R.id.tv_chef_ord_cost);
        et_chef_ord_place = (EditText) findViewById(R.id.et_chef_ord_place);
        tv_chef_act_date = (TextView) findViewById(R.id.tv_chef_act_date);
        rg_rb_noon = (RadioButton) findViewById(R.id.rg_rb_noon);
        rg_rb_evening = (RadioButton) findViewById(R.id.rg_rb_evening);

//        dp_chef_act_date = (DatePicker) findViewById(R.id.dp_chef_act_date);

        tvMessage = (TextView) findViewById(R.id.tvMessage);
    }


    @Override
    protected void onStart() {
        super.onStart();
//        Bundle bundle =  getIntent().getExtras();
//        memberVO = (MemberVO) getIntent().getExtras().getSerializable("memberVO");
        mem_no = (String) getIntent().getExtras().getSerializable("mem_no");
        chef_no = (String) getIntent().getExtras().getSerializable("chef_no");
        personal_mem_no = (String) getIntent().getExtras().getSerializable("personal_mem_no");

//        if (picture == null) {
//            Log.d(TAG,"picture is not null");
//            Bitmap bitmap = BitmapFactory.decodeByteArray(memberVO.getMem_image(), 0, memberVO.getMem_image().length);
//            iv_member_detail_mem_image.setImageBitmap(bitmap);
//        }else {
//            Log.d(TAG, "picture is null");
//        }
//        et_chef_ord_cnt.setText(chef_order_listVO.getChef_ord_cnt());
//        et_chef_act_date.setText(chef_order_listVO.getChef_act_date());
//        et_chef_ord_cost.setText(chef_order_listVO.getChef_ord_cost());
//        et_chef_ord_place.setText(chef_order_listVO.getChef_ord_place());


        /**************設定開始日期**************/
        tv_chef_act_date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                DialogFragment picker = new DatePickerFragment();
                picker.show(getSupportFragmentManager(), "datePicker");
            }

            class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String pickStartDate = sdf.format(calendar.getTime());
                    et_chef_act_date.setText(pickStartDate);
                }

                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    // Use the current date as the default date in the picker
                    final Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH);
                    int day = calendar.get(Calendar.DAY_OF_MONTH);

                    // Create a new instance of DatePickerDialog and return it
                    return new DatePickerDialog(getActivity(), this, year, month, day);
                }
            }
        });

        findViews();

    }

    private void findViews() {
        rg_rb_noon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate = " 10:00:00";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(ChefOrderListInsertActivity.this, "午餐時段", duration).show();
            }
        });
        rg_rb_evening.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDate = " 16:00:00";
                int duration = Toast.LENGTH_SHORT;
                Toast.makeText(ChefOrderListInsertActivity.this, "晚餐時段", duration).show();
            }
        });
    }

    @Override
    protected void onPause() {
        if (dataUploadTask != null) {
            dataUploadTask.cancel(true);
        }
        super.onPause();
    }

    //update cancel
    public void onChefOrderListInsertCancelClick(View view) {
        finish();
    }

    // update
    public void onChefOrderListInsertClick(View view) {
        if (networkConnected()) {
            String url = Common.URL + "Chef_order_listServletAndroid";
//            Log.d(TAG, "url:" + url);
//            dataUploadTask = new DataUploadTask().execute(url, memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_image());
            try {
//                if (picture != null) {
//                    ByteArrayOutputStream out = new ByteArrayOutputStream();
//                    picture.compress(Bitmap.CompressFormat.JPEG, 100, out);
//                    image = out.toByteArray();
//                }
                if (!et_chef_ord_cnt.getText().toString().trim().equals("")) {
                    chef_ord_cnt = et_chef_ord_cnt.getText().toString().trim();
//                    memberVO.setMem_name(et_member_detail_mem_name.getText().toString().trim());
                }
                if (!et_chef_act_date.getText().toString().trim().equals("")) {
                    chef_act_date = et_chef_act_date.getText().toString().trim();
                }
//                if (et_chef_ord_cost.getText().toString().trim() != null) {
//                    chef_ord_cost = et_chef_ord_cost.getText().toString().trim();
//                }

                if (!et_chef_ord_place.getText().toString().trim().equals("")) {
                    chef_ord_place = et_chef_ord_place.getText().toString().trim();
                }
                new ChefOrderListInsertTask().execute(url, ACTION, personal_mem_no, chef_no, 0, chef_act_date + chooseDate, chef_ord_place, chef_ord_cnt);
//                AsyncTask<Object, Integer, Integer> response;
//                try {
//                    response = new ChefOrderListInsertTask().execute(url, ACTION, personal_mem_no, chef_no, 0, chef_act_date + chooseDate, chef_ord_place, chef_ord_cnt);
//                    if (response.equals("500")) {
//                        showToast(this, R.string.msg_ErrorInput);
//                    }
//                } catch (Exception e) {
//                    Log.e(TAG, e.toString());
//                }
//                dataUploadTask = new DataUploadTask().execute(url, mem_no, chef_no, chef_ord_cost,
//                        chef_act_date, chef_ord_place, chef_ord_cnt);
                Toast.makeText(this, "新增成功", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }

    }


    class DataUploadTask extends AsyncTask<Object, Integer, MemberVO> {

        @Override
        // invoked on the UI thread immediately after the task is executed.
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChefOrderListInsertActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }

        @Override
        // invoked on the background thread immediately after onPreExecute()
        protected MemberVO doInBackground(Object... params) {
            String url = params[0].toString();
            String action = params[1].toString();
            String mem_no = params[2].toString();
            String chef_no = params[3].toString();
            String chef_ord_cost = params[4].toString();
            String chef_act_date = params[5].toString();
            String chef_ord_place = params[6].toString();
            String chef_ord_cnt = params[7].toString();

            String jsonIn;
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("action", ACTION);

            jsonObject.addProperty("mem_no", mem_no);
            jsonObject.addProperty("chef_no", chef_no);
            jsonObject.addProperty("chef_ord_cost", chef_ord_cost);
            jsonObject.addProperty("chef_act_date", chef_act_date);
            jsonObject.addProperty("chef_ord_place", chef_ord_place);
            jsonObject.addProperty("chef_ord_cnt", chef_ord_cnt);

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
//            String password = memberVO.getPassword();
            Bitmap bitmap = BitmapFactory.decodeByteArray(memberVO.getMem_image(), 0, memberVO.getMem_image().length);
//            EditText et_member_detail_mem_name = (EditText) findViewById(R.id.et_member_detail_mem_name);
//            EditText et_member_detail_mem_adrs = (EditText) findViewById(R.id.et_member_detail_mem_adrs);
//            EditText et_member_detail_mem_email = (EditText) findViewById(R.id.et_member_detail_mem_email);
//            EditText et_member_detail_mem_phone = (EditText) findViewById(R.id.et_member_detail_mem_phone);
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
        tvMessage.setText(text);
        // 設定寬度不超過width，並利用Options.inSampleSize來縮圖
        int scaleSize = 1024;
        int longer = Math.max(picture.getWidth(), picture.getHeight());
        if (longer > scaleSize) {
            Options options = new Options();
            // 若原始照片寬度無法整除width，則inSampleSize + 1，
            // 若則inSampleSize = 3，實際縮圖時為2，參看javadoc
            options.inSampleSize = longer % scaleSize == 0 ?
                    longer / scaleSize : longer / scaleSize + 1;
            picture = BitmapFactory.decodeFile(out.getPath(), options);
            System.gc();
            text = "\ninSampleSize = " + options.inSampleSize +
                    "\nnew size = " + picture.getWidth() + "x" + picture.getHeight();
            tvMessage.append(text);
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
