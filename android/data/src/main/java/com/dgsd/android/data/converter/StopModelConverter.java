package com.dgsd.android.data.converter;

import com.dgsd.android.data.model.DbStop;
import com.dgsd.sydtrip.model.Stop;

public class StopModelConverter implements DbToModelConverter<DbStop, Stop> {

    private static StopModelConverter instance;


    public static StopModelConverter getInstance() {
        if (instance == null) {
            instance = new StopModelConverter();
        }
        return instance;
    }

    private StopModelConverter() {

    }

    @Override
    public Stop convert(DbStop dbStop) {
        // TODO: Difference between 'type' and 'stopType' ??
        return new Stop(dbStop.getId(),
                dbStop.getCode(), dbStop.getName(),
                dbStop.getLat(), dbStop.getLng(),
                dbStop.getPlatformCode(), dbStop.getParentId(),
                getStopType(dbStop));
    }

    private Stop.StopType getStopType(DbStop stop) {
        switch (stop.getType()) {
            case DbStop.TYPE_BUS:
                return Stop.StopType.BUS;
            case DbStop.TYPE_RAIL:
                return Stop.StopType.RAIL;
            case DbStop.TYPE_FERRY:
                return Stop.StopType.FERRY;
            case DbStop.TYPE_TRAM:
                return Stop.StopType.LIGHTRAIL;
        }
        return Stop.StopType.UNKNOWN;
    }
}
