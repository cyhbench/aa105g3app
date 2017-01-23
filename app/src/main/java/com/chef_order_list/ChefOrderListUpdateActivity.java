package com.chef_order_list;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.R;
import com.main.Common;
import com.member.MemberVO;

import java.io.File;

import static com.main.Common.showToast;


/**
 * Created by cyh on 2016/12/28.
 */

public class ChefOrderListUpdateActivity extends AppCompatActivity {
    private static final int REQ_TAKE_PICTURE = 0;
    private static final int REQUEST_PICK_IMAGE = 1;
    private ProgressDialog progressDialog;
    private AsyncTask dataUploadTask;
    private File out;
    private static final String TAG = "會員的私廚訂單-修改Atvty";
    private final static String ACTION = "update";
    private Bitmap picture;
    //    private ImageView ivTakePicture;
    private TextView tvMessage;
    private MemberVO memberVO;
    private ImageView iv_member_detail_mem_image;
    private TextView tv_member_detail_mem_no;
    private TextView et_chef_ord_cnt;
    private TextView et_chef_act_date;
    private TextView tv_chef_ord_cost;
    private TextView et_chef_ord_place;
    private TextView tv_chef_act_date;
    private TextView et_chef_ord_cost;
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

        setContentView(R.layout.chef_order_list_update_for_member_activity);

        et_chef_ord_cnt = (TextView) findViewById(R.id.et_chef_ord_cnt);
        et_chef_act_date = (TextView) findViewById(R.id.et_chef_act_date);
        tv_chef_ord_cost = (TextView) findViewById(R.id.tv_chef_ord_cost);
        et_chef_ord_place = (TextView) findViewById(R.id.et_chef_ord_place);
        tv_chef_act_date = (TextView) findViewById(R.id.tv_chef_act_date);
        et_chef_ord_cost = (TextView) findViewById(R.id.et_chef_ord_cost);
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
        chef_order_listVO = (Chef_order_listVO) getIntent().getExtras().getSerializable("chef_order_listVO");

        et_chef_ord_cnt.setText(chef_order_listVO.getChef_ord_cnt().toString().trim());
        if (chef_order_listVO.getChef_act_date().toString().trim().subSequence(11, 13).equals("10")) {
            et_chef_act_date.setText(chef_order_listVO.getChef_act_date().toString().trim().subSequence(0, 10) + " 午餐時段");
        } else {
            et_chef_act_date.setText(chef_order_listVO.getChef_act_date().toString().trim().subSequence(0, 10) + " 晚餐時段");
        }
        et_chef_ord_cost.setText(chef_order_listVO.getChef_ord_cost().shortValue() + " 元整"); //訂單金額不可以有 .0
        et_chef_ord_place.setText(chef_order_listVO.getChef_ord_place().toString().trim());

    }

    @Override
    protected void onPause() {
        if (dataUploadTask != null) {
            dataUploadTask.cancel(true);
        }
        super.onPause();
    }

    //update cancel
    public void onChefOrderListDenyClick(View view) {
        if (networkConnected()) {
            String url = Common.URL + "Chef_order_listServletAndroid";
//            Log.d(TAG, "url:" + url);
//            dataUploadTask = new DataUploadTask().execute(url, memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_image());
            try {
                AsyncTask<Object, Integer, Integer> response;
                try {
                    response = new ChefOrderListUpdateTask().execute(url, ACTION, personal_mem_no,
                            chef_order_listVO.getChef_no(), chef_order_listVO.getChef_ord_cost(),
                            chef_order_listVO.getChef_act_date(), chef_order_listVO.getChef_ord_place(),
                            chef_order_listVO.getChef_ord_cnt(), "0", chef_order_listVO.getChef_ord_no());
                    Toast.makeText(this, "訂單重新定價", Toast.LENGTH_SHORT).show();
                    finish();
                    if (response.equals("500")) {
                        showToast(this, R.string.msg_ErrorInput);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
//                dataUploadTask = new DataUploadTask().execute(url, mem_no, chef_no, chef_ord_cost,
//                        chef_act_date, chef_ord_place, chef_ord_cnt);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }
        finish();
    }

    // update
    public void onChefOrderListAgreeClick(View view) {
        if (networkConnected()) {
            String url = Common.URL + "Chef_order_listServletAndroid";
//            Log.d(TAG, "url:" + url);
//            dataUploadTask = new DataUploadTask().execute(url, memberVO.getMem_no(), memberVO.getMem_name(), memberVO.getMem_image());
            try {
                AsyncTask<Object, Integer, Integer> response;
                try {
                    response = new ChefOrderListUpdateTask().execute(url, ACTION, personal_mem_no,
                            chef_order_listVO.getChef_no(), chef_order_listVO.getChef_ord_cost(),
                            chef_order_listVO.getChef_act_date(), chef_order_listVO.getChef_ord_place(),
                            chef_order_listVO.getChef_ord_cnt(), "2", chef_order_listVO.getChef_ord_no());
                    Toast.makeText(this, "訂單確認", Toast.LENGTH_SHORT).show();
                    finish();
                    if (response.equals("500")) {
                        showToast(this, R.string.msg_ErrorInput);
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }
//                dataUploadTask = new DataUploadTask().execute(url, mem_no, chef_no, chef_ord_cost,
//                        chef_act_date, chef_ord_place, chef_ord_cnt);
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            showToast(this, R.string.msg_NoNetwork);
        }
    }

    // check if the device connect to the network
    private boolean networkConnected() {
        ConnectivityManager conManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


}
