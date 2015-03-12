package com.dgsd.android.data.converter;

import com.dgsd.android.data.model.DbStopTime;
import com.dgsd.sydtrip.model.StopTime;

public class StopTimeModelConverter implements DbToModelConverter<DbStopTime, StopTime> {

    private static StopTimeModelConverter instance;


    public static StopTimeModelConverter getInstance() {
        if (instance == null) {
            instance = new StopTimeModelConverter();
        }
        return instance;
    }

    private StopTimeModelConverter() {

    }

    @Override
    public StopTime convert(DbStopTime dbStopTime) {
        return new StopTime(dbStopTime.getStopId(), dbStopTime.getStopTimeId(),
                dbStopTime.getSecondsSinceMidnight());
    }
}
