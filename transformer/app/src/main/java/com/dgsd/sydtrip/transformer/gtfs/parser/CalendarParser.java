package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendar;

import java.util.List;

public class CalendarParser extends CsvParser<GtfsCalendar> {

    private static final String KEY_SERVICE_ID = "service_id";
    private static final String KEY_MONDAY = "monday";
    private static final String KEY_TUESDAY = "tuesday";
    private static final String KEY_WEDNESDAY = "wednesday";
    private static final String KEY_THURSDAY = "thursday";
    private static final String KEY_FRIDAY = "friday";
    private static final String KEY_SATURDAY = "saturday";
    private static final String KEY_SUNDAY = "sunday";
    private static final String KEY_START_DATE = "start_date";
    private static final String KEY_END_DATE = "end_date";

    @Override
    protected GtfsCalendar create(List<String> fields, List<String> values) {
        final GtfsCalendar calendar = new GtfsCalendar();

        for (int i = 0, len = fields.size(); i < len; i++) {
            final String key = fields.get(i);
            final String value = values.get(i);

            switch (key) {
                case KEY_SERVICE_ID:
                    calendar.setServiceId(value);
                    break;
                case KEY_MONDAY:
                    calendar.setMonday(value);
                    break;
                case KEY_TUESDAY:
                    calendar.setTuesday(value);
                    break;
                case KEY_WEDNESDAY:
                    calendar.setWednesday(value);
                    break;
                case KEY_THURSDAY:
                    calendar.setThursday(value);
                    break;
                case KEY_FRIDAY:
                    calendar.setFriday(value);
                    break;
                case KEY_SATURDAY:
                    calendar.setSaturday(value);
                    break;
                case KEY_SUNDAY:
                    calendar.setSunday(value);
                    break;
                case KEY_START_DATE:
                    calendar.setStartDate(value);
                    break;
                case KEY_END_DATE:
                    calendar.setEndDate(value);
                    break;
            }
        }

        return calendar;
    }
}
