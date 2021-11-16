package com.labs.stimaupdate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ReportAdapter extends ArrayAdapter<Report> {
    public ReportAdapter(@NonNull Context context, List<Report> report) {
        super(context, 0, report);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.report_row_list, parent, false);
        }
        Report currentReport = getItem(position);
        TextView tvReportNumber, tvReportDate, tvReportStatus;

        tvReportNumber = listItemView.findViewById(R.id.tvReportNumber);
        tvReportDate = listItemView.findViewById(R.id.tvReportDate);
        tvReportStatus = listItemView.findViewById(R.id.tvStatus);

        tvReportDate.setText((CharSequence) currentReport.getRestoredDate());
        tvReportStatus.setText(String.valueOf(currentReport.isRestored()));
        tvReportNumber.setText(String.valueOf(currentReport.getId()));

        return listItemView;
    }
}
