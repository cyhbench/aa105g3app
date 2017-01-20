package com.talk;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidbelieve.drawerwithswipetabs.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class InviteTalking extends AppCompatActivity {
    private static final String TAG = "InviteTalk";
//    private static final String SERVER_URI = "ws://10.0.2.2:8081/WebSocketChat_Web/MyWebSocketServer";
    private static final String USER_NAME = "Android";
    private static final String KEY_USER_NAME = "userName";
    private static final String KEY_MESSAGE = "message";

//    private String url = Common.URL + "ChefServletAndroid.do";

    private MyWebSocketClient myWebSocketClient;
    private TextView tv_friend_message;
    private TextView tv_member_message;
    private EditText etMessage;
    private ScrollView scrollView;
    private String mem_no;
    private String frd_no;

    class MyWebSocketClient extends WebSocketClient {

        public MyWebSocketClient(URI serverURI) {
            super(serverURI, new Draft_17());
        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.d(TAG, "onOpen: handshakedata.toString() = " + handshakedata.toString());
        }

        @Override
        public void onMessage(final String message) {
            Log.d(TAG, "onMessage: " + message);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        JSONObject jsonObject = new JSONObject(message);
                        String userName = jsonObject.get("from").toString();
                        String guestName = jsonObject.get("memberName").toString();
                        String message = jsonObject.get("content").toString();
                        String text = guestName + ": " + message + "\n";
//                        String text = userName + ": " + message + "\n";

//                        String userName = jsonObject.get(KEY_USER_NAME).toString();
//                        String message = jsonObject.get(KEY_MESSAGE).toString();
//                        String text = userName + ": " + message + "\n";

//                        JsonObject jsonObject2 = new JsonObject();
//                        jsonObject2.addProperty("from", mem_no);
//                        jsonObject2.addProperty("to", frd_no);
//                        jsonObject2.addProperty("content", message);
//                        mem_no = jsonObject.get("from").toString();
//                        frd_no = jsonObject.get("to").toString();
//                        message = jsonObject.get("content").toString();
//                        String text = mem_no + frd_no + message + "\n";
                        tv_friend_message.append(text);
                        scrollView.fullScroll(View.FOCUS_DOWN);
                    } catch (JSONException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            });
        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            String text = String.format(Locale.getDefault(),
                    "code = %d, reason = %s, remote = %b",
                    code, reason, remote);
            Log.d(TAG, "onClose: " + text);
        }

        @Override
        public void onError(Exception ex) {
            Log.d(TAG, "onError: exception = " + ex.toString());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.talk_activity_main);
        mem_no = (String) getIntent().getExtras().getSerializable("mem_no");
        frd_no = (String) getIntent().getExtras().getSerializable("frd_no");
        findViews();
        URI uri = null;
//        @ServerEndpoint("/ChatEndpoint/{mem_no}/{frd_no}")
        String SERVER_URI_cyh = "ws://10.0.2.2:8081/AA105G3/ChatEndpoint/" + mem_no + "/" + frd_no;
        try {
            uri = new URI(SERVER_URI_cyh);
        } catch (URISyntaxException e) {
            Log.e(TAG, e.toString());
        }
        myWebSocketClient = new MyWebSocketClient(uri);
        myWebSocketClient.connect();
    }

    private void findViews() {
        tv_friend_message = (TextView) findViewById(R.id.tv_friend_message);
        etMessage = (EditText) findViewById(R.id.etMessage);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
    }

    public void onSendClick(View view) {
        String message = etMessage.getText().toString();
        if (message.trim().isEmpty()) {
            showToast(R.string.text_MessageEmpty);
            return;
        }
        Map<String, String> map = new HashMap<>();
//        map.put(KEY_USER_NAME, USER_NAME);
//        map.put(KEY_MESSAGE, message);

        map.put("from", mem_no);
        map.put("to", frd_no);
        map.put("content", message);  // //map.put(message, "XXXX");
        if (myWebSocketClient != null) {
            myWebSocketClient.send(new JSONObject(map).toString());
//            tv_friend_message.set
            tv_friend_message.setRight(500);
            tv_friend_message.append("我:"+message+"\n");
            scrollView.fullScroll(View.FOCUS_DOWN);
            message.replace(message, "");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (myWebSocketClient != null) {
                myWebSocketClient.close();
                showToast(R.string.text_LeftChatRoom);
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    private void showToast(int messageId) {
        Toast.makeText(this, messageId, Toast.LENGTH_SHORT).show();
    }
}
