package com.dgsd.sydtrip.transformer.gtfs.model.target;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendar;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendarDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarInformation {

    private final int startJulianDate;

    private final int endJulianDate;

    private final int availabilityBitmask;

    private final Map<Integer, Integer> julianDayToExceptionMap;

    public CalendarInformation(GtfsStagingCalendar calendar, List<GtfsStagingCalendarDate> dates) {
        startJulianDate = calendar.getStartJulianDay();
        endJulianDate = calendar.getEndJulianDay();
        availabilityBitmask = calendar.getDayBitmask();

        julianDayToExceptionMap = new HashMap<>();
        if (dates != null) {
            dates.forEach(date -> {
                julianDayToExceptionMap.put(date.getJulianDay(), date.getExceptionType());
            });
        }
    }

    public int getStartJulianDate() {
        return startJulianDate;
    }

    public int getEndJulianDate() {
        return endJulianDate;
    }

    public int getAvailabilityBitmask() {
        return availabilityBitmask;
    }

    public Map<Integer, Integer> getJulianDayToExceptionMap() {
        return julianDayToExceptionMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CalendarInformation that = (CalendarInformation) o;

        if (availabilityBitmask != that.availabilityBitmask) return false;
        if (endJulianDate != that.endJulianDate) return false;
        if (startJulianDate != that.startJulianDate) return false;
        if (julianDayToExceptionMap != null ? !julianDayToExceptionMap.equals(that.julianDayToExceptionMap) : that.julianDayToExceptionMap != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = startJulianDate;
        result = 31 * result + endJulianDate;
        result = 31 * result + availabilityBitmask;
        result = 31 * result + (julianDayToExceptionMap != null ? julianDayToExceptionMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CalendarInformation{" +
                "startJulianDate=" + startJulianDate +
                ", endJulianDate=" + endJulianDate +
                ", availabilityBitmask=" + availabilityBitmask +
                ", julianDayToExceptionMap=" + julianDayToExceptionMap +
                '}';
    }
}