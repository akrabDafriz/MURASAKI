package com.arcfit.murasaki;

import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
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
import com.arcfit.murasaki.model.Workout;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import com.arcfit.murasaki.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgressInputActivity extends AppCompatActivity {
    private Spinner workoutTypeSpinner;
    private Spinner workoutCategorySpinner;
    private Spinner setsSpinner;
    private Button updateButton;
    private BaseApiService mApiService;
    private Context mContext;

    private Map<String, List<Workout>> workoutCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_progress_input);

        mContext = this;
        mApiService = UtilsApi.getApiService();

        if (LoginActivity.loggedAccount == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        bindUIComponents();

        // Inisialisasi data workout sebelum menggunakan setupCategorySpinner
        initializeWorkoutData();

        setupCategorySpinner();  // Sekarang workoutCategories sudah terisi
        setupSetsSpinner();

        ArrayAdapter<CharSequence> workoutAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.workout_types_array,
                android.R.layout.simple_spinner_item
        );
        workoutAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutTypeSpinner.setAdapter(workoutAdapter);

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

    private void setupCategorySpinner() {
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                new ArrayList<>(workoutCategories.keySet()) // Ambil daftar kategori
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutCategorySpinner.setAdapter(categoryAdapter);

        // Listener untuk kategori
        workoutCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = workoutCategorySpinner.getSelectedItem().toString();
                updateWorkoutTypeSpinner(selectedCategory);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initializeWorkoutData() {
        workoutCategories = new HashMap<>();

        List<Workout> sportsWorkouts = Arrays.asList(
                new Workout("Running", Arrays.asList("Leg Speed", "Heart Vitality")),
                new Workout("Sprinting", Arrays.asList("Leg Speed", "Foot Agility")),
                new Workout("Cycling", Arrays.asList("Leg Speed", "Heart Vitality")),
                new Workout("Swimming", Arrays.asList("Body Flexibility", "Heart Vitality")),
                new Workout("Ladder Drills", Arrays.asList("Leg Speed", "Foot Agility"))
        );
        workoutCategories.put("Sports", sportsWorkouts);

        // Upper Body category
        List<Workout> upperBodyWorkouts = Arrays.asList(
                new Workout("Push-ups", Arrays.asList("Arm Strength", "Chest Strength")),
                new Workout("Pull-ups", Arrays.asList("Arm Strength", "Back Strength")),
                new Workout("Bench Press", Arrays.asList("Chest Strength", "Arm Strength")),
                new Workout("Incline Bench Press", Arrays.asList("Chest Strength", "Arm Strength")),
                new Workout("Chest Fly", Arrays.asList("Chest Strength")),
                new Workout("Seated Row", Arrays.asList("Back Strength", "Core Stability")),
                new Workout("Face Pulls", Arrays.asList("Back Strength", "Arm Strength")),
                new Workout("Lat Pulldowns", Arrays.asList("Back Strength", "Arm Strength")),
                new Workout("Overhead Press", Arrays.asList("Arm Strength", "Core Stability")),
                new Workout("Bicep Curls", Arrays.asList("Arm Strength")),
                new Workout("Tricep Dips", Arrays.asList("Arm Strength", "Core Stability")),
                new Workout("Tricep Pulls", Arrays.asList("Arm Strength"))
        );
        workoutCategories.put("Upper Body", upperBodyWorkouts);

        List<Workout> lowerBodyWorkouts = Arrays.asList(
                new Workout("Jump Squats", Arrays.asList("Leg Speed", "Core Stability")),
                new Workout("Barbell Squats", Arrays.asList("Leg Speed", "Core Stability")),
                new Workout("Romanian Deadlifts", Arrays.asList("Back Strength", "Core Stability")),
                new Workout("Leg Press", Arrays.asList("Leg Speed")),
                new Workout("Calf Raises", Arrays.asList("Foot Agility", "Leg Speed")),
                new Workout("Lunges", Arrays.asList("Body Flexibility", "Heart Vitality")),
                new Workout("High Knees", Arrays.asList("Foot Agility", "Leg Speed"))
        );
        workoutCategories.put("Lower Body", lowerBodyWorkouts);

        List<Workout> cardioEnduranceWorkouts = Arrays.asList(
                new Workout("Treadmill Running", Arrays.asList("Leg Speed", "Heart Vitality")),
                new Workout("Elliptical Training", Arrays.asList("Heart Vitality", "Body Flexibility")),
                new Workout("Rowing Machine", Arrays.asList("Back Strength", "Heart Vitality")),
                new Workout("Battle Ropes", Arrays.asList("Arm Strength", "Heart Vitality")),
                new Workout("Burpees", Arrays.asList("Leg Speed", "Heart Vitality"))
        );
        workoutCategories.put("Cardio and Endurance", cardioEnduranceWorkouts);

        List<Workout> coreStabilityWorkouts = Arrays.asList(
                new Workout("Planks", Arrays.asList("Core Stability", "Body Flexibility")),
                new Workout("Side Planks", Arrays.asList("Core Stability", "Body Flexibility")),
                new Workout("Ab Wheel Rollouts", Arrays.asList("Core Stability")),
                new Workout("Russian Twists", Arrays.asList("Core Stability", "Body Flexibility")),
                new Workout("Cable Woodchoppers", Arrays.asList("Core Stability", "Body Flexibility")),
                new Workout("Hanging Leg Raises", Arrays.asList("Core Stability"))
        );
        workoutCategories.put("Core and Stability", coreStabilityWorkouts);

        List<Workout> functionalDynamicsWorkouts = Arrays.asList(
                new Workout("Deadlifts", Arrays.asList("Back Strength", "Core Stability")),
                new Workout("Kettlebell Swings", Arrays.asList("Core Stability", "Back Strength")),
                new Workout("Medicine Ball Slams", Arrays.asList("Core Stability", "Arm Strength")),
                new Workout("Farmers Walk", Arrays.asList("Core Stability", "Back Strength")),
                new Workout("Box Jumps", Arrays.asList("Leg Speed", "Core Stability"))
        );
        workoutCategories.put("Functional and Dynamics", functionalDynamicsWorkouts);

        List<Workout> flexibilityRecoveryWorkouts = Arrays.asList(
                new Workout("Yoga", Arrays.asList("Body Flexibility", "Core Stability")),
                new Workout("Foam Rolling", Arrays.asList("Body Flexibility")),
                new Workout("Dynamic Stretching", Arrays.asList("Body Flexibility", "Core Stability")),
                new Workout("Static Stretching", Arrays.asList("Body Flexibility"))
        );
        workoutCategories.put("Flexbibility and Recovery", flexibilityRecoveryWorkouts);
    }

    private void bindUIComponents() {
        workoutCategorySpinner = findViewById(R.id.workoutCategorySpinner);
        workoutTypeSpinner = findViewById(R.id.workoutTypeSpinner);
        setsSpinner = findViewById(R.id.setSpinner);
        updateButton = findViewById(R.id.updateButton);
    }



    private void setupSetsSpinner() {
        ArrayAdapter<CharSequence> setsAdapter = ArrayAdapter.createFromResource(
                this,
                R.array.sets_array,
                android.R.layout.simple_spinner_item
        );
        setsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        setsSpinner.setAdapter(setsAdapter);
    }

    private void updateWorkoutTypeSpinner(String category) {
        List<Workout> workouts = workoutCategories.get(category);
        if (workouts == null) return;

        List<String> workoutNames = new ArrayList<>();
        for (Workout workout : workouts) {
            workoutNames.add(workout.getName());
        }

        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                workoutNames
        );
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        workoutTypeSpinner.setAdapter(typeAdapter);
    }

    private void handleUpdate() {
        String userId = LoginActivity.loggedAccount.id;
        String selectedWorkout = workoutTypeSpinner.getSelectedItem().toString();
        Integer selectedSets = Integer.parseInt(setsSpinner.getSelectedItem().toString());
        System.out.println(userId);
        System.out.println(selectedWorkout);
        System.out.println(selectedSets);
        if (userId == null || userId.isEmpty() || selectedWorkout.isEmpty() || selectedSets <= 0) {
            Toast.makeText(mContext, "Invalid input. Please check your selections.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make the API call with the correct parameters
        mApiService.increaseStats(userId, selectedWorkout, selectedSets)
                .enqueue(new Callback<BaseResponse<Void>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Void>> call, Response<BaseResponse<Void>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(mContext, "Progress updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Void>> call, Throwable t) {
                        Toast.makeText(mContext, "Server issue: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(t.getMessage());
                    }
                });
    }

}