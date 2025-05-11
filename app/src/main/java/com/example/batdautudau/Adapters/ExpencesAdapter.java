package com.example.batdautudau.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.batdautudau.Model.objExpences;
import com.example.batdautudau.R;
import com.example.batdautudau.function;

import java.util.ArrayList;

public class ExpencesAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<objExpences> expenses;

    public ExpencesAdapter(Context context, ArrayList<objExpences> expenses) {
        this.context = context;
        this.expenses = expenses;
    }

    @Override
    public int getCount() {
        return expenses.size();
    }

    @Override
    public Object getItem(int position) {
        return expenses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_expense, parent, false);
        }

        // Lấy đối tượng hiện tại
        objExpences expense = expenses.get(position);

        // Gán dữ liệu cho các TextView
        TextView textDate = (TextView) convertView.findViewById(R.id.textDate);
        TextView textAmount =(TextView) convertView.findViewById(R.id.textAmount);
        TextView textDescription =(TextView) convertView.findViewById(R.id.textDescription);


        textDate.setText(expense.getDate());
        textAmount.setText(function.formatCurrency(expense.getAmount()));
        textDescription.setText(expense.getDescription());
        if(expense.getCategory().equals("Thu")){
            textAmount.setBackgroundColor(Color.GREEN);
        }else{
            textAmount.setBackgroundColor(Color.RED);
        }

        return convertView;
    }

}
