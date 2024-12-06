package com.arcfit.murasaki;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.model.User;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.request.UtilsApi;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    public static User loggedAccount = null;
    private BaseApiService mApiService;
    private Context mContext;
    private EditText email, password;
    private TextView registerNow = null;
    private Button login_btn = null;
    //private androidx.appcompat.widget.AppCompatButton registerbtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_login); // Explicitly call the method from AppCompatActivity
        //getSupportActionBar().hide();

        mContext = this;
        mApiService = UtilsApi.getApiService();
        email = findViewById(R.id.edit_email);
        password = findViewById(R.id.edit_password);
        registerNow = findViewById(R.id.register_txt);
        login_btn = findViewById(R.id.login_btn);

        login_btn.setOnClickListener(v -> {
            handleLogin();
        });
        registerNow.setOnClickListener(v -> {
            moveActivity(mContext, RegisterActivity.class);
        });

        password.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    handleLogin();
                    return true;
                }
                return false;
            }
        });
    }
    protected void handleLogin() {
        // handling empty field
        String emailS = email.getText().toString();
        String passwordS = password.getText().toString();

        if (emailS.isEmpty() || passwordS.isEmpty()) {
            Toast.makeText(mContext, "Field cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.loginUser(emailS, passwordS).enqueue(new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<User> baseResponse = response.body();
                    if (baseResponse != null && baseResponse.success) {
                        loggedAccount = baseResponse.payload;
                        Toast.makeText(mContext, "Login Successful", Toast.LENGTH_SHORT).show();
                        email.setText("");
                        password.setText("");
                        moveActivity(mContext, HomePage.class);
                    } else {
                        Toast.makeText(mContext, "Login Failed: " + (baseResponse != null ? baseResponse.message : "Unknown error"), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Handle non-2xx responses by parsing the error body
                    String errorMessage = parseErrorResponse(response);
                    Toast.makeText(mContext, "Login Failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String parseErrorResponse(Response<?> response) {
        try {
            // Assuming your backend sends JSON error response
            Gson gson = new Gson();
            BaseResponse<?> errorResponse = gson.fromJson(response.errorBody().string(), BaseResponse.class);
            return errorResponse != null ? errorResponse.message : "Unknown error";
        } catch (Exception e) {
            return "Failed to parse error response";
        }
    }


    private void moveActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        startActivity(intent);
    }

}