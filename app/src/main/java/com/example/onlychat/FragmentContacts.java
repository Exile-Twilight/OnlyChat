package com.example.onlychat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.onlychat.adapter.ContactsAdapter;
import com.example.onlychat.data.Data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FragmentContacts extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.contacts_fragment,container,false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getActivity().getIntent().getExtras();
        String username = bundle.getString("username");
        String token = bundle.getString("token");
        RecyclerView recyclerView = (RecyclerView)getActivity().findViewById(R.id.contacts_recycler);

        //创建JSONObjectRequest对象
        String url = Data.urlGetContacts+"?Client_id="+username+"&Token="+token+"&type=all";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                String error = response.optString("error");

                switch (error) {
                    case "0":
                        List<ContactsAdapter.Contacts> contactsList = new ArrayList<>();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("userlist");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        for (int i=0;i<jsonArray.length();i++)
                        {
                            try {
                                JSONObject object = jsonArray.getJSONObject(i);
                                String name = object.optString("name");
                                String num = object.optString("client_id");
                                ContactsAdapter.Contacts contacts =new ContactsAdapter.Contacts(name,num);
                                contactsList.add(contacts);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                        Log.i("JSON",response.toString());
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        ContactsAdapter contactsAdapter = new ContactsAdapter(contactsList);
                        contactsAdapter.setOnItemClickListener(new ContactsAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(),ChatActivity.class);
                                Bundle bundle1 = new Bundle();
                                bundle1.putString("type","single");
                                bundle1.putString("token",token);
                                bundle1.putString("username",username);
                                bundle1.putString("chatid", contactsAdapter.getid(position));
                                intent.putExtras(bundle1);
                                startActivity(intent);
                            }
                        });
                        recyclerView.setAdapter(contactsAdapter);


                        break;
                    case "100":
                        Toast.makeText(getContext(), "必选参数为空", Toast.LENGTH_SHORT).show();
                        break;
                    case "101":
                        Toast.makeText(getContext(), "Token验证失败", Toast.LENGTH_SHORT).show();
                        break;
                    case "102":
                        Toast.makeText(getContext(), "Type学生类型错误", Toast.LENGTH_SHORT).show();
                        break;
                    case "103":
                        Toast.makeText(getContext(), "未查找到学生信息", Toast.LENGTH_SHORT).show();
                        break;
                    case "405":
                        Toast.makeText(getContext(), "未知错误", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "请求出错", Toast.LENGTH_SHORT).show();
            }
        });
        //加入到RequestQueue队列中，请求获取联系人列表
        MySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsonObjectRequest);


    }
}
