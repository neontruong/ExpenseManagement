package com.example.batdautudau;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private EditText editUsername, editPassword, editSecurityAnswer;
    private Spinner spnSecurityQuestion;
    private Button btnRegister;
    private DatabaseHelper databaseHelper;
    private String selectedQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        editSecurityAnswer = findViewById(R.id.editSecurityAnswer);
        spnSecurityQuestion = findViewById(R.id.spnSecurityQuestion);
        btnRegister = findViewById(R.id.btnRegister);

        databaseHelper = new DatabaseHelper(this);

        // Set up spinner for security questions
        String[] questions = {"What is your pet's name?", "What is your mother's maiden name?", "What is your favorite color?"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questions);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnSecurityQuestion.setAdapter(adapter);

        spnSecurityQuestion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedQuestion = questions[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedQuestion = questions[0];
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();
                String securityAnswer = editSecurityAnswer.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty() || securityAnswer.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    boolean success = databaseHelper.insertUser(username, password, selectedQuestion, securityAnswer);
                    if (success) {
                        Toast.makeText(RegisterActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}
