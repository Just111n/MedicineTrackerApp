package com.example.medicinetrackerapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText emailResetEditText;
    Button resetPasswordButton;
    TextView resetInstructionTextView;

    FirebaseAuth mAuth;
    Button forgetBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mAuth = FirebaseAuth.getInstance();


        emailResetEditText = findViewById(R.id.email_reset_EditText);
        resetPasswordButton = findViewById(R.id.reset_password_button);
        resetInstructionTextView = findViewById(R.id.reset_instruction_textView);
        forgetBackButton = findViewById(R.id.forget_back_button);


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = emailResetEditText.getText().toString();

                if (TextUtils.isEmpty(emailAddress)){
                    emailResetEditText.setError("Email cannot be empty");
                    emailResetEditText.requestFocus();
                } else {
                    mAuth.sendPasswordResetEmail(emailAddress)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgetPasswordActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();

                                        resetInstructionTextView.setText("Check email for link to reset the password");
                                    }
                                }
                            });
                }


            }
        });

        forgetBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class));
            }
        });







    }
}