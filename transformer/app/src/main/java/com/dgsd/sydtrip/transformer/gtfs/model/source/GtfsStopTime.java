package com.dgsd.sydtrip.transformer.gtfs.model.source;

public class GtfsStopTime extends BaseGtfsModel {

    private String tripId;

    private String arrivalTime;

    private String departureTime;

    private String stopId;

    private String stopSequence;

    private String stopHeadsign;

    private String pickupType;

    private String dropoffType;

    private String shapeDistTravelled;

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getStopId() {
        return stopId;
    }

    public void setStopId(String stopId) {
        this.stopId = stopId;
    }

    public String getStopSequence() {
        return stopSequence;
    }

    public void setStopSequence(String stopSequence) {
        this.stopSequence = stopSequence;
    }

    public String getStopHeadsign() {
        return stopHeadsign;
    }

    public void setStopHeadsign(String stopHeadsign) {
        this.stopHeadsign = stopHeadsign;
    }

    public String getPickupType() {
        return pickupType;
    }

    public void setPickupType(String pickupType) {
        this.pickupType = pickupType;
    }

    public String getDropoffType() {
        return dropoffType;
    }

    public void setDropoffType(String dropoffType) {
        this.dropoffType = dropoffType;
    }

    public String getShapeDistTravelled() {
        return shapeDistTravelled;
    }

    public void setShapeDistTravelled(String shapeDistTravelled) {
        this.shapeDistTravelled = shapeDistTravelled;
    }

    @Override
    public String toString() {
        return "StopTime{" +
                "tripId='" + tripId + '\'' +
                ", arrivalTime='" + arrivalTime + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", stopId='" + stopId + '\'' +
                ", stopSequence='" + stopSequence + '\'' +
                ", stopHeadsign='" + stopHeadsign + '\'' +
                ", pickupType='" + pickupType + '\'' +
                ", dropoffType='" + dropoffType + '\'' +
                ", shapeDistTravelled='" + shapeDistTravelled + '\'' +
                '}';
    }
}
