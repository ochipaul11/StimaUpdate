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

public class ReportStatusAdapter extends ArrayAdapter<ReportStatus> {

    public ReportStatusAdapter(@NonNull Context context, List<ReportStatus> reportStatuses) {
        super(context, 0, reportStatuses);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext())
                    .inflate(R.layout.report_row_list, parent, false);
        }
        ReportStatus currentReport = getItem(position);
        TextView tvReportNumber, tvReportDate, tvReportStatus;
        tvReportNumber = listItemView.findViewById(R.id.tvReportNumber);
        tvReportDate = listItemView.findViewById(R.id.tvReportDate);
        tvReportStatus = listItemView.findViewById(R.id.tvStatus);

       // tvReportNumber.setText(currentReport.getId());
        tvReportDate.setText(currentReport.getReportReceivedDate());
        //tvReportStatus.setText(currentReport.);

        return listItemView;
    }
}
