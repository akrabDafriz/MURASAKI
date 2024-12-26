package com.arcfit.murasaki;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.arcfit.murasaki.model.BaseResponse;
import com.arcfit.murasaki.model.Plans;
import com.arcfit.murasaki.model.Workout;
import com.arcfit.murasaki.request.BaseApiService;
import com.arcfit.murasaki.request.UtilsApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlanInputActivity extends AppCompatActivity {
    private Spinner workoutTypeSpinner;
    private Spinner workoutCategorySpinner;
    private Spinner setsSpinner;
    private Button updateButton;
    private Button btnPlanInput;
    private ImageButton btnInputProgress;
    private ImageButton btnHome;
    private ImageButton btnStatDetail;
    private ImageButton btnPlan;
    private TextView quotes_text;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    private BaseApiService mApiService;
    private Context mContext;

    private Map<String, List<Workout>> workoutCategories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plan_input);

        initDatePicker();

        mContext = this;
        mApiService = UtilsApi.getApiService();
        btnInputProgress = findViewById(R.id.btn_input_progress);
        btnHome = findViewById(R.id.btn_home);
        btnStatDetail = findViewById(R.id.btn_stat_details);
        btnPlan = findViewById(R.id.btn_exercise_list);

        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getTodaysDate());

        if (LoginActivity.loggedAccount == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        String[] motivationalQuotes = {
                "Believe you can and you're halfway there.",
                "Every journey begins with a single step.",
                "Push harder than yesterday if you want a different tomorrow.",
                "The pain you feel today will be the strength you feel tomorrow.",
                "Success usually comes to those who are too busy to be looking for it.",
                "Wake up with determination. Go to bed with satisfaction.",
                "Don’t count the days, make the days count."
        };

        String randomQuote = motivationalQuotes[new Random().nextInt(motivationalQuotes.length)];

        TextView quoteTextView = findViewById(R.id.quotes_text);
        quoteTextView.setText(randomQuote);

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
            handleInput();
        });

        btnPlan.setOnClickListener(v -> {
            Intent intent = new Intent(PlanInputActivity.this, PlanActivity.class);
            startActivity(intent);
        });

        btnInputProgress.setOnClickListener(v -> {
            Intent intent = new Intent(PlanInputActivity.this, ProgressInputActivity.class);
            startActivity(intent);
        });

        btnStatDetail.setOnClickListener(v -> {
            Intent intent = new Intent(PlanInputActivity.this, AspectActivity.class);
            startActivity(intent);
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(PlanInputActivity.this, HomePage.class);
            startActivity(intent);
        });

    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(year, month, day);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month+1;
                String date = makeDateString(year, month, day);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String makeDateString(int year, int month, int day) {
        // Pastikan angka bulan dan hari terdiri dari dua digit
        String formattedMonth = month < 10 ? "0" + month : String.valueOf(month);
        String formattedDay = day < 10 ? "0" + day : String.valueOf(day);
        return year + "-" + formattedMonth + "-" + formattedDay;
    }


    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DES";

        return "JAN";
    }

    public void openDatePicker(View view) {
        datePickerDialog.show();
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

    private void handleInput() {
        String userId = LoginActivity.loggedAccount.id;
        String selectedWorkout = workoutTypeSpinner.getSelectedItem().toString();
        String deadline = dateButton.getText().toString();

        // Validasi input
        if (userId == null || userId.isEmpty() ||
                selectedWorkout.isEmpty() ||
                deadline == null || deadline.isEmpty()) {
            Toast.makeText(mContext, "Invalid input. Please check your selections.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Debugging untuk memastikan data
        System.out.println("User ID: " + userId);
        System.out.println("Workout: " + selectedWorkout);
        System.out.println("Deadline: " + deadline);

        // Panggil API untuk menyimpan plan
        mApiService.getPlans(userId, selectedWorkout, deadline)
                .enqueue(new Callback<BaseResponse<Plans>>() {
                    @Override
                    public void onResponse(Call<BaseResponse<Plans>> call, Response<BaseResponse<Plans>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(mContext, "Plan added successfully!", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse<Plans>> call, Throwable t) {
                        Toast.makeText(mContext, "Server issue: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        System.out.println(t.getMessage());
                    }
                });
    }




}