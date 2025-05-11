package com.example.batdautudau;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class AddBudgetActivity extends AppCompatActivity {

    private EditText editTextCategory, editTextMaxBudget, editTextStartDate, editTextEndDate;
    private Button buttonAddBudget, buttonViewBudget;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_budget);

        editTextCategory = findViewById(R.id.editTextCategory);
        editTextMaxBudget = findViewById(R.id.editTextMaxBudget);
        editTextStartDate = findViewById(R.id.editTextStartDate);
        editTextEndDate = findViewById(R.id.editTextEndDate);
        buttonAddBudget = findViewById(R.id.buttonAddBudget);
        buttonViewBudget = findViewById(R.id.buttonViewBudget);
        databaseHelper = new DatabaseHelper(this);

        editTextStartDate.setOnClickListener(v -> showDatePicker(editTextStartDate));
        editTextEndDate.setOnClickListener(v -> showDatePicker(editTextEndDate));
        buttonViewBudget.setOnClickListener(vv -> {
            startActivity(new Intent(AddBudgetActivity.this, ViewBudgetActivity.class));
            finish();
        });
        buttonAddBudget.setOnClickListener(v -> {
            String category = editTextCategory.getText().toString().trim();
            String maxBudgetStr = editTextMaxBudget.getText().toString().trim();
            String startDate = editTextStartDate.getText().toString().trim();
            String endDate = editTextEndDate.getText().toString().trim();

            if (category.isEmpty() || maxBudgetStr.isEmpty() || startDate.isEmpty() || endDate.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int maxBudget;
            try {
                maxBudget = Integer.parseInt(maxBudgetStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid budget amount", Toast.LENGTH_SHORT).show();
                return;
            }

            boolean isAdded = databaseHelper.addBudget(category, maxBudget, startDate, endDate);
            if (isAdded) {
                Toast.makeText(this, "Budget added successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(AddBudgetActivity.this, ViewBudgetActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Failed to add budget", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showDatePicker(EditText editText) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        new DatePickerDialog(this, (view, year1, month1, dayOfMonth) ->
                editText.setText(year1 + "-" + (month1 + 1) + "-" + dayOfMonth), year, month, day).show();
    }
}
