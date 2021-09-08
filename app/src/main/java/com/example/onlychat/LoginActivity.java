package com.example.onlychat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.onlychat.data.Data;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText editTextUser = findViewById(R.id.login_username);
        String username = editTextUser.getText().toString();

        EditText editTextPsw = findViewById(R.id.login_password);
        EditText editTextId = findViewById(R.id.login_ID);


        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = Data.urlLogin + "?Client_id=" + editTextUser.getText().toString() + "&Client_tag=" + editTextId.getText().toString() + "&client_pwd=" + editTextPsw.getText().toString();
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String token = response.optString("token");
                        String error = response.optString("error");
                        String expireIn = response.optString("expire_in");
                        String client_id = response.optString("client_id");
                        Log.i("Login",editTextUser.getText().toString());

                        switch (error) {
                            case "0":
                                Toast.makeText(getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("username",editTextUser.getText().toString());
                                bundle.putString("token",token);
                                intent.putExtras(bundle);
                                startActivity(intent);
                                finish();
                                break;
                            case "100":
                                Toast.makeText(getApplicationContext(), "未找到该学号", Toast.LENGTH_SHORT).show();
                                break;
                            case "101":
                                Toast.makeText(getApplicationContext(), "年度ID错误", Toast.LENGTH_SHORT).show();
                                break;
                            case "102":
                                Toast.makeText(getApplicationContext(), "密码错误", Toast.LENGTH_SHORT).show();
                                break;
                            case "103":
                                Toast.makeText(getApplicationContext(), "输入有空格", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "请求出错", Toast.LENGTH_SHORT).show();
                    }
                });
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }
        });
    }
}