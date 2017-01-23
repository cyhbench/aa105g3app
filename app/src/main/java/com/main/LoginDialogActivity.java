package com.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.androidbelieve.drawerwithswipetabs.R;
import com.member.MemberGetOneByACTask;
import com.member.MemberVO;

// 此Activity將會以對話視窗模式顯示，呼叫setResult()設定回傳結果
public class LoginDialogActivity extends AppCompatActivity {
    private static final String TAG = "logMainActivityLogin";
    private EditText et_login_member_mem_ac;
    private EditText et_login_member_mem_pw;
    private TextView tv_login_message;
    private MemberVO memberVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        findViews();
        setResult(RESULT_CANCELED);
    }


    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences pref = getSharedPreferences(Common.PREF_FILE, MODE_PRIVATE);
        boolean login = pref.getBoolean("login", false);
        if (login) {
            String mem_ac = pref.getString("mem_ac", "");
            String mem_pw = pref.getString("mem_pw", "");
            if (isUserValid(mem_ac, mem_pw)) {
                setResult(RESULT_OK);
                finish();
            } else {
                showMessage(R.string.msg_InvalidUserOrPassword);
            }
        }
    }

    private void findViews() {
        et_login_member_mem_ac = (EditText) findViewById(R.id.et_login_member_mem_ac);
        et_login_member_mem_pw = (EditText) findViewById(R.id.et_login_member_mem_pw);
        Button bt_login = (Button) findViewById(R.id.bt_login);
        tv_login_message = (TextView) findViewById(R.id.tv_login_message);

        bt_login.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String mem_ac = et_login_member_mem_ac.getText().toString().trim();
                String mem_pw = et_login_member_mem_pw.getText().toString().trim();
                String mem_no, mem_name, mem_own, mem_online;
                if (mem_ac.length() <= 0 || mem_pw.length() <= 0) {
                    showMessage(R.string.msg_InvalidUserOrPassword);
                    return;
                }

                if (isUserValid(mem_ac, mem_pw)) {
                    SharedPreferences pref = getSharedPreferences(Common.PREF_FILE,
                            MODE_PRIVATE);
                    pref.edit()
                            .putBoolean("login", true)
                            .putString("mem_ac", mem_ac)
                            .putString("mem_pw", mem_pw)
                            .putString("mem_no", memberVO.getMem_no())
                            .putString("mem_name", memberVO.getMem_name())
                            .putString("mem_own", memberVO.getMem_own())
                            .putString("mem_online", memberVO.getMem_online())
                            .apply();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    showMessage(R.string.msg_InvalidUserOrPassword);
                }
            }
        });
    }


    private void showMessage(int msgResId) {
        tv_login_message.setText(msgResId);
    }

    private boolean isUserValid(String mem_ac, String mem_pw) {
        // 應該連線至server端檢查帳號密碼是否正確

        String url = Common.URL + "MemberServletAndroid";
        String action = "login";
        Boolean loginResult = false;

//        memberVO = new MemberGetOneByACTask(url, action, mem_ac).get();

        if (Common.networkConnected(this)) {
            try {
                memberVO = new MemberGetOneByACTask().execute(url, action, mem_ac, mem_pw).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        } else {
            Common.showToast(this, R.string.msg_NoNetwork);
        }

        if (memberVO!=null && memberVO.getMem_pw().equals(mem_pw)) {
            loginResult = true;
        }
        return loginResult;
    }
}
