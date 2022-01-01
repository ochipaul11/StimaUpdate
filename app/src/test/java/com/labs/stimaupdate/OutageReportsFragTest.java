package com.labs.stimaupdate;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class OutageReportsFragTest {


    @Test
    public void onCreateView() {
        Date date = Calendar.getInstance().getTime();
        String afterFormatDate = new SimpleDateFormat("dd-MM-yy").format(date);
    }
}