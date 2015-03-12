package com.dgsd.android.data.converter;

import com.dgsd.android.data.model.DbTrip;
import com.dgsd.sydtrip.model.Trip;

public class TripModelConverter implements DbToModelConverter<DbTrip, Trip> {

    private static TripModelConverter instance;


    public static TripModelConverter getInstance() {
        if (instance == null) {
            instance = new TripModelConverter();
        }
        return instance;
    }

    private TripModelConverter() {

    }

    @Override
    public Trip convert(DbTrip dbTrip) {
        // TODO: Direction!
        return new Trip(dbTrip.getId(), dbTrip.getHeadSign(), null,
                dbTrip.getBlockId(), dbTrip.getWheelchairAccess() > 0, dbTrip.getRouteId(),
                dbTrip.getStopTimeId());
    }
}
