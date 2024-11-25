package com.arcfit.murasaki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.arcfit.murasaki.model.User;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.request.UtilsApi;


public class LoginActivity extends AppCompatActivity {
    public static User loggedAccount = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText username, password;
    private TextView registerNow = null;
    private Button login_btn = null;
    //private androidx.appcompat.widget.AppCompatButton registerbtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        mContext = this;
        mApiService = UtilsApi.getApiService();
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        registerNow = findViewById(R.id.register_txt);
        login_btn = findViewById(R.id.login_btn);
    }
}