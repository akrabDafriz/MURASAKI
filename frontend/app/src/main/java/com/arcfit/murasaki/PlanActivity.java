package com.arcfit.murasaki;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.model.Plans;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.request.UtilsApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlanActivity extends AppCompatActivity {
    private Button btnPlanInput;
    private ImageButton btnInputProgress;
    private ImageButton btnHome;
    private ImageButton btnStatDetail;
    private ImageButton btnPlan;
    private RecyclerView planList;
    private TextView deadlineText;
    private RecyclerView planRecyclerView;
    private PlanAdapter planAdapter;
    private BaseApiService mApiService;
    private Context mContext;
    private List<Plans> plans = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan);
        mContext = this;
        mApiService = UtilsApi.getApiService();

        btnPlanInput = findViewById(R.id.addExercise_btn);
        btnPlan = findViewById(R.id.btn_exercise_list);
        btnInputProgress = findViewById(R.id.btn_input_progress);
        btnHome = findViewById(R.id.btn_home);
        btnStatDetail = findViewById(R.id.btn_stat_details);

        planRecyclerView = findViewById(R.id.plan_list);

        planRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        planAdapter = new PlanAdapter();
        planRecyclerView.setAdapter(planAdapter);

        getPlansFromServer();

        if (LoginActivity.loggedAccount == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        btnPlanInput.setOnClickListener(v -> {
            Intent intent = new Intent(PlanActivity.this, PlanInputActivity.class);
            startActivity(intent);
        });

        btnInputProgress.setOnClickListener(v -> {
            Intent intent = new Intent(PlanActivity.this, ProgressInputActivity.class);
            startActivity(intent);
        });

        btnStatDetail.setOnClickListener(v -> {
            Intent intent = new Intent(PlanActivity.this, AspectActivity.class);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(PlanActivity.this, HomePage.class);
            startActivity(intent);
        });

        btnPlan.setOnClickListener(v -> {
            Toast.makeText(PlanActivity.this, "You are already on the Plan Page", Toast.LENGTH_SHORT).show();
        });
    }

    private void getPlansFromServer() {
        String userId = LoginActivity.loggedAccount.id;

        if (userId == null || userId.isEmpty()) {
            Toast.makeText(mContext, "User ID is null or empty", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("DEBUG", "Fetching plans for user ID: " + userId);

        mApiService.getPlans(userId).enqueue(new Callback<BaseResponse<List<Plans>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Plans>>> call, Response<BaseResponse<List<Plans>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("DEBUG", "Response received: " + response.body());
                    List<Plans> plans = response.body().getData();
                    if (plans != null && !plans.isEmpty()) {
                        planAdapter.updatePlans(plans);
                    } else {
                        Toast.makeText(mContext, "No plans available", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Log.e("DEBUG", "Failed response: " + response.code() + ", " + response.message());
                    Toast.makeText(mContext, "Failed to fetch plans", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Plans>>> call, Throwable t) {
                Log.e("DEBUG", "Error: " + t.getMessage());
                Toast.makeText(mContext, "Server error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}