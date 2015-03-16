package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStop;

import java.util.List;

public class StopParser extends CsvParser<GtfsStop> {

    private static final String KEY_STOP_ID = "stop_id";
    private static final String KEY_STOP_CODE = "stop_code";
    private static final String KEY_STOP_NAME = "stop_name";
    private static final String KEY_STOP_LAT = "stop_lat";
    private static final String KEY_STOP_LON = "stop_lon";
    private static final String KEY_LOCATION_TYPE = "location_type";
    private static final String KEY_PARENT_STATION = "parent_station";
    private static final String KEY_WHEELCHAIR_BOARDING = "wheelchair_boarding";
    private static final String KEY_PLATFORM_CODE = "platform_code";


    @Override
    protected GtfsStop create(List<String> fields, List<String> values) {
        final GtfsStop stop = new GtfsStop();

        for (int i = 0, len = fields.size(); i < len; i++) {
            final String key = fields.get(i);
            final String value = values.get(i);

            switch (key) {
                case KEY_STOP_ID:
                    stop.setId(value);
                    break;
                case KEY_STOP_CODE:
                    stop.setCode(value);
                    break;
                case KEY_STOP_NAME:
                    stop.setName(value);
                    break;
                case KEY_STOP_LAT:
                    stop.setLat(value);
                    break;
                case KEY_STOP_LON:
                    stop.setLng(value);
                    break;
                case KEY_LOCATION_TYPE:
                    stop.setLocationType(value);
                    break;
                case KEY_PARENT_STATION:
                    stop.setParentStation(value);
                    break;
                case KEY_WHEELCHAIR_BOARDING:
                    stop.setWheelchairBoarding(value);
                    break;
                case KEY_PLATFORM_CODE:
                    stop.setPlatformCode(value);
                    break;
            }
        }

        return stop;
    }
}
