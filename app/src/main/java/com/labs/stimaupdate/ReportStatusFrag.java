package com.labs.stimaupdate;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.transferwise.sequencelayout.SequenceLayout;
import com.transferwise.sequencelayout.SequenceStep;


public class ReportStatusFrag extends Fragment {
    SequenceStep step1, step2, step3, step4;
    SequenceLayout sequenceLayout;

    public ReportStatusFrag() {
        // Required empty public constructor
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_report_status, container, false);

        Toolbar reportStatusToolbar = view.findViewById(R.id.toolbarReportStatus);
        ((AppCompatActivity) requireActivity()).setSupportActionBar(reportStatusToolbar);
        reportStatusToolbar.setNavigationIcon(R.drawable.ic_navigate_before_white_24dp);

        sequenceLayout = view.findViewById(R.id.sequenceLayout);
        sequenceLayout.setProgressForegroundColor(R.color.colorAccent);
        sequenceLayout.setProgressBackgroundColor(R.color.colorAccent);


        step1 = view.findViewById(R.id.step1);
        step2 = view.findViewById(R.id.step2);
        step3 = view.findViewById(R.id.step3);
        step4 = view.findViewById(R.id.step4);

        Bundle bundle = getArguments();
        String address = bundle.getString("address");
        int reportNumber = bundle.getInt("reportNumber");
        Boolean technicianOnSite = bundle.getBoolean("technicianOnSite");
        Boolean restored = bundle.getBoolean("restored");
        String createdDate = bundle.getString("created");
        String restoredDate = bundle.getString("restoredDate");
        //     Log.d("STATUS DATE:   ",createdDate);
        reportStatusToolbar.setTitle("Report status: #" + reportNumber);

        if (technicianOnSite & !restored) {
            step1.setAnchor(createdDate);
            step2.setAnchor(createdDate);
            step3.setActive(true);
            step3.setTitleTextAppearance(R.style.Base_TextAppearance_AppCompat_Title);

        } else if (restored) {
            step1.setAnchor(createdDate);
            step2.setAnchor(createdDate);
            step4.setActive(true);
            step4.setTitleTextAppearance(R.style.Base_TextAppearance_AppCompat_Title);
            step4.setAnchor(restoredDate);


        } else {
            step1.setAnchor(createdDate);
            step2.setAnchor(createdDate);
            step2.setActive(true);
            step2.setTitleTextAppearance(R.style.Base_TextAppearance_AppCompat_Title);
            step2.animate();
        }

        sequenceLayout = view.findViewById(R.id.sequenceLayout);
        sequenceLayout.setProgressForegroundColor(R.color.colorAccent);
        sequenceLayout.setProgressBackgroundColor(R.color.colorAccent);


        return view;
    }
}
