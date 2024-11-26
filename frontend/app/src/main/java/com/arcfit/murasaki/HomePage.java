package com.arcfit.murasaki;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.content.Context;

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
    protected static Stats userStats;
    private Context mContext;

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

        // Fetch stats from backend
        getStatsFromServer();
    }

    private void getStatsFromServer() {
        //Retrofit retrofit = RetrofitClient.getClient(UtilsApi.BASE_URL_API);
        //BaseApiService apiService = retrofit.create(BaseApiService.class);

        // Get user ID from LoginActivity
        int userId = Integer.parseInt(LoginActivity.loggedAccount.id);

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
