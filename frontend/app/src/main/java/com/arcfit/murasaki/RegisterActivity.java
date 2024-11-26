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
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private BaseApiService mApiService;
    private Context mContext;
    private EditText username, email, password;
    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_register);

        mContext = this;
        mApiService = UtilsApi.getApiService();
        username = findViewById(R.id.edit_username);
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);
        registerBtn = findViewById(R.id.register_btn);

        registerBtn.setOnClickListener(v -> {
            handleRegister();
        });
    }

    protected void handleRegister() {
        String usernameS = username.getText().toString();
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        if (usernameS.isEmpty() || emailS.isEmpty() || passwordS.isEmpty() ) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.registerUser(usernameS, emailS, passwordS).enqueue(new Callback<BaseResponse<String>>() {
            @Override
            public void onResponse(Call<BaseResponse<String>> call, Response<BaseResponse<String>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<String> baseResponse = response.body();
                if (baseResponse != null && baseResponse.success) {
                    Toast.makeText(mContext, "Registration Successful", Toast.LENGTH_SHORT).show();
                    username.setText("");
                    email.setText("");
                    password.setText("");
                    moveActivity(mContext, LoginActivity.class);
                } else {
                    Toast.makeText(mContext, "Registration Failed: " + (baseResponse != null ? baseResponse.message : "Unknown error"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<String>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

}
