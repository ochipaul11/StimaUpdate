package com.labs.stimaupdate;

import static org.junit.Assert.assertTrue;

import android.util.Log;

import com.backendless.Backendless;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import org.junit.Test;

import java.util.List;

public class ReportOutageFragTest {

    @Test
    public void reportOutage() {
        ReportOutageFrag reportOutageFrag = new ReportOutageFrag();
        final Boolean[] result = {true};
        final String[] accountNumber = {"60912324434"};
        String whereClause = "accountNumber = '" + accountNumber[0] + "'";
        DataQueryBuilder queryBuilder = DataQueryBuilder.create();
        queryBuilder.setWhereClause(whereClause);

        Backendless.Data.of(MeterAccount.class).find(queryBuilder, new AsyncCallback<List<MeterAccount>>() {
            @Override
            public void handleResponse(List<MeterAccount> response) {

                result[0] = response.isEmpty();
            }

            @Override
            public void handleFault(BackendlessFault fault) {
                Log.d("Error", fault.toString());
                result[0] = false;
            }
        });
        assertTrue(result[0]);
    }
}