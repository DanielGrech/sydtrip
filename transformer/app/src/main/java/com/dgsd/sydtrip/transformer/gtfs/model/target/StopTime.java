package com.dgsd.sydtrip.transformer.gtfs.model.target;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStopTime;

public class StopTime {

    private final int stopId;

    private final int secondsSinceMidnight;

    public StopTime(GtfsStagingStopTime stopTime) {
        stopId = stopTime.getStopId();
        secondsSinceMidnight = stopTime.getArrivalTime();
    }

    public int getStopId() {
        return stopId;
    }

    public int getTime() {
        return secondsSinceMidnight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StopTime stopTime = (StopTime) o;

        if (secondsSinceMidnight != stopTime.secondsSinceMidnight) return false;
        if (stopId != stopTime.stopId) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = stopId;
        result = 31 * result + secondsSinceMidnight;
        return result;
    }

    @Override
    public String toString() {
        return "StopTime{" +
                "stopId=" + stopId +
                ", time=" + secondsSinceMidnight +
                '}';
    }
}
