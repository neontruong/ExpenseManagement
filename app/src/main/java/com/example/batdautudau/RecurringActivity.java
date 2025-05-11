package com.example.batdautudau;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.batdautudau.Model.RecurringExpense;

import java.util.List;

public class RecurringActivity extends AppCompatActivity {
    private ListView listViewRecurringExpenses;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recurring);

        EditText editDesc = findViewById(R.id.etDescription);
        EditText editAmount = findViewById(R.id.etAmount);
        EditText editStart = findViewById(R.id.editStaartDate);
        EditText editEnd = findViewById(R.id.editEndDate);
        Button btnAddRecurringExpense = findViewById(R.id.btnAddRecurringExpense);
        listViewRecurringExpenses = findViewById(R.id.listRecurringExpenses);

        db = new DatabaseHelper(this);

        // Tải danh sách chi tiêu định kỳ ban đầu
        loadRecurringExpenses();

        // Xử lý sự kiện khi nhấn nút "Add Recurring Expense"
        btnAddRecurringExpense.setOnClickListener(v -> {
            RecurringExpense expense = new RecurringExpense();
            double amount = Double.parseDouble(editAmount.getText().toString());
            expense.setAmount(amount);
            expense.setDescription(editDesc.getText().toString());
            expense.setStartDate(editStart.getText().toString());
            expense.setEndDate(editEnd.getText().toString());
            expense.setCategory("");

            if (db.addRecurringExpense(expense)) {
                Toast.makeText(this, "Recurring Expense Added", Toast.LENGTH_SHORT).show();
                loadRecurringExpenses(); // Cập nhật danh sách sau khi thêm
            } else {
                Toast.makeText(this, "Error Adding Expense", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadRecurringExpenses() {
        // Lấy danh sách từ cơ sở dữ liệu
        List<RecurringExpense> recurringExpenses = db.getAllRecurringExpenses(); // Giả định bạn đã có phương thức này

        // Sử dụng ArrayAdapter để hiển thị danh sách
        ArrayAdapter<RecurringExpense> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1, // Layout hiển thị đơn giản
                recurringExpenses
        );

        listViewRecurringExpenses.setAdapter(adapter); // Đặt adapter cho ListView
    }

}
