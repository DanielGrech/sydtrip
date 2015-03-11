package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendarDate;

import java.util.Comparator;

public class GtfsStagingCalendarDate extends BaseStagingModel<GtfsCalendarDate> {

    private final int serviceId;

    private final int julianDay;

    private final int exceptionType;

    public GtfsStagingCalendarDate(GtfsCalendarDate gtfsObject) {
        super(gtfsObject);

        serviceId = IdConverter.convertServiceId(gtfsObject.getServiceId());
        julianDay = parseDateToJulianDay(gtfsObject.getDate());
        exceptionType = safeParseInt(gtfsObject.getExceptionType(), Integer.MAX_VALUE);
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getJulianDay() {
        return julianDay;
    }

    public int getExceptionType() {
        return exceptionType;
    }

    @Override
    public String toString() {
        return "GtfsStagingCalendarDate{" +
                "serviceId=" + serviceId +
                ", julianDay=" + julianDay +
                ", exceptionType=" + exceptionType +
                '}';
    }

    public static Comparator<GtfsStagingCalendarDate> SORT_BY_JULIAN_DAY
            = new Comparator<GtfsStagingCalendarDate>() {
        @Override
        public int compare(GtfsStagingCalendarDate lhs, GtfsStagingCalendarDate rhs) {
            return rhs.julianDay - lhs.julianDay;
        }
    };
}
