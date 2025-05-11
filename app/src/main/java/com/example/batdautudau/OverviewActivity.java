package com.example.batdautudau;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.charts.PieChart;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.PieData;
//import com.github.mikephil.charting.data.PieDataSet;
//import com.github.mikephil.charting.data.PieEntry;
//import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class OverviewActivity extends AppCompatActivity {
    TextView tvTitle;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);
        dbHelper = new DatabaseHelper(this);

        tvTitle=(TextView)findViewById(R.id.tvTitle);
        String result=dbHelper.getTotalExpensesAsJson();
//        {
//            "totalIncome": 5000000,
//             "totalExpense": 3000000
//        }
        float incomePercentage = 0;
        float expensePercentage = 0;
        try {
            JSONObject jsonObject = new JSONObject(result);
            int totalIncome = jsonObject.optInt("totalIncome", 0);
            int totalExpense = jsonObject.optInt("totalExpense", 0);
            int recurring = jsonObject.optInt("recurring", 0);

            incomePercentage = (float)((totalIncome * 100f) / (totalIncome+totalExpense));
            expensePercentage =  (float)((totalExpense * 100f) / (totalIncome+totalExpense));

            tvTitle.setText(function.formatCurrencyVND(totalIncome-totalExpense-recurring));
        } catch (Exception e) {
            e.printStackTrace();
        }


        // Initialize the PieChart
        PieChart pieChart = findViewById(R.id.pieChart);


        // Data for the PieChart
        ArrayList<PieEntry> entriesPie = new ArrayList<>();
        entriesPie.add(new PieEntry(incomePercentage, "Thu")); // Replace 60f with actual data
        entriesPie.add(new PieEntry(expensePercentage, "Chi")); // Replace 40f with actual data

        // Create a PieDataSet
        PieDataSet dataSet = new PieDataSet(entriesPie, "Tỷ lệ Thu và Chi");
        dataSet.setColors(Color.GREEN, Color.RED); // Example colors for "Thu" and "Chi"
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setValueTextSize(14f);

        // Create PieData
        PieData pieData = new PieData(dataSet);

        // Configure the PieChart
        pieChart.setData(pieData);
        pieChart.setCenterText("Thu vs Chi");
        pieChart.setCenterTextSize(18f);
        pieChart.setEntryLabelTextSize(14f);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false); // Disable the description

        // Refresh the chart
        pieChart.invalidate();


        // Data for the chart (12 months)
//        ArrayList<BarEntry> entries = new ArrayList<>();
//        entries.add(new BarEntry(1, 5000)); // January
//        entries.add(new BarEntry(2, 4000)); // February
//        entries.add(new BarEntry(3, 4500)); // March
//        entries.add(new BarEntry(4, 3000)); // April
//        entries.add(new BarEntry(5, 5500)); // May
//        entries.add(new BarEntry(6, 2000)); // June
//        entries.add(new BarEntry(7, 3500)); // July
//        entries.add(new BarEntry(8, 6000)); // August
//        entries.add(new BarEntry(9, 7000)); // September
//        entries.add(new BarEntry(10, 3000)); // October
//        entries.add(new BarEntry(11, 8000)); // November
//        entries.add(new BarEntry(12, 4000)); // December
//
//        // Create BarDataSet
//        BarDataSet barDataSet = new BarDataSet(entries, "Monthly Expenses");
//        barDataSet.setColors(Color.BLUE); // Set bar color
//        barDataSet.setValueTextColor(Color.BLACK);
//        barDataSet.setValueTextSize(12f);
//
//        // Create BarData
//        BarData barData = new BarData(barDataSet);
//        barChart.setData(barData);
//
//        // Configure X-Axis
//        XAxis xAxis = barChart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularity(1f);
//        xAxis.setLabelCount(12);
//        xAxis.setValueFormatter(new ValueFormatter() {
//            @Override
//            public String getFormattedValue(float value) {
//                String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
//                        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
//                return months[(int) value - 1]; // Adjust for index starting at 0
//            }
//        });
//
//        // Configure chart appearance
//        barChart.getDescription().setEnabled(false); // Disable description
//        barChart.setFitBars(true); // Ensure bars fit within the chart
//        barChart.animateY(1000); // Add animation
//
//        // Refresh the chart
//        barChart.invalidate();

        // Initialize the BarChart
        BarChart barChart = findViewById(R.id.barChart);


        JSONArray jsonArray = dbHelper.getMonthlyTotalsAsJson();

        // Prepare data for the BarChart
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> months = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String month = jsonObject.getString("month");
                double totalThu = jsonObject.getDouble("totalThu");
                double totalChi = jsonObject.getDouble("totalChi");

                // Add data for a stacked bar (Thu and Chi in one bar)
                barEntries.add(new BarEntry(i, new float[]{(float) totalThu, (float) totalChi}));
                months.add(month); // Add month label
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.i("OverView",months.toString());
        Log.i("OverView",barEntries.toString());

        // Create a BarDataSet for the stacked bars
        BarDataSet barDataSet = new BarDataSet(barEntries, "Monthly Totals (Thu/Chi)");
        barDataSet.setColors(Color.GREEN, Color.RED); // Set colors for Thu and Chi
        barDataSet.setStackLabels(new String[]{"Thu", "Chi"}); // Set labels for the stacked values

        // Create BarData
        BarData barData = new BarData(barDataSet);
        barData.setBarWidth(0.7f); // Set bar width

        // Configure the BarChart
        barChart.setData(barData);
        barChart.setFitBars(true);
        barChart.getDescription().setEnabled(false); // Disable chart description
        barChart.animateY(1000); // Animate Y-axis

        // Configure the X-Axis
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months)); // Set month labels
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // Ensure intervals match entries
        xAxis.setGranularityEnabled(true);

        // Refresh the chart
        barChart.invalidate();

    }
}
