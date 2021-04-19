package com.android.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.testapp.adapter.DataListAdapter;
import com.android.testapp.model.Data;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView data_recycler_view;
    public List<Data> dataList;
    private DataListAdapter dataListAdapter;
    public static final String SHARED_PREF_FILE = "Data";
    public static final String KEY = "Data_Key";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EditText phone_edit_text = findViewById(R.id.phone);
        EditText email_edit_text = findViewById(R.id.email);
        Button save_button = findViewById(R.id.save);
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        String phoneNumberPattern = "^+[0-9]{10,13}$";
        dataList = loadData();
        setAdaptor();
        save_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean phone_status = true, email_status = true;
                String phone = phone_edit_text.getText().toString().trim();
                String email = email_edit_text.getText().toString().trim();
                if (!phone.matches(phoneNumberPattern) || !(phone.length()==10 || phone.length()==13)) {
                    phone_edit_text.setError("Enter valid phone no");
                    phone_status = false;
                }
                if (!email.matches(emailPattern) || !(email.length() > 0)) {
                    email_edit_text.setError("Enter valid email");
                    email_status = false;
                }
                if (phone_status && email_status){
                    if (searchInList(phone, email)) {
                        Toast.makeText(getApplicationContext(), "Data already exists", Toast.LENGTH_SHORT).show();
                    }else {
                        dataList.add(new Data(phone, email));
                        dataListAdapter.notifyItemInserted(dataList.size() - 1);
                        SharedPreferences preferences = getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        Gson gson = new Gson();
                        editor.putString(KEY, gson.toJson(dataList));
                        editor.apply();
                        Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void setAdaptor(){
        try {
            data_recycler_view = findViewById(R.id.data_view);
            data_recycler_view.setLayoutManager(new LinearLayoutManager(this));
            data_recycler_view.setItemAnimator(new DefaultItemAnimator());
            dataListAdapter = new DataListAdapter(dataList);
            data_recycler_view.setAdapter(dataListAdapter);
            dataListAdapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean searchInList(String mobile, String email){
        if (dataList.size() > 0){
            for (int i=0; i < dataList.size(); i++) {
                if (dataList.get(i).getPhone().equals(mobile) || dataList.get(i).getEmail().equals(email)){
                    return true;
                }
            }
        }
        return false;
    }

    private List<Data> loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY, null);
        Type type = new TypeToken<ArrayList<Data>>() {}.getType();
        dataList = gson.fromJson(json, type);
        if (dataList == null) {
            dataList = new ArrayList<Data>();
        }
        return dataList;
    }
}