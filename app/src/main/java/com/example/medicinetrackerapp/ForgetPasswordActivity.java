package com.example.medicinetrackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private EditText emailResetEditText;
    private Button resetPasswordButton;
    private TextView resetInstructionTextView;

    private FirebaseAuth mAuth;
    private Button forgetBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        mAuth = FirebaseAuth.getInstance();


        emailResetEditText = findViewById(R.id.email_reset_EditText);
        resetPasswordButton = findViewById(R.id.reset_password_button);
        resetInstructionTextView = findViewById(R.id.reset_instruction_textView);
        forgetBackButton = findViewById(R.id.forget_back_button);


        resetPasswordButton.setOnClickListener(view -> {
            String emailAddress = emailResetEditText.getText().toString();

            if (TextUtils.isEmpty(emailAddress)){
                emailResetEditText.setError("Email cannot be empty");
                emailResetEditText.requestFocus();
            } else {
                mAuth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(ForgetPasswordActivity.this, "Email sent successfully", Toast.LENGTH_SHORT).show();

                                resetInstructionTextView.setText(R.string.check_email);
                            }
                        });
            }


        });

        forgetBackButton.setOnClickListener(view -> startActivity(new Intent(ForgetPasswordActivity.this,LoginActivity.class)));







    }
}