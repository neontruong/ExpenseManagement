package com.example.batdautudau;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.batdautudau.Adapters.BudgetAdapter;
import com.example.batdautudau.Model.Budget;

import java.util.List;

public class ViewBudgetActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BudgetAdapter budgetAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_budget);

        recyclerView = findViewById(R.id.recyclerViewBudget);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        databaseHelper = new DatabaseHelper(this);

        // Fetching budget data from the database
        List<Budget> budgetList = databaseHelper.getAllBudgets();

        if (budgetList != null && !budgetList.isEmpty()) {
            // Setting up the adapter to display data
            budgetAdapter = new BudgetAdapter(budgetList);
            recyclerView.setAdapter(budgetAdapter);
        } else {
            Toast.makeText(this, "No budgets found", Toast.LENGTH_SHORT).show();
        }
    }
}
