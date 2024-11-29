package com.arcfit.murasaki;

import android.os.Bundle;
import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.arcfit.murasaki.model.User;

public class ProgressInputActivity extends AppCompatActivity {
    private Spinner workoutTypeSpinner;
    private Spinner setsSpinner;
    private Button updateButton;
    private BaseApiService mApiService;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progress_input);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        // Check if loggedAccount is initialized
        if (LoginActivity.loggedAccount == null) {
            // Redirect to login or fetch user data
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Adjust the IDs to match those in activity_progress_input.xml
        workoutTypeSpinner = findViewById(R.id.workoutTypeSpinner);
        setsSpinner = findViewById(R.id.setSpinner);
        updateButton = findViewById(R.id.updateButton);

        // Set up workout type spinner
        ArrayAdapter<CharSequence> workoutAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.workout_types_array,
            android.R.layout.simple_spinner_item
        );
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutTypeSpinner.setAdapter(workoutAdapter);

        // Set up sets spinner
        ArrayAdapter<CharSequence> setsAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.sets_array,
            android.R.layout.simple_spinner_item
        );
        setsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setsSpinner.setAdapter(setsAdapter);

        updateButton.setOnClickListener(v -> {
            handleUpdate();
        });
    }

    private void handleUpdate() {
        String selectedWorkout = workoutTypeSpinner.getSelectedItem().toString();
        int selectedSets = Integer.parseInt(setsSpinner.getSelectedItem().toString());

        String aspectToChange = getAspectFromWorkout(selectedWorkout);

        // Ensure that user ID is not null
        String userId = LoginActivity.loggedAccount.id;
        if (userId == null) {
            Toast.makeText(mContext, "User ID is null", Toast.LENGTH_SHORT).show();
            return;
        }

        if (aspectToChange == null) {
            Toast.makeText(mContext, "Invalid workout type selected", Toast.LENGTH_SHORT).show();
            return;
        }

        mApiService.increaseStats(
            LoginActivity.loggedAccount.id,
            aspectToChange,
            selectedSets
        ).enqueue(new Callback<BaseResponse<Void>>() {
            @Override
            public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(mContext, "Application error " + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                BaseResponse<Void> baseResponse = response.body();
                if (baseResponse != null && baseResponse.success) {
                    Toast.makeText(mContext, "Stats updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "Update failed: " + (baseResponse != null ? baseResponse.message : "Unknown error"), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                Toast.makeText(mContext, "Problem with the server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAspectFromWorkout(String workoutType) {
        switch (workoutType) {
            case "Push-up":
                return "arm_strength";
            case "Pull-up":
                return "back_strength";
            case "Sit-up":
                return "core_stability";
            default:
                return null;
        }
    }
}