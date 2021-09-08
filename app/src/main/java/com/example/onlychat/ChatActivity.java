package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.onlychat.adapter.MsgAdapter;
import com.example.onlychat.data.Data;
import com.example.onlychat.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static String username;
    private static String token;
    private static String type;
    private static String chatid;
    String url;
    List<Msg> msgList;

    public static String getUsername() {
        return username;
    }

    RecyclerView recyclerView;
    EditText editText;
    Button button;
    ImageButton imageButton;
    MsgAdapter msgAdpter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        username = bundle.getString("username");
        token = bundle.getString("token");
        chatid = bundle.getString("chatid");
        url = Data.urlGetMsg + "?Client_id=" + username + "&Token=" + token + "&type=" + type + "&target_id="+chatid;
        recyclerView = findViewById(R.id.chat_recycler);
        editText = findViewById(R.id.msg_edittext);
        button = findViewById(R.id.msg_button);
        imageButton = findViewById(R.id.imageButton);

        msgList = new ArrayList<>();
        msgAdpter = new MsgAdapter(msgList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(msgAdpter);

    }


    @Override
    protected void onStart() {
        super.onStart();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String error = response.optString("error");
                Log.i("type",type);
                switch (error) {
                    case "0":
                        try {
                            JSONArray jsonArray = response.getJSONArray("msglist");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String sender = jsonObject.getString("sender");

                                String sender_id = jsonObject.getString("sender_id");
                                String record_guid = jsonObject.getString("record_guid");
                                String contents = jsonObject.getString("contents");
                                String contentstype = jsonObject.getString("contentstype");
                                String sendtime = jsonObject.getString("sendtime");

                                Msg msg = new Msg(sender, sender_id, contents, contentstype);
                                msgList.add(msg);
                                //Log.i("index",msg.getSender()+msg.getSender_id()+msg.getContents()+msg.getContentstype());
                            }
                            msgAdpter.notifyDataSetChanged();
                            //recyclerView.scrollToPosition(msgList.size() - 1);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                    case "100":
                        Toast.makeText(getApplicationContext(), "必选参数为空", Toast.LENGTH_SHORT).show();
                        break;
                    case "101":
                        Toast.makeText(getApplicationContext(), "Token验证失败", Toast.LENGTH_SHORT).show();
                        break;
                    case "102":
                        Toast.makeText(getApplicationContext(), "暂无聊天记录", Toast.LENGTH_SHORT).show();
                        break;
                    case "103":
                        Toast.makeText(getApplicationContext(), "Type消息类型错误", Toast.LENGTH_SHORT).show();
                        break;
                    case "405":
                        Toast.makeText(getApplicationContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "请求出错", Toast.LENGTH_SHORT).show();

            }
        });
        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

        //点击“发送”按钮事件
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Client_id", username);
                    jsonObject.put("Token", token);
                    jsonObject.put("Type", type);
                    jsonObject.put("Contents", msg);
                    jsonObject.put("ContentsType", "text");
                    jsonObject.put("target_id",chatid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, Data.urlSendMsg, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = response.optString("code");
                        switch (code) {
                            case "0":
                                Msg newMsg = new Msg("myself", username, msg, "text");
                                msgList.add(newMsg);
                                msgAdpter.notifyItemInserted(msgList.size() - 1);
                                recyclerView.scrollToPosition(msgList.size() - 1);
                                editText.setText("");
                                Toast.makeText(getApplicationContext(), "已发送", Toast.LENGTH_SHORT).show();
                                break;
                            case "100":
                                Toast.makeText(getApplicationContext(), "参数为空", Toast.LENGTH_SHORT).show();
                                break;
                            case "101":
                                Toast.makeText(getApplicationContext(), "Token验证失败", Toast.LENGTH_SHORT).show();
                                break;
                            case "102":
                                Toast.makeText(getApplicationContext(), "接收人不属于《移动开发课程》", Toast.LENGTH_SHORT).show();
                                break;
                            case "103":
                                Toast.makeText(getApplicationContext(), "添加失败，请重试", Toast.LENGTH_SHORT).show();
                                break;
                            case "104":
                                Toast.makeText(getApplicationContext(), "图片文件错误", Toast.LENGTH_SHORT).show();
                                break;
                            case "105":
                                Toast.makeText(getApplicationContext(), "表情系统暂未开放", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "请求出错", Toast.LENGTH_SHORT).show();
                    }
                });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest1);

            }
        });

        //点击“相册”图标
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && null != data) {
            ContentResolver resolver = getContentResolver();

            try {
                InputStream inputStream = null;
                inputStream = resolver.openInputStream(data.getData());
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("Client_id", username);
                    jsonObject.put("Token", token);
                    jsonObject.put("Type", type);
                    jsonObject.put("Contents", Util.bitmapTobase64(bitmap));
                    jsonObject.put("ContentsType", "image");
                    jsonObject.put("target_id",chatid);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, Data.urlSendMsg, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String code = response.optString("code");
                        switch (code) {
                            case "0":
                                Msg newMsg = new Msg("myself", username, Util.bitmapTobase64(bitmap), "image");
                                msgList.add(newMsg);
                                msgAdpter.notifyItemInserted(msgList.size() - 1);
                                recyclerView.scrollToPosition(msgList.size() - 1);
                                Toast.makeText(getApplicationContext(), "已发送", Toast.LENGTH_SHORT).show();
                                break;
                            case "100":
                                Toast.makeText(getApplicationContext(), "参数为空", Toast.LENGTH_SHORT).show();
                                break;
                            case "101":
                                Toast.makeText(getApplicationContext(), "Token验证失败", Toast.LENGTH_SHORT).show();
                                break;
                            case "102":
                                Toast.makeText(getApplicationContext(), "接收人不属于《移动开发课程》", Toast.LENGTH_SHORT).show();
                                break;
                            case "103":
                                Toast.makeText(getApplicationContext(), "添加失败，请重试", Toast.LENGTH_SHORT).show();
                                break;
                            case "104":
                                Toast.makeText(getApplicationContext(), "图片文件错误", Toast.LENGTH_SHORT).show();
                                break;
                            case "105":
                                Toast.makeText(getApplicationContext(), "表情系统暂未开放", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "请求出错", Toast.LENGTH_SHORT).show();
                    }
                });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest1);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
