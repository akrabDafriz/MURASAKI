package com.arcfit.murasaki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.model.User;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


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
        //getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();
        username = findViewById(R.id.edit_username);
        password = findViewById(R.id.edit_password);
        registerNow = findViewById(R.id.register_txt);
        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(v -> {
            handleLogin();
        });
        registerNow.setOnClickListener(v -> {
            moveActivity(mContext, RegisterActivity.class);
        });
    }
    protected void handleLogin () {
        // handling empty field
        String usernameS = username.getText().toString();
        String passwordS = password.getText().toString();

        if (usernameS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        // Create User object
        User user = new User();
        user.username = usernameS;
        user.password = passwordS;

        mApiService.login(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                // handle the potential 4xx & 5xx error
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                // if success finish this activity (back to login activity)
                Toast.makeText(mContext, "Login Berhasil", Toast.LENGTH_SHORT).show();
                moveActivity(mContext, HomePage.class);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

}