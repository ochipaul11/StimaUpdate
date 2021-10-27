package com.labs.stimaupdate;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class OutageReportsFrag extends Fragment {
    ListView lvReportsList;
    private OutageReportsListener outageReportsListener;

    public OutageReportsFrag() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_outage_reports, container, false);

/*        final Toolbar outageReportsToolbar = view.findViewById(R.id.toolbar_outage_reports);
        outageReportsToolbar.setTitle("Outage Reports");
        outageReportsToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        outageReportsToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                outageReportsListener.backFromOutageReportsFrag();
            }
        });

 */
        lvReportsList = view.findViewById(R.id.lvAllReports);
        lvReportsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ReportStatusFrag reportStatusFrag = new ReportStatusFrag();
                Bundle bundle = new Bundle();
                bundle.putInt("reportNumber", MainActivity.reportStatuses.get(i).getReportNumber());
                bundle.putString("address", MainActivity.reportStatuses.get(i).getAddress());
                bundle.putString("outageReceivedDate", MainActivity.reportStatuses.get(i).getReportReceivedDate());
                bundle.putInt("technicianOnSIte", MainActivity.reportStatuses.get(i).getTechnicianOnSite());
                bundle.putInt("restored", MainActivity.reportStatuses.get(i).getRestored());

                reportStatusFrag.setArguments(bundle);
                outageReportsListener.openReportStatusFrag(reportStatusFrag);


            }
        });
        outageReportsList();
        return view;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        outageReportsListener = (OutageReportsListener) activity;
    }

    private void outageReportsList() {
        String URL = "http://stimaupdate.site/getoutagereports.php";
        MainActivity.reportStatuses = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response != null) {
                    try {
                        JSONArray jsonArray = new JSONArray(response);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject data = jsonArray.getJSONObject(i);
                            MainActivity.reportStatuses.add(new ReportStatus(
                                    data.getInt("id"),
                                    data.getString("address"),
                                    data.getString("report_received_date"),
                                    data.getInt("technician_on_site"),
                                    data.getInt("restored")));

                        }
                        MainActivity.reportStatusAdapter = new ReportStatusAdapter(getActivity(), MainActivity.reportStatuses);
                        lvReportsList.setAdapter(MainActivity.reportStatusAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainActivity.prefConfig.displayToast(error.getMessage());
            }
        });
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    public interface OutageReportsListener {
        void openReportStatusFrag(ReportStatusFrag i);

        void backFromOutageReportsFrag();
    }
}
