package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStopTime;

import java.util.Comparator;

public class GtfsStagingStopTime extends BaseStagingModel<GtfsStopTime> {

    private final int tripId;

    private final int stopId;

    private final int arrivalTime;

    private final int departureTime;

    private final int sequence;

    private final String headSign;

    private final int pickupType;

    private final int dropoffType;

    private final float distTravelled;

    public GtfsStagingStopTime(GtfsStopTime gtfsObject) {
        super(gtfsObject);

        tripId = IdConverter.convertTripId(gtfsObject.getTripId());
        stopId = IdConverter.convertStopId(gtfsObject.getStopId());
        arrivalTime = parseTimeToSecondsOfDay(gtfsObject.getArrivalTime());
        departureTime = parseTimeToSecondsOfDay(gtfsObject.getDepartureTime());
        sequence = safeParseInt(gtfsObject.getStopSequence(), Integer.MAX_VALUE);
        headSign = gtfsObject.getStopHeadsign();
        pickupType = safeParseInt(gtfsObject.getPickupType(), 0);
        dropoffType = safeParseInt(gtfsObject.getDropoffType(), 0);
        distTravelled = safeParseFloat(gtfsObject.getShapeDistTravelled(), 0);
    }

    public int getTripId() {
        return tripId;
    }

    public int getStopId() {
        return stopId;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getDepartureTime() {
        return departureTime;
    }

    public int getSequence() {
        return sequence;
    }

    public String getHeadSign() {
        return headSign;
    }

    public int getPickupType() {
        return pickupType;
    }

    public int getDropoffType() {
        return dropoffType;
    }

    public float getDistTravelled() {
        return distTravelled;
    }

    @Override
    public String toString() {
        return "GtfsStagingStopTime{" +
                "tripId=" + tripId +
                ", stopId=" + stopId +
                ", arrivalTime=" + arrivalTime +
                ", departureTime=" + departureTime +
                ", sequence=" + sequence +
                ", headSign='" + headSign + '\'' +
                ", pickupType=" + pickupType +
                ", dropoffType=" + dropoffType +
                ", distTravelled=" + distTravelled +
                '}';
    }

    public static Comparator<GtfsStagingStopTime> SORT_BY_SEQUENCE
            = new Comparator<GtfsStagingStopTime>() {
        @Override
        public int compare(GtfsStagingStopTime lhs, GtfsStagingStopTime rhs) {
            return lhs.sequence - rhs.sequence;
        }
    };
}
