package com.ovot.fiveStarapp_test1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    Context context;
    private ArrayList<JobLogs> MyList;

    public RecyclerViewAdapter(Context context, ArrayList<JobLogs> myList) {
        this.context = context;
        MyList = myList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        JobLogs jobLogs = MyList.get(position);
        holder.tvJobLogId.setText("Job Id : " + jobLogs.getJobLogID());
        holder.tvUpdateDate.setText(jobLogs.getUpdateDate());
        holder.tvStatusCode.setText(jobLogs.getStatusCode());
        holder.tvDescription.setText(jobLogs.getDescription());
    }

    @Override
    public int getItemCount() {
        return this.MyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvJobLogId, tvUpdateDate, tvStatusCode, tvDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvJobLogId = itemView.findViewById(R.id.TextViewJobLogId);
            tvUpdateDate = itemView.findViewById(R.id.TextViewUpdateDate);
            tvStatusCode = itemView.findViewById(R.id.TextViewStatusCode);
            tvDescription = itemView.findViewById(R.id.Description);
        }
    }
}
