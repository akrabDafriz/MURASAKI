package com.arcfit.murasaki;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import android.widget.ImageButton;
import android.content.Intent;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.arcfit.murasaki.request.UtilsApi;
import com.arcfit.murasaki.request.RetrofitClient;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.model.Stats;
import com.arcfit.murasaki.LoginActivity;
// ...other necessary imports...

public class HomePage extends AppCompatActivity {
    private BaseApiService mApiService;
    private ProgressBar progressStrength;
    private ProgressBar progressAgility;
    private ProgressBar progressVitality;
    private ProgressBar progressFlexibility;
    private ProgressBar progressStability;
    private ImageButton btnInputProgress;
    private ImageButton btnHome;
    protected static Stats userStats;
    private Context mContext;
    private TextView tvUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        mContext = this;
        mApiService = UtilsApi.getApiService();
        // Initialize progress bars
        progressStrength = findViewById(R.id.progress_strength);
        progressAgility = findViewById(R.id.progress_agility);
        progressVitality = findViewById(R.id.progress_vitality);
        progressFlexibility = findViewById(R.id.progress_flexibility);
        progressStability = findViewById(R.id.progress_stability);
        ImageView avatarImage = findViewById(R.id.avatar_image);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gandalf_warrior) // Nama file GIF di drawable
                .into(avatarImage);
        btnInputProgress = findViewById(R.id.btn_input_progress);
        btnHome = findViewById(R.id.btn_home);
        tvUserName = findViewById(R.id.tv_user_name);

        // Set the username
        if (LoginActivity.loggedAccount != null && LoginActivity.loggedAccount.username != null) {
            tvUserName.setText(LoginActivity.loggedAccount.username);
        } else {
            tvUserName.setText("Guest");
        }

        btnInputProgress.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ProgressInputActivity.class);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Toast.makeText(HomePage.this, "You are already on the Home Page", Toast.LENGTH_SHORT).show();
        });
        // Fetch stats from backend
        getStatsFromServer();
    }

    private void getStatsFromServer() {
        //Retrofit retrofit = RetrofitClient.getClient(UtilsApi.BASE_URL_API);
        //BaseApiService apiService = retrofit.create(BaseApiService.class);

        // Get user ID from LoginActivity
        String userId = LoginActivity.loggedAccount.id;

        //Call<BaseResponse<Stats>> call = apiService.getStats(userId);
        mApiService.getStats(userId).enqueue(new Callback<BaseResponse<Stats>>() {
            @Override
            public void onResponse(Call<BaseResponse<Stats>> call, Response<BaseResponse<Stats>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userStats = response.body().getData();
                    updateProgressBars(userStats);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Stats>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProgressBars(Stats stats) {
        progressStrength.setProgress(userStats.Strength);
        progressAgility.setProgress(userStats.Agility);
        progressVitality.setProgress(userStats.Vitality);
        progressFlexibility.setProgress(userStats.Flexibility);
        progressStability.setProgress(userStats.Stability);
    }
}
