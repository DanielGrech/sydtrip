package com.dgsd.android.data.model;

import io.realm.RealmObject;

import static com.dgsd.android.data.model.ModelUtils.assertImport;
import static com.dgsd.android.data.model.ModelUtils.getInt;

@SuppressWarnings("unused")
public class DbCalendarInfo extends RealmObject {

    private int tripId;

    private int startDay;

    private int endDay;

    private int availability;

    public DbCalendarInfo() {
    }

    public DbCalendarInfo(String[] csvImport) {
        assertImport(csvImport, 4);

        tripId = getInt(csvImport[0]);
        startDay = getInt(csvImport[1]);
        endDay = getInt(csvImport[2]);
        availability = getInt(csvImport[3]);
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getStartDay() {
        return startDay;
    }

    public void setStartDay(int startDay) {
        this.startDay = startDay;
    }

    public int getEndDay() {
        return endDay;
    }

    public void setEndDay(int endDay) {
        this.endDay = endDay;
    }

    public int getAvailability() {
        return availability;
    }

    public void setAvailability(int availability) {
        this.availability = availability;
    }
}
