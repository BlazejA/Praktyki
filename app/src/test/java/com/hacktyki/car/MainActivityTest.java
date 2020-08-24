package com.hacktyki.car;

import org.junit.Assert;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivityTest {

    @Test
    public void loginSystem() {
    }

    @Test
    public void getYesterdayDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        Date now = new Date();
        String today = sdf.format(now);


        MainActivity mainActivity = new MainActivity();
        String yesterday = mainActivity.getYesterdayDate();


        Assert.assertNotEquals(yesterday,today);
    }

    @Test
    public void delOldReservation() {
    }
}