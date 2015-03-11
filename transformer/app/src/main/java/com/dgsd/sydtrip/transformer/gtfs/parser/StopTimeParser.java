package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStopTime;

import java.util.List;

public class StopTimeParser extends CsvParser<GtfsStopTime> {

    private static final String KEY_TRIP_ID = "trip_id";
    private static final String KEY_ARRIVAL_TIME = "arrival_time";
    private static final String KEY_DEPARTURE_TIME = "departure_time";
    private static final String KEY_STOP_ID = "stop_id";
    private static final String KEY_STOP_SEQUENCE = "stop_sequence";
    private static final String KEY_STOP_HEADSIGN = "stop_headsign";
    private static final String KEY_PICKUP_TYPE = "pickup_type";
    private static final String KEY_DROPOFF_TYPE = "drop_off_type";
    private static final String KEY_SHAPE_DIST_TRAVELLED = "shape_dist_traveled";

    @Override
    protected GtfsStopTime create(List<String> fields, String... values) {
        final GtfsStopTime stopTime = new GtfsStopTime();

        for (int i = 0, len = fields.size(); i < len; i++) {
            final String key = fields.get(i).intern();
            final String value = values[i].intern();

            switch (key) {
                case KEY_TRIP_ID:
                    stopTime.setTripId(value);
                    break;
                case KEY_ARRIVAL_TIME:
                    stopTime.setArrivalTime(value);
                    break;
                case KEY_DEPARTURE_TIME:
                    stopTime.setDepartureTime(value);
                    break;
                case KEY_STOP_ID:
                    stopTime.setStopId(value);
                    break;
                case KEY_STOP_SEQUENCE:
                    stopTime.setStopSequence(value);
                    break;
                case KEY_STOP_HEADSIGN:
                    stopTime.setStopHeadsign(value);
                    break;
                case KEY_PICKUP_TYPE:
                    stopTime.setPickupType(value);
                    break;
                case KEY_DROPOFF_TYPE:
                    stopTime.setDropoffType(value);
                    break;
                case KEY_SHAPE_DIST_TRAVELLED:
                    stopTime.setShapeDistTravelled(value);
                    break;
            }
        }

        return stopTime;
    }
}
