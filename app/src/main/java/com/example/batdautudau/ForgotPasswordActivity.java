package com.example.batdautudau;

import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.batdautudau.DatabaseHelper;

public class ForgotPasswordActivity extends AppCompatActivity {
    private EditText etUsername, etNewPassword, etConfirmPassword, etSecurityAnswer;
    private Spinner spinnerSecurityQuestion;
    private Button btnResetPassword;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password_activity);

        etUsername = findViewById(R.id.et_username);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        etSecurityAnswer = findViewById(R.id.et_security_answer);
        spinnerSecurityQuestion = findViewById(R.id.spinner_security_question);
        btnResetPassword = findViewById(R.id.btn_reset_password);

        databaseHelper = new DatabaseHelper(this);

        // Set up spinner for security questions
        String[] questions = {"What is your pet's name?", "What is your mother's maiden name?", "What is your favorite color?"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSecurityQuestion.setAdapter(adapter);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString().trim();
                String newPassword = etNewPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                String securityAnswer = etSecurityAnswer.getText().toString().trim();
                String securityQuestion = spinnerSecurityQuestion.getSelectedItem().toString();

                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword) || TextUtils.isEmpty(securityAnswer)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(ForgotPasswordActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Kiểm tra người dùng có tồn tại trong CSDL không
                Cursor cursor = databaseHelper.getUser(username);
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    String storedSecurityQuestion = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_SECURITY_QUESTION));
                    String storedAnswer = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ANSWER));

                    // Kiểm tra câu hỏi bảo mật và câu trả lời có khớp không
                    if (storedSecurityQuestion.equals(securityQuestion) && storedAnswer.equals(securityAnswer)) {
                        boolean success = databaseHelper.updatePassword(username, newPassword);
                        if (success) {
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset successful", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Password reset failed", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, "Security question or answer is incorrect", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Username not found", Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });
    }
}
