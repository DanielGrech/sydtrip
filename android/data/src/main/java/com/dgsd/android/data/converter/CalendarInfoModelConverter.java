package com.dgsd.android.data.converter;

import com.dgsd.android.data.model.DbCalendarInfo;
import com.dgsd.sydtrip.model.CalendarInfo;

public class CalendarInfoModelConverter implements DbToModelConverter<DbCalendarInfo, CalendarInfo> {

    private static CalendarInfoModelConverter instance;

    public static CalendarInfoModelConverter getInstance() {
        if (instance == null) {
            instance = new CalendarInfoModelConverter();
        }
        return instance;
    }

    private CalendarInfoModelConverter() {

    }

    @Override
    public CalendarInfo convert(DbCalendarInfo dbCalInfo) {
        return new CalendarInfo(dbCalInfo.getTripId(),
                dbCalInfo.getStartDay(), dbCalInfo.getEndDay(), dbCalInfo.getAvailability());
    }
}
