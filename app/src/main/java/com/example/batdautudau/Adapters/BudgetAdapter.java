package com.example.batdautudau.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.batdautudau.Model.Budget;
import com.example.batdautudau.R;

import java.util.List;

public class BudgetAdapter extends RecyclerView.Adapter<BudgetAdapter.BudgetViewHolder> {

    private List<Budget> budgetList;

    public BudgetAdapter(List<Budget> budgetList) {
        this.budgetList = budgetList;
    }

    @Override
    public BudgetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_budget, parent, false);
        return new BudgetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BudgetViewHolder holder, int position) {
        Budget budget = budgetList.get(position);
        holder.categoryTextView.setText(budget.getCategory());
        holder.maxBudgetTextView.setText("Max Budget: " + budget.getMaxBudget());
        holder.startDateTextView.setText("Start Date: " + budget.getStartDate());
        holder.endDateTextView.setText("End Date: " + budget.getEndDate());
    }

    @Override
    public int getItemCount() {
        return budgetList.size();
    }

    public static class BudgetViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTextView, maxBudgetTextView, startDateTextView, endDateTextView;

        public BudgetViewHolder(View itemView) {
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.textViewCategory);
            maxBudgetTextView = itemView.findViewById(R.id.textViewMaxBudget);
            startDateTextView = itemView.findViewById(R.id.textViewStartDate);
            endDateTextView = itemView.findViewById(R.id.textViewEndDate);
        }
    }
}
