package com.arcfit.murasaki;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;

import com.arcfit.murasaki.model.Aspects;

import android.widget.ImageButton;
import android.widget.TextView;

import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.request.UtilsApi;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.model.Stats;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AspectActivity extends AppCompatActivity {

    private BaseApiService mApiService;
    private ProgressBar progressArmStrentgh;
    private ProgressBar progressChestStrentgh;
    private ProgressBar progressBackStrentgh;
    private ProgressBar progressFootAgility;
    private ProgressBar progressLegSpeed;
    private ProgressBar progressHeartVitality;
    private ProgressBar progressBodyFlexibility;
    private ProgressBar progressCoreStability;
    protected static Stats userStats;
    private Context mContext;
    private TextView tvUserName;
    private ImageButton btnInputProgress;
    private ImageButton btnHome;
    private ImageButton btnStatDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspect);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        progressArmStrentgh = findViewById(R.id.progress_arm_strength);
        progressChestStrentgh = findViewById(R.id.progress_chest_strength);
        progressBackStrentgh = findViewById(R.id.progress_back_strength);
        progressFootAgility = findViewById(R.id.progress_foot_agility);
        progressLegSpeed = findViewById(R.id.progress_leg_speed);
        progressHeartVitality = findViewById(R.id.progress_heart_vitality);
        progressBodyFlexibility = findViewById(R.id.progress_body_flexibility);
        progressCoreStability = findViewById(R.id.progress_core_stability);

        btnInputProgress = findViewById(R.id.btn_input_progress);
        btnHome = findViewById(R.id.btn_home);
        btnStatDetail = findViewById(R.id.btn_stat_details);
        tvUserName = findViewById(R.id.tv_user_name);

        if (LoginActivity.loggedAccount != null && LoginActivity.loggedAccount.username != null) {
            tvUserName.setText(LoginActivity.loggedAccount.username);
        } else {
            tvUserName.setText("Guest");
        }

        btnInputProgress.setOnClickListener(v -> {
            Intent intent = new Intent(AspectActivity.this, ProgressInputActivity.class);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(AspectActivity.this, HomePage.class);
            startActivity(intent);
        });

        btnStatDetail.setOnClickListener(v -> {
            Toast.makeText(AspectActivity.this, "You are already on the Aspect Page", Toast.LENGTH_SHORT).show();
        });

        getAspectsFromServer();

    }

    private void getAspectsFromServer() {
        String userId = LoginActivity.loggedAccount.id;

        mApiService.getAspects(userId).enqueue(new Callback<BaseResponse<Aspects>>() {
            @Override
            public void onResponse(Call<BaseResponse<Aspects>> call, Response<BaseResponse<Aspects>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BaseResponse<Aspects> responseBody = response.body();

                    // Mengambil data menggunakan getData() karena Anda menggunakan field 'payload'
                    if (responseBody.getData() != null) {
                        Aspects aspects = responseBody.getData();
                        // Update UI dengan aspek yang diterima
                        updateProgressBars(aspects);
                    } else {
                        Toast.makeText(mContext, "Aspects data is null.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mContext, "Failed to fetch aspects.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Aspects>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server connection.", Toast.LENGTH_SHORT).show();
            }
        });
    }




    private void updateProgressBars(Aspects aspects) {
        if (aspects == null) {
            Toast.makeText(mContext, "Error: Aspects data is null.", Toast.LENGTH_SHORT).show();
            return;
        }

        progressArmStrentgh.setProgress(aspects.arm_strength);
        progressChestStrentgh.setProgress(aspects.chest_strength);
        progressBackStrentgh.setProgress(aspects.back_strength);
        progressFootAgility.setProgress(aspects.foot_agility);
        progressLegSpeed.setProgress(aspects.leg_speed);
        progressHeartVitality.setProgress(aspects.heart_vitality);
        progressBodyFlexibility.setProgress(aspects.body_flexibility);
        progressCoreStability .setProgress(aspects.core_stability);

        TextView armStrengthValue = findViewById(R.id.strength_value);
        TextView chestStrengthValue = findViewById(R.id.agility_value);
        TextView backStrengthValue = findViewById(R.id.vitality_value);
        TextView footAgilityValue = findViewById(R.id.flexibility_value);
        TextView legSpeedValue = findViewById(R.id.stability_value);
        TextView heartVitalityValue = findViewById(R.id.stability_value);
        TextView bodyFlexibilityValue = findViewById(R.id.stability_value);
        TextView coreStabilityValue = findViewById(R.id.stability_value);

        armStrengthValue.setText(String.valueOf(aspects.arm_strength));
        chestStrengthValue.setText(String.valueOf(aspects.chest_strength));
        backStrengthValue.setText(String.valueOf(aspects.back_strength));
        footAgilityValue.setText(String.valueOf(aspects.foot_agility));
        legSpeedValue.setText(String.valueOf(aspects.leg_speed));
        heartVitalityValue.setText(String.valueOf(aspects.heart_vitality));
        bodyFlexibilityValue.setText(String.valueOf(aspects.body_flexibility));
        coreStabilityValue.setText(String.valueOf(aspects.core_stability));
    }
}