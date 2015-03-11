package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendar;

public class GtfsStagingCalendar extends BaseStagingModel<GtfsCalendar> {

    private static final int DAY_MONDAY_ONLY = Integer.parseInt("1000000", 2);
    private static final int DAY_TUESDAY_ONLY = Integer.parseInt("0100000", 2);
    private static final int DAY_WEDNESDAY_ONLY = Integer.parseInt("0010000", 2);
    private static final int DAY_THURSDAY_ONLY = Integer.parseInt("0001000", 2);
    private static final int DAY_FRIDAY_ONLY = Integer.parseInt("0000100", 2);
    private static final int DAY_SATURDAY_ONLY = Integer.parseInt("0000010", 2);
    private static final int DAY_SUNDAY_ONLY = Integer.parseInt("0000001", 2);

    private final int serviceId;

    private final int dayBitmask;

    private final int startJulianDay;

    private final int endJulianDay;

    public GtfsStagingCalendar(GtfsCalendar gtfsObject) {
        super(gtfsObject);

        this.serviceId = IdConverter.convertServiceId(gtfsObject.getServiceId());
        this.dayBitmask = createDayBitmask(gtfsObject);
        this.startJulianDay = parseDateToJulianDay(gtfsObject.getStartDate());
        this.endJulianDay = parseDateToJulianDay(gtfsObject.getEndDate());
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getDayBitmask() {
        return dayBitmask;
    }

    public int getStartJulianDay() {
        return startJulianDay;
    }

    public int getEndJulianDay() {
        return endJulianDay;
    }

    private int createDayBitmask(GtfsCalendar calendar) {
        int retval = 0;

        if (isDayFlagOn(calendar.getMonday())) {
            retval |= DAY_MONDAY_ONLY;
        }
        if (isDayFlagOn(calendar.getTuesday())) {
            retval |= DAY_TUESDAY_ONLY;
        }
        if (isDayFlagOn(calendar.getWednesday())) {
            retval |= DAY_WEDNESDAY_ONLY;
        }
        if (isDayFlagOn(calendar.getThursday())) {
            retval |= DAY_THURSDAY_ONLY;
        }
        if (isDayFlagOn(calendar.getFriday())) {
            retval |= DAY_FRIDAY_ONLY;
        }
        if (isDayFlagOn(calendar.getSaturday())) {
            retval |= DAY_SATURDAY_ONLY;
        }
        if (isDayFlagOn(calendar.getSunday())) {
            retval |= DAY_SUNDAY_ONLY;
        }

        return retval;
    }

    private boolean isDayFlagOn(String input) {
        return safeParseInt(input, 0) != 0;
    }

    public boolean isMonday() {
        return hasDaySet(DAY_MONDAY_ONLY);
    }

    public boolean isTuesday() {
        return hasDaySet(DAY_TUESDAY_ONLY);
    }

    public boolean isWednesday() {
        return hasDaySet(DAY_WEDNESDAY_ONLY);
    }

    public boolean isThursday() {
        return hasDaySet(DAY_THURSDAY_ONLY);
    }

    public boolean isFriday() {
        return hasDaySet(DAY_FRIDAY_ONLY);
    }

    public boolean isSaturday() {
        return hasDaySet(DAY_SATURDAY_ONLY);
    }

    public boolean isSunday() {
        return hasDaySet(DAY_SUNDAY_ONLY);
    }

    private boolean hasDaySet(int flag) {
        return (dayBitmask & flag) == flag;
    }

    @Override
    public String toString() {
        return "GtfsStagingCalendar{" +
                "serviceId=" + serviceId +
                ", dayBitmask=" + dayBitmask +
                ", startJulianDay=" + startJulianDay +
                ", endJulianDay=" + endJulianDay +
                '}';
    }
}
