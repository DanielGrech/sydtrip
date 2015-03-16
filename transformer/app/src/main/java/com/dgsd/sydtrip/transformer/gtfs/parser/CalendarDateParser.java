package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendarDate;

import java.util.List;

public class CalendarDateParser extends CsvParser<GtfsCalendarDate> {

    private static final String KEY_SERVICE_ID = "service_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_EXCEPTION_TYPE = "exception_type";

    @Override
    protected GtfsCalendarDate create(List<String> fields, List<String> values) {
        final GtfsCalendarDate calendarDate = new GtfsCalendarDate();

        for (int i = 0, len = fields.size(); i < len; i++) {
            final String key = fields.get(i);
            final String value = values.get(i);

            switch (key) {
                case KEY_SERVICE_ID:
                    calendarDate.setServiceId(value);
                    break;
                case KEY_DATE:
                    calendarDate.setDate(value);
                    break;
                case KEY_EXCEPTION_TYPE:
                    calendarDate.setExceptionType(value);
                    break;
            }
        }

        return calendarDate;
    }
}
