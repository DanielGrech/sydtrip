package com.dgsd.android.data.model;

import io.realm.RealmObject;

import static com.dgsd.android.data.model.ModelUtils.assertImport;
import static com.dgsd.android.data.model.ModelUtils.getInt;

public class DbCalendarInfoEx extends RealmObject {

    private int tripId;

    private int julianDay;

    private int exceptionType;

    public DbCalendarInfoEx() {
    }

    public DbCalendarInfoEx(String[] csvImport) {
        assertImport(csvImport, 3);

        tripId = getInt(csvImport[0]);
        julianDay = getInt(csvImport[1]);
        exceptionType = getInt(csvImport[2]);
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getJulianDay() {
        return julianDay;
    }

    public void setJulianDay(int julianDay) {
        this.julianDay = julianDay;
    }

    public int getExceptionType() {
        return exceptionType;
    }

    public void setExceptionType(int exceptionType) {
        this.exceptionType = exceptionType;
    }
}
