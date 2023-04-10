package com.example.medicinetrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MedicineInfoActivity extends AppCompatActivity {

    private TextView tvMedName, tvMedDescription;
    private FirebaseAuth mAuth;

    final String ERROR_NO_NETWORK = "No Network";
    final String ERROR_NOT_VALID = "Comic No Not Valid";
    final String ERROR_MALFORMED_URL = "Malformed URL";
    final String ERROR_BAD_JSON = "Bad JSON Response";
    final String ERROR_HTTPS_ERROR = "HTTPS Error";

    final String API_KEY = "aMchNi8ksgXCpONFYXBszSboK641yncpEWPA7v8g";
    String urlString = "https://api.fda.gov/drug/label.json?api_key="+API_KEY+"&search=openfda.generic_name:aspirin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_info);

        mAuth = FirebaseAuth.getInstance();


        tvMedName = findViewById(R.id.tv_drug_name);
        tvMedDescription = findViewById(R.id.med_description);
        String medName = getIntent().getStringExtra(ReminderEditorActivity.MED_NAME_KEY);

        tvMedName.setText(medName);
        if ( Utils.isNetworkAvailable(MedicineInfoActivity.this)){
            Log.d(Utils.UTILS_TAG,"if statemnt in onCLick is running");
            getMedInfo(medName);
        }else{
            Log.d(Utils.UTILS_TAG,"else statemnt in onCLick is running");
            Toast.makeText(MedicineInfoActivity.this, ERROR_NO_NETWORK,
                    Toast.LENGTH_LONG).show();
        }

    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MedicineInfoActivity.this, LoginActivity.class));
        }
    }

    final static class Container<T>{

        private T t;

        T get(){
            return t;
        }
        void set(T t ){
            this.t = t;
        }
    }

    private void getMedInfo(final String medName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String jsonResult = Utils.getMedInfoFromApi(medName);
                Log.d(Utils.UTILS_TAG, "Try is running");

                // Check if jsonResult is not null
                if (jsonResult != null) {
                    String purpose = "";
                    JSONObject jsonObject = new JSONObject(jsonResult);
                    if (jsonObject.has("error")) {
                        JSONObject errorObject = jsonObject.getJSONObject("error");
                        String errorCode = errorObject.getString("code");
                        String errorMessage = errorObject.getString("message");

                        // Handle the error case
                        System.out.println("Error Code: " + errorCode);
                        System.out.println("Error Message: " + errorMessage);

                    } else {
                        JSONArray resultsArray = jsonObject.getJSONArray("results");
                        JSONObject firstResult = resultsArray.getJSONObject(0);
                        JSONArray purposeArray = firstResult.getJSONArray("purpose");
                        purpose = purposeArray.getString(0);
                    }

                    // Update UI on the main thread
                    String finalPurpose = purpose;
                    handler.post(() -> {
                        // Update your TextViews or other UI components here
                        tvMedDescription.setText(finalPurpose);
                    });
                } else {
                    // Handle the case when jsonResult is null
                    handler.post(() -> tvMedDescription.setText(R.string.no_med_info));
                }

            } catch (IOException e) {
                e.printStackTrace();
                handler.post(() -> tvMedDescription.setText(R.string.no_med_info));
                // Handle the exception
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }





}