package com.dgsd.android.data.model;

import io.realm.RealmObject;

import static com.dgsd.android.data.model.ModelUtils.assertImport;
import static com.dgsd.android.data.model.ModelUtils.getInt;

@SuppressWarnings("unused")
public class DbStopTime extends RealmObject {

    private int stopTimeId;

    private int stopId;

    private int secondsSinceMidnight;

    public DbStopTime() {

    }

    public DbStopTime(String[] csvImport) {
        assertImport(csvImport, 3);
        stopTimeId = getInt(csvImport[0]);
        stopId = getInt(csvImport[1]);
        secondsSinceMidnight = getInt(csvImport[2]);
    }

    public int getStopTimeId() {
        return stopTimeId;
    }

    public int getStopId() {
        return stopId;
    }

    public int getSecondsSinceMidnight() {
        return secondsSinceMidnight;
    }

    public void setStopTimeId(int stopTimeId) {
        this.stopTimeId = stopTimeId;
    }

    public void setStopId(int stopId) {
        this.stopId = stopId;
    }

    public void setSecondsSinceMidnight(int secondsSinceMidnight) {
        this.secondsSinceMidnight = secondsSinceMidnight;
    }
}
