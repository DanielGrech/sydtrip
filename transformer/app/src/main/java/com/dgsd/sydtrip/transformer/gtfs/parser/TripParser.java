package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsTrip;

import java.util.List;

public class TripParser extends CsvParser<GtfsTrip> {

    private static final String KEY_ROUTE_ID = "route_id";
    private static final String KEY_SERVICE_ID = "service_id";
    private static final String KEY_TRIP_ID = "trip_id";
    private static final String KEY_SHAPE_ID = "shape_id";
    private static final String KEY_TRIP_HEADSIGN = "trip_headsign";
    private static final String KEY_DIRECTION_ID = "direction_id";
    private static final String KEY_BLOCK_ID = "block_id";
    private static final String KEY_WHEELCHAIR_ACCESSIBLE = "wheelchair_accessible";

    @Override
    protected GtfsTrip create(List<String> fields, String... values) {
        final GtfsTrip trip = new GtfsTrip();

        for (int i = 0, len = fields.size(); i < len; i++) {
            final String key = fields.get(i);
            final String value = values[i];

            switch (key) {
                case KEY_TRIP_ID:
                    trip.setId(value);
                    break;
                case KEY_ROUTE_ID:
                    trip.setRouteId(value);
                    break;
                case KEY_SERVICE_ID:
                    trip.setServiceId(value);
                    break;
                case KEY_SHAPE_ID:
                    trip.setShapeId(value);
                    break;
                case KEY_TRIP_HEADSIGN:
                    trip.setTripHeadsign(value);
                    break;
                case KEY_DIRECTION_ID:
                    trip.setDirectionId(value);
                    break;
                case KEY_BLOCK_ID:
                    trip.setBlockId(value);
                    break;
                case KEY_WHEELCHAIR_ACCESSIBLE:
                    trip.setWheelchairAccessible(value);
                    break;
            }
        }

        return trip;
    }
}
