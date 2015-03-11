package com.dgsd.android.data.model;

import io.realm.RealmObject;

import static com.dgsd.android.data.model.ModelUtils.assertImport;
import static com.dgsd.android.data.model.ModelUtils.getInt;

public class DbStopTime extends RealmObject {

    private int tripId;

    private int stopId;

    private int secondsSinceMidnight;

    public DbStopTime() {

    }

    public DbStopTime(String[] csvImport) {
        assertImport(csvImport, 3);
        tripId = getInt(csvImport[0]);
        stopId = getInt(csvImport[1]);
        secondsSinceMidnight = getInt(csvImport[2]);
    }

    public int getTripId() {
        return tripId;
    }

    public void setTripId(int tripId) {
        this.tripId = tripId;
    }

    public int getStopId() {
        return stopId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public int getSecondsSinceMidnight() {
        return secondsSinceMidnight;
    }

    public void setSecondsSinceMidnight(int secondsSinceMidnight) {
        this.secondsSinceMidnight = secondsSinceMidnight;
    }
}
