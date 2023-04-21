package com.example.medicinetrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
    final String INDICATIONS_AND_USAGE = "indications_and_usage";
    final String RESULTS = "results", ERROR = "error", CODE = "code", MESSAGE = "message";

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
            getMedInfo(medName);
        }else{
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

    private void getMedInfo(final String medName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String jsonResult = Utils.getMedInfoFromApi(medName);
                if (jsonResult == null) {
                    handler.post(() -> tvMedDescription.setText(R.string.no_med_info));
                    return;
                }
                JSONObject jsonObject = new JSONObject(jsonResult);
                if (jsonObject.has(ERROR)) {
                    JSONObject errorObject = jsonObject.getJSONObject(ERROR);
                    String errorCode = errorObject.getString(CODE);
                    String errorMessage = errorObject.getString(MESSAGE);
                    Toast.makeText(MedicineInfoActivity.this, "error Message:" + errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                JSONArray resultsArray = jsonObject.getJSONArray(RESULTS);
                JSONObject firstResult = resultsArray.getJSONObject(0);
                String purpose = getString(R.string.purpose_not_available);

                if (firstResult.has(INDICATIONS_AND_USAGE)) {
                    JSONArray purposeArray = firstResult.getJSONArray(INDICATIONS_AND_USAGE);
                    purpose = purposeArray.getString(0);
                }

                String finalPurpose = purpose;
                handler.post(() -> tvMedDescription.setText(finalPurpose));

            } catch (IOException e) {
                e.printStackTrace();
                handler.post(() -> tvMedDescription.setText(R.string.no_med_info));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        });
    }






}