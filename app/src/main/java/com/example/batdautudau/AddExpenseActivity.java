package com.example.batdautudau;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.batdautudau.Model.objExpences;

public class AddExpenseActivity extends AppCompatActivity {
    private EditText editTextDate, editTextAmount, editTextDescription;
    private Spinner spinnerCategory;
    private Button buttonSaveExpense, buttonGoback;
    private DatabaseHelper dbHelper;
    int idExpences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        dbHelper = new DatabaseHelper(this);

        editTextDate = findViewById(R.id.editTextDate);
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDescription = findViewById(R.id.editTextDescription);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        buttonSaveExpense = findViewById(R.id.buttonSaveExpense);
        buttonGoback = findViewById(R.id.buttonGoback);

        String[] transactionTypes = {"Thu", "Chi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, transactionTypes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(adapter);

        Intent intent = getIntent();
        if (intent != null) {
            idExpences = intent.getIntExtra("expenses", -1);
            if (idExpences != -1) {
                String expenseDate = intent.getStringExtra("expense_date");
                int expenseAmount = intent.getIntExtra("expense_amount", 0);
                String expenseDescription = intent.getStringExtra("expense_description");
                String expenseCategory = intent.getStringExtra("expense_category");

                editTextDate.setText(expenseDate);
                editTextAmount.setText(String.valueOf(expenseAmount));
                editTextDescription.setText(expenseDescription);

                if (expenseCategory != null) {
                    int spinnerPosition = adapter.getPosition(expenseCategory);
                    spinnerCategory.setSelection(spinnerPosition);
                }
                buttonSaveExpense.setText("Update Expense");
            }
        } else {
            buttonSaveExpense.setText("Save Expense");
        }

        buttonSaveExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateInput = editTextDate.getText().toString().trim(); // Biến tạm thời
                final String date = dateInput.isEmpty() ?
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()) : dateInput;

                String description = editTextDescription.getText().toString().trim();
                String category = spinnerCategory.getSelectedItem().toString();

                String amountString = editTextAmount.getText().toString().trim();
                if (amountString.isEmpty()) {
                    Toast.makeText(AddExpenseActivity.this, "Please enter an amount", Toast.LENGTH_SHORT).show();
                    return;
                }
                int amount = Integer.parseInt(amountString);

                if (dbHelper.isExpenseExceedingBudget(description, amount)) {
                    new androidx.appcompat.app.AlertDialog.Builder(AddExpenseActivity.this)
                            .setTitle("Budget Exceeded")
                            .setMessage("Your expense for '" + description + "' exceeds the set budget.")
                            .setPositiveButton("OK", (dialog, which) -> {
                                // Thực hiện hành động thêm hoặc cập nhật chi tiêu khi nhấn OK
                                addOrUpdateExpense(date, amount, description, category);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> {
                                // Đóng dialog khi nhấn Hủy
                                dialog.dismiss();
                            })
                            .setCancelable(false)
                            .show();
                } else {
                    // Thêm chi tiêu trực tiếp nếu không vượt ngân sách
                    addOrUpdateExpense(date, amount, description, category);
                }
            }
        });



        buttonGoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Đóng Activity và quay lại
            }
        });
    }

    private void addOrUpdateExpense(String date, int amount, String description, String category) {
        if (buttonSaveExpense.getText().equals("Update Expense") && idExpences != 0) {
            objExpences obj = new objExpences();
            obj.setId(idExpences);
            obj.setDate(date);
            obj.setAmount(amount);
            obj.setDescription(description);
            obj.setCategory(category);
            if (dbHelper.updateExpense(obj)) {
                Toast.makeText(AddExpenseActivity.this, "Expense updated", Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity và quay lại
            } else {
                Toast.makeText(AddExpenseActivity.this, "Failed to update expense", Toast.LENGTH_SHORT).show();
            }
        } else {
            if (dbHelper.addExpense(date, amount, description, category)) {
                Toast.makeText(AddExpenseActivity.this, "Expense saved", Toast.LENGTH_SHORT).show();
                finish(); // Đóng Activity và quay lại
            } else {
                Toast.makeText(AddExpenseActivity.this, "Failed to save expense", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
