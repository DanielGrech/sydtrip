package com.dgsd.sydtrip.model;

public final class StopTime extends BaseModel {

    private final int stopId;

    private final int tripId;

    private final int secondsSinceMidnight;

    public StopTime(int stopId, int tripId, int secondsSinceMidnight) {
        this.stopId = stopId;
        this.tripId = tripId;
        this.secondsSinceMidnight = secondsSinceMidnight;
    }

    public int getStopId() {
        return stopId;
    }

    public int getTripId() {
        return tripId;
    }

    public int getSecondsSinceMidnight() {
        return secondsSinceMidnight;
    }

    @Override
     public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        final StopTime stopTime = (StopTime) o;
        return this.stopId == stopTime.stopId && this.tripId == stopTime.tripId
                && this.secondsSinceMidnight == stopTime.secondsSinceMidnight;
    }

    @Override
    public int hashCode() {
        int result = stopId;
        result = 31 * result + tripId;
        result = 31 * result + secondsSinceMidnight;
        return result;
    }
}
