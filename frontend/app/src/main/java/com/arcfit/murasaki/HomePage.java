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
    private ImageButton btnStatDetail;
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

        // Initialize text values
        TextView strengthValue = findViewById(R.id.strength_value);
        TextView agilityValue = findViewById(R.id.agility_value);
        TextView vitalityValue = findViewById(R.id.vitality_value);
        TextView flexibilityValue = findViewById(R.id.flexibility_value);
        TextView stabilityValue = findViewById(R.id.stability_value);

        // Initialize avatar
        ImageView avatarImage = findViewById(R.id.avatar_image);
        Glide.with(this)
                .asGif()
                .load(R.drawable.gandalf_warrior) // Replace with your actual GIF name
                .into(avatarImage);

        // Initialize buttons
        btnInputProgress = findViewById(R.id.btn_input_progress);
        btnHome = findViewById(R.id.btn_home);
        btnStatDetail = findViewById(R.id.btn_stat_details);
        tvUserName = findViewById(R.id.tv_user_name);

        // Set the username
        if (LoginActivity.loggedAccount != null && LoginActivity.loggedAccount.username != null) {
            tvUserName.setText(LoginActivity.loggedAccount.username);
        } else {
            tvUserName.setText("Guest");
        }

        // Button actions
        btnInputProgress.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, ProgressInputActivity.class);
            startActivity(intent);
        });

        btnStatDetail.setOnClickListener(v -> {
            Intent intent = new Intent(HomePage.this, AspectActivity.class);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Toast.makeText(HomePage.this, "You are already on the Home Page", Toast.LENGTH_SHORT).show();
        });

        // Fetch stats from backend
        getStatsFromServer();


    }


    private void getStatsFromServer() {
        String userId = LoginActivity.loggedAccount.id;

        mApiService.getStats(userId).enqueue(new Callback<BaseResponse<Stats>>() {
            @Override
            public void onResponse(Call<BaseResponse<Stats>> call, Response<BaseResponse<Stats>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    userStats = response.body().getData();  // Get stats object
                    updateProgressBars(userStats);  // Update progress bars and text values
                    Toast.makeText(mContext, "Successfully fetching stats from server.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Failed to fetch stats from server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Stats>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateProgressBars(Stats stats) {
        if (stats == null) {
            Toast.makeText(mContext, "Error: Stats data is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update progress bars
//        progressStrength.setProgress(stats.getStrength());
//        progressAgility.setProgress(stats.getAgility());
//        progressVitality.setProgress(stats.getVitality());
//        progressFlexibility.setProgress(stats.getFlexibility());
//        progressStability.setProgress(stats.getStability());
        progressStrength.setProgress(stats.strength);
        progressAgility.setProgress(stats.agility);
        progressVitality.setProgress(stats.vitality);
        progressFlexibility.setProgress(stats.flexibility);
        progressStability.setProgress(stats.stability);

        // Update text values next to the progress bars
        TextView strengthValue = findViewById(R.id.strength_value);
        TextView agilityValue = findViewById(R.id.agility_value);
        TextView vitalityValue = findViewById(R.id.vitality_value);
        TextView flexibilityValue = findViewById(R.id.flexibility_value);
        TextView stabilityValue = findViewById(R.id.stability_value);

        //strengthValue.setText("90");
//        strengthValue.setText(String.valueOf(stats.getStrength()));
//        agilityValue.setText(String.valueOf(stats.getAgility()));
//        vitalityValue.setText(String.valueOf(stats.getVitality()));
//        flexibilityValue.setText(String.valueOf(stats.getFlexibility()));
//        stabilityValue.setText(String.valueOf(stats.getStability()));

        strengthValue.setText(String.valueOf(stats.strength));
        agilityValue.setText(String.valueOf(stats.agility));
        vitalityValue.setText(String.valueOf(stats.vitality));
        flexibilityValue.setText(String.valueOf(stats.flexibility));
        stabilityValue.setText(String.valueOf(stats.stability));
    }


}
