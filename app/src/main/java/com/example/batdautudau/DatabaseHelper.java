package com.example.batdautudau;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.batdautudau.Model.Budget;
import com.example.batdautudau.Model.RecurringExpense;
import com.example.batdautudau.Model.objExpences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    // Database Name and Version
    private static final String DATABASE_NAME = "AppDatabase.db";
    private static final int DATABASE_VERSION = 4;

    // User Table
    public static final String TABLE_USERS = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_SECURITY_QUESTION = "security_question";
    public static final String COLUMN_ANSWER = "answer";

    // Expense Table
    public static final String TABLE_EXPENSES = "expenses";
    public static final String COLUMN_EXPENSE_ID = "id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_CATEGORY = "category";

    // Budget
    private static final String TABLE_BUDGET = "budget";
    private static final String COLUMN_BG_ID = "id";
    private static final String COLUMN_BG_CATEGORY = "category";
    private static final String COLUMN_MAX_BUDGET = "max_budget";
    private static final String COLUMN_START_DATE = "start_date";
    private static final String COLUMN_END_DATE = "end_date";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_USERNAME + " TEXT UNIQUE, " +
                COLUMN_PASSWORD + " TEXT, " +
                COLUMN_SECURITY_QUESTION + " TEXT, " +
                COLUMN_ANSWER + " TEXT)";
        db.execSQL(createTable);

        // Create Expenses Table
        String createExpenseTable = "CREATE TABLE " + TABLE_EXPENSES + " (" +
                COLUMN_EXPENSE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DATE + " TEXT NOT NULL, " +
                COLUMN_AMOUNT + " INTEGER NOT NULL, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_CATEGORY + " TEXT)";
        db.execSQL(createExpenseTable);

        // SQL câu lệnh tạo bảng ngân sách
        String CREATE_BUDGET_TABLE = "CREATE TABLE " + TABLE_BUDGET + " (" +
                COLUMN_BG_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_BG_CATEGORY + " TEXT, " +
                COLUMN_MAX_BUDGET + " INTEGER, " +
                COLUMN_START_DATE + " TEXT, " +
                COLUMN_END_DATE + " TEXT" +
                ");";

        db.execSQL(CREATE_BUDGET_TABLE);

        db.execSQL("CREATE TABLE recurring_expenses (" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    description TEXT NOT NULL," +
                "    amount REAL NOT NULL," +
                "    category TEXT NOT NULL," +
                "    start_date TEXT NOT NULL," +
                "    end_date TEXT" +
                ");");


    }




    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BUDGET);
        db.execSQL("DROP TABLE IF EXISTS recurring_expenses;");

        // Xóa bảng cũ nếu tồn tại
        onCreate(db);
    }

    // Insert a new user
    public boolean insertUser(String username, String password, String securityQuestion, String answer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);
        values.put(COLUMN_SECURITY_QUESTION, securityQuestion);
        values.put(COLUMN_ANSWER, answer);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }


    // Get user by username
    public Cursor getUser(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_USERS, null, COLUMN_USERNAME + " = ?", new String[]{username}, null, null, null);
    }

    // Check if the security answer is correct
    public boolean checkSecurityAnswer(String username, String answer) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_ANSWER + " = ?", new String[]{username, answer});
        boolean result = cursor.getCount() > 0;
        cursor.close();
        return result;
    }
    //Quen mat khau
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASSWORD, newPassword);

        int rowsUpdated = db.update(TABLE_USERS, values, COLUMN_USERNAME + " = ?", new String[]{username});
        return rowsUpdated > 0;  // Nếu cập nhật thành công, trả về true
    }

    // Add Expense
    public boolean addExpense(String date, int amount, String description, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_AMOUNT, amount);
        values.put(COLUMN_DESCRIPTION, description);
        values.put(COLUMN_CATEGORY, category);

        long result = db.insert(TABLE_EXPENSES, null, values);
        return result != -1;
    }


    // Kiểm tra xem category và description có khớp với ngân sách không
    public boolean checkCategoryAndDescription(String category, String description) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM Budget WHERE category = ? AND description = ?",
                new String[]{category, description}
        );

        boolean exists = false;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                exists = cursor.getInt(0) > 0; // Nếu có ít nhất một bản ghi, trả về true
            }
            cursor.close();
        }
        db.close();
        return exists;
    }


    // Lấy ngân sách tối đa dựa trên category và description
    public int getMaxBudgetForCategoryAndDescription(String category, String description) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT MAX(budget) FROM Budget WHERE category = ? AND description = ?",
                new String[]{category, description}
        );

        int maxBudget = 0;
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                maxBudget = cursor.getInt(0); // Lấy giá trị ngân sách lớn nhất
            }
            cursor.close();
        }
        db.close();
        return maxBudget;
    }
    public boolean isExpenseExceedingBudget(String description, int amount) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + COLUMN_MAX_BUDGET +
                " FROM " + TABLE_BUDGET +
                " WHERE " + COLUMN_BG_CATEGORY + " = ?";

        Cursor cursor = db.rawQuery(query, new String[]{description});
        boolean exceedsBudget = false;

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int maxBudget = cursor.getInt(0);
                if (amount > maxBudget) {
                    exceedsBudget = true;
                }
            }
            cursor.close();
        }
        db.close();
        return exceedsBudget;
    }







    // Update Expense
    public boolean updateExpense(objExpences obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, obj.getDate());
        values.put(COLUMN_AMOUNT, obj.getAmount());
        values.put(COLUMN_DESCRIPTION, obj.getDescription());
        values.put(COLUMN_CATEGORY, obj.getCategory());

        int result = db.update(TABLE_EXPENSES, values,
                COLUMN_EXPENSE_ID + "=?", new String[]{String.valueOf(obj.getId())});
        return result > 0;
    }

    // Delete Expense
    public boolean deleteExpense(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_EXPENSES,
                COLUMN_EXPENSE_ID + "=?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Get All Expenses
    public ArrayList<objExpences> getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<objExpences> arr = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(TABLE_EXPENSES,
                    null, null, null, null, null,
                    COLUMN_EXPENSE_ID + " DESC");

            if (cursor != null && cursor.moveToFirst()) { // Ensure cursor is not null and has data
                do {
                    objExpences obj = new objExpences();
                    obj.setId(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_EXPENSE_ID)));
                    obj.setAmount(cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)));
                    obj.setDate(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)));
                    obj.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)));
                    obj.setCategory(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)));
                    arr.add(obj);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {
                cursor.close(); // Always close the cursor to avoid memory leaks
            }
            db.close(); // Close database connection
        }

        return arr;
    }

    // Get Total Expenses as JSON
    public String getTotalExpensesAsJson() {
        SQLiteDatabase db = this.getReadableDatabase();
        JSONObject jsonResult = new JSONObject();
        Cursor cursor = null;

        String sql = "SELECT " +
                "(SELECT SUM(amount) FROM recurring_expenses  " +
                "WHERE (start_date <= '2024-11-01' AND (end_date IS NULL OR end_date >= '2024-11-30'))) AS recurring, " +
                "(SELECT SUM(amount) FROM expenses WHERE category = 'Thu') AS totalIncome, " +
                "(SELECT SUM(amount) FROM expenses WHERE category = 'Chi') AS totalExpense";

        try {
            cursor = db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                int totalIncome = cursor.getInt(cursor.getColumnIndexOrThrow("totalIncome"));
                int totalExpense = cursor.getInt(cursor.getColumnIndexOrThrow("totalExpense"));
                int recurring = cursor.getInt(cursor.getColumnIndexOrThrow("recurring"));
                // Add data to JSON object
                jsonResult.put("totalIncome", totalIncome);
                jsonResult.put("totalExpense", totalExpense);
                jsonResult.put("recurring", recurring);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                jsonResult.put("error", e.getMessage());
            } catch (Exception jsonException) {
                jsonException.printStackTrace();
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return jsonResult.toString();
    }

    public JSONArray getMonthlyTotalsAsJson() {
        SQLiteDatabase db = this.getReadableDatabase();
        JSONArray jsonArray = new JSONArray();

        // Truy vấn SQL tính tổng hàng tháng
        String sql = "SELECT " +
                "strftime('%m', date) AS month, " +
                "SUM(CASE WHEN category = 'Thu' THEN amount ELSE 0 END) AS totalThu, " +
                "SUM(CASE WHEN category = 'Chi' THEN amount ELSE 0 END) AS totalChi, " +
                "SUM(amount) AS total " +
                "FROM expenses " +
                "GROUP BY month " +
                "ORDER BY month";

        Cursor cursor = null;
        try {
            cursor = db.rawQuery(sql, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    JSONObject jsonObject = new JSONObject();

                    // Kiểm tra và ánh xạ cột 'month'
                    String month = cursor.getString(cursor.getColumnIndexOrThrow("month"));
                    if (month != null) {
                        jsonObject.put("month", month);
                    } else {
                        jsonObject.put("month", "N/A"); // Nếu không có giá trị, đặt mặc định là 'N/A'
                    }

                    double totalThu = cursor.getDouble(cursor.getColumnIndexOrThrow("totalThu"));
                    double totalChi = cursor.getDouble(cursor.getColumnIndexOrThrow("totalChi"));
                    double total = cursor.getDouble(cursor.getColumnIndexOrThrow("total"));

                    jsonObject.put("totalThu", totalThu);
                    jsonObject.put("totalChi", totalChi);
                    jsonObject.put("total", total);

                    Log.i("jsonObject", jsonObject.toString());
                    jsonArray.put(jsonObject);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return jsonArray;
    }

    // Create Sample Data
    public void createDATA() {
        String sql = "-- Insert 50 sample records\n" +
                "INSERT INTO expenses (date, amount, description, category)\n" +
                "VALUES\n" +
                "    ('2024-01-03', 150, 'Groceries', 'Chi'),\n" +
                "    ('2024-01-15', 2000, 'Salary', 'Thu'),\n" +
                "    ('2024-01-20', 500, 'Electricity Bill', 'Chi'),\n" +
                "    ('2024-01-25', 700, 'Freelance Payment', 'Thu'),\n" +
                "    ('2024-02-05', 300, 'Transport', 'Chi');"; // Add more records

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(sql);
        db.close();
    }

    public boolean addBudget(String category, int maxBudget, String startDate, String endDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COLUMN_BG_CATEGORY, category);
        contentValues.put(COLUMN_MAX_BUDGET, maxBudget);
        contentValues.put(COLUMN_START_DATE, startDate);
        contentValues.put(COLUMN_END_DATE, endDate);

        try {
            long result = db.insert(TABLE_BUDGET, null, contentValues);
            return result != -1; // Trả về true nếu thêm thành công
        } catch (Exception e) {
            e.printStackTrace();
            return false; // Trả về false nếu thất bại
        } finally {
            db.close(); // Đảm bảo đóng kết nối
        }
    }

    public List<Budget> getAllBudgets() {
        List<Budget> budgetList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BUDGET, null);

        if (cursor.moveToFirst()) {
            do {
                String category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BG_CATEGORY));
                int maxBudget = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_MAX_BUDGET));
                String startDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_START_DATE));
                String endDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_END_DATE));

                Budget budget = new Budget(category, maxBudget, startDate, endDate);
                budgetList.add(budget);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return budgetList;
    }

    public boolean addRecurringExpense(com.example.batdautudau.Model.RecurringExpense expense ) {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("description", expense.getDescription());
        values.put("amount", expense.getAmount());
        values.put("category", expense.getCategory());
        values.put("start_date", expense.getStartDate());
        values.put("end_date", expense.getEndDate());
        long result = db.insert("recurring_expenses", null, values);
        db.close();
        return result != -1; // Trả về true nếu thêm thành công
    }
    public List<RecurringExpense> getAllRecurringExpenses() {
        List<RecurringExpense> recurringExpenseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM recurring_expenses";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
                        double amount = cursor.getDouble(cursor.getColumnIndexOrThrow("amount"));
                        String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
                        String startDate = cursor.getString(cursor.getColumnIndexOrThrow("start_date"));
                        String endDate = cursor.getString(cursor.getColumnIndexOrThrow("end_date"));

                        RecurringExpense expense = new RecurringExpense(id, description, amount, category, startDate, endDate);
                        recurringExpenseList.add(expense);
                    } while (cursor.moveToNext());
                }
            } finally {
                cursor.close();
            }
        }
        db.close();

        return recurringExpenseList;
    }



}






