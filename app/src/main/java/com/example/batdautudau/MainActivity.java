package com.example.batdautudau;

import static com.example.batdautudau.function.formatCurrencyVND;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.batdautudau.Adapters.ExpencesAdapter;
import com.example.batdautudau.Model.objExpences;

import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;
    ListView listViewExpenses;
    Button buttonAddExpense, buttonOverview, buttonBudget, btnLogout, buttonRecurring;
    EditText searchExpenseEditText;
    TextView tvTitleMain;
    ArrayList<objExpences> _arrExpences;
    ExpencesAdapter adapterExpences;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        dbHelper = new DatabaseHelper(this);
        listViewExpenses = findViewById(R.id.listViewExpenses);
        buttonAddExpense = findViewById(R.id.buttonAddExpense);
        buttonOverview = findViewById(R.id.buttonOverview);
        buttonBudget = findViewById(R.id.buttonBudget);
        btnLogout = findViewById(R.id.btnLogout);
        searchExpenseEditText = findViewById(R.id.searchExpenseEditText);
        tvTitleMain = findViewById(R.id.tvTitleMain);
        buttonRecurring = findViewById(R.id.buttonRecurring);
        // Add Expense button click
        buttonAddExpense.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            startActivity(intent);
        });

        // Overview button click
        buttonOverview.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OverviewActivity.class);
            startActivity(intent);
        });

        // Budget button click
        buttonBudget.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddBudgetActivity.class);
            startActivity(intent);
        });
        buttonRecurring.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RecurringActivity.class);
            startActivity(intent);
        });

        // Logout button click
        btnLogout.setOnClickListener(v -> {
            Toast.makeText(MainActivity.this, "Logged out successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

        // Load expenses
        loadExpenses();

        // Context menu for ListView
        registerForContextMenu(listViewExpenses);

        // Search bar functionality
        searchExpenseEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterExpenses(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.item_menu, menu);

        menu.setHeaderTitle("Choose an action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int position = info.position;
        objExpences expense = _arrExpences.get(position);

        if (item.getTitle().equals("Edit")) {
            Intent intent = new Intent(MainActivity.this, AddExpenseActivity.class);
            intent.putExtra("expenses", expense.getId());
            intent.putExtra("expense_date", expense.getDate());
            intent.putExtra("expense_amount", expense.getAmount());
            intent.putExtra("expense_description", expense.getDescription());
            intent.putExtra("expense_category", expense.getCategory());
            startActivity(intent);
            return true;
        } else if (item.getTitle().equals("Delete")) {
            dbHelper.deleteExpense(expense.getId());
            _arrExpences.remove(position);
            adapterExpences.notifyDataSetChanged();
            Toast.makeText(this, "Deleted: " + expense.getDescription(), Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadExpenses(); // Refresh data when returning to activity
    }

    private void loadExpenses() {

        _arrExpences =dbHelper.getAllExpenses();
        if(_arrExpences.size()==0){
            dbHelper.createDATA();
            _arrExpences =dbHelper.getAllExpenses();
        }

        adapterExpences = new ExpencesAdapter(this, _arrExpences);
        listViewExpenses.setAdapter(adapterExpences);

        String total=dbHelper.getTotalExpensesAsJson();
        try {
            JSONObject jsonObject = new JSONObject(total);
            int totalIncome = jsonObject.optInt("totalIncome", 0);
            int totalExpense = jsonObject.optInt("totalExpense", 0);
            int recurring = jsonObject.optInt("recurring", 0);


            tvTitleMain.setText(formatCurrencyVND(totalIncome - totalExpense - recurring));


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void filterExpenses(String query) {
        ArrayList<objExpences> filteredList = new ArrayList<>();
        for (objExpences expense : _arrExpences) {
            if (expense.getDescription().toLowerCase().contains(query.toLowerCase()) ||
                    expense.getCategory().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(expense);
            }
        }

        adapterExpences = new ExpencesAdapter(this, filteredList);
        listViewExpenses.setAdapter(adapterExpences);
    }
}
