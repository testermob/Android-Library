package com.testermob.testermoblib;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.InputType;
import android.util.Log;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserActivityTracker {
    private static final String BASE_URL = "https://dashboard.testermob.com/api/";
    private ApiService apiService;
    private Context context;
    private SharedPreferences preferences;

    public UserActivityTracker(Context context, String appId) {
        this.context = context;
        this.preferences = context.getSharedPreferences("user_prefs_" + context.getPackageName(), Context.MODE_PRIVATE);

        // Check if the email is already saved
        if (getGmail().isEmpty()) {
            showEmailDialog(appId); // Show dialog if email is not present
        } else {
            // Continue with tracking user activity
            trackUserActivity();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public void sendUserActivity(String appId) {
        UserActivity activity = new UserActivity(appId);
        activity.setPakageName(context.getPackageName());
        activity.setGmail(getGmail());
        activity.setDays(getCurrentDay());
        activity.setHours(getTotalHours());
        activity.setMinutes(calculateAI(false));
        activity.setSeconds(getTotalSeconds());
        activity.setInstalled(isAppInstalled());
        activity.setActivity(getActivitiesAsString());
        activity.setClicks(getTesterMob()); //Saba
        activity.setOsVersion(Build.VERSION.RELEASE);
        activity.setOsName("android");
        activity.setAppVersion(getVersionCodeAsString(context));

        Call<Void> call = apiService.sendActivity(activity);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.i("UserActivityTracker", "Data sent successfully"+ response.message());

                } else {
                    String errorBody = "";
                    try {
                        errorBody = response.errorBody().string();
                    } catch (Exception e) {
                        errorBody = "Unable to parse error body";
                    }
                    Log.e("UserActivityTracker", "Error sending data: " + response.message() + ", Code: " + response.code() + ", Body: " + errorBody);

                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("UserActivityTracker", "Failed to send data: " + t.getMessage());

            }
        });
    }

    private void showEmailDialog(String appId) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Welcome as a Tester");
        builder.setMessage("Please enter the email you used to sign up on the website, which is also registered with the Android operating system on your mobile device.");

        final EditText emailInput = new EditText(context);
        emailInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        builder.setView(emailInput);

        builder.setPositiveButton("Confirm", null); // Temporarily set the positive button

        // Create and show the dialog
        final AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // Prevent closing the dialog
        dialog.show();

        // Set the positive button action to validate email
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String email = emailInput.getText().toString().trim();
            if (isValidEmail(email)) {
                // Save the email in SharedPreferences

                preferences.edit().putString("user_gmail", email).apply();
                sendUserActivity(appId);
                // Continue with tracking user activity
                trackUserActivity();
                dialog.dismiss(); // Close the dialog
            } else {
                emailInput.setError("Please enter a valid email.");
            }
        });
    }

    private boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches(); // Validate email
    }

    private void trackUserActivity() {
        boolean hasShownToast = preferences.getBoolean("has_shown_toast", true);

        if (hasShownToast) {
            Toast.makeText(context, "Congratulations! Work as tester for 15 days and earn dollars.", Toast.LENGTH_LONG).show();
            preferences.edit().putBoolean("has_shown_toast", false).apply();
        } else {
            Toast.makeText(context, "You're work as a tester started", Toast.LENGTH_SHORT).show();
        }
    }

    private String getGmail() {
        return preferences.getString("user_gmail", "");
    }

    private String getCurrentDay() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.getDefault());
        return sdf.format(new Date());
    }

    private boolean isAppInstalled() { return true; }

    public String getVersionCodeAsString(Context context) {
        String versionCodeStr = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            // API Level 28
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                versionCodeStr = String.valueOf(packageInfo.getLongVersionCode());
            } else {
                versionCodeStr = String.valueOf(packageInfo.versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCodeStr;
    }

    private String getTotalHours() {

        long totalHours = preferences.getLong("total_hours", 0);

        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - preferences.getLong("last_open_time", currentTime);

        if (elapsedTime > 0) {
            totalHours += elapsedTime / (1000 * 60 * 60);
        }

        preferences.edit().putLong("last_open_time", currentTime).apply();
        preferences.edit().putLong("total_hours", totalHours).apply();

        return String.valueOf(totalHours);
    }

    public String calculateAI(boolean isStart) {

        long currentTime = System.currentTimeMillis();
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(currentTime));

        String savedDate = preferences.getString("saved_date", currentDate);

        if (isStart) {

            preferences.edit().putLong("start_time", currentTime).apply();
            preferences.edit().putString("saved_date", currentDate).apply();
            return "Your work started";
        } else {

            if (!currentDate.equals(savedDate)) {
                preferences.edit().putLong("total_time", 0).apply();
                preferences.edit().putString("saved_date", currentDate).apply();
            }

            long startTime = preferences.getLong("start_time", currentTime);
            long elapsedTime = currentTime - startTime;

            long totalTime = preferences.getLong("total_time", 0);
            totalTime += elapsedTime;

            preferences.edit().putLong("total_time", totalTime).apply();

            long seconds = totalTime / 1000;
            long hours = seconds / 3600;
            seconds %= 3600;
            long minutes = seconds / 60;
            seconds %= 60;

            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    private String getTotalSeconds() {

        long currentTime = System.currentTimeMillis();

        long startTime = preferences.getLong("start_time", currentTime);

        long elapsedTime = currentTime - startTime;

        long secondsElapsed = elapsedTime / 1000;

        return String.valueOf(secondsElapsed);
    }

    public String getActivitiesAsString() {

        String lastGmail = preferences.getString("last_gmail", null);
        if (lastGmail == null || !lastGmail.equals(getGmail())) {
            preferences.edit().putInt("main_activity_count", 0).apply();
            preferences.edit().putString("last_gmail", getGmail()).apply();
        }

        String lastDate = preferences.getString("last_date", null);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        if (lastDate == null || !lastDate.equals(currentDate)) {
            preferences.edit().putInt("main_activity_count", 0).apply();
            preferences.edit().putString("last_date", currentDate).apply();
        }

        int mainActivityCount = preferences.getInt("main_activity_count", 0);
        mainActivityCount++;
        preferences.edit().putInt("main_activity_count", mainActivityCount).apply();

        String activitiesString = "MainActivity: " + mainActivityCount;

        // activitiesString += ", OtherActivity: " + otherActivityCount;

        return String.valueOf(activitiesString);
    }

    public String getTesterMob() {

        int totalClicks = preferences.getInt("total_clicks", 0);

        totalClicks++;

        preferences.edit().putInt("total_clicks", totalClicks).apply();

        return String.valueOf(totalClicks);
    }


}
