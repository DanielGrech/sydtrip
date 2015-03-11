package com.dgsd.sydtrip.model;

public class CalendarInfo extends BaseModel {

    private static final int DAY_MONDAY_ONLY = Integer.parseInt("1000000", 2);
    private static final int DAY_TUESDAY_ONLY = Integer.parseInt("0100000", 2);
    private static final int DAY_WEDNESDAY_ONLY = Integer.parseInt("0010000", 2);
    private static final int DAY_THURSDAY_ONLY = Integer.parseInt("0001000", 2);
    private static final int DAY_FRIDAY_ONLY = Integer.parseInt("0000100", 2);
    private static final int DAY_SATURDAY_ONLY = Integer.parseInt("0000010", 2);
    private static final int DAY_SUNDAY_ONLY = Integer.parseInt("0000001", 2);

    private final int tripId;

    private final int startDay;

    private final int endDay;

    private final int availability;

    public CalendarInfo(int tripId, int startDay, int endDay, int availability) {
        this.tripId = tripId;
        this.startDay = startDay;
        this.endDay = endDay;
        this.availability = availability;
    }

    public int getTripId() {
        return tripId;
    }

    public int getStartDay() {
        return startDay;
    }

    public int getEndDay() {
        return endDay;
    }

    public boolean monday() {
        return hasDaySet(DAY_MONDAY_ONLY);
    }

    public boolean tuesday() {
        return hasDaySet(DAY_TUESDAY_ONLY);
    }

    public boolean wednesday() {
        return hasDaySet(DAY_WEDNESDAY_ONLY);
    }

    public boolean thursday() {
        return hasDaySet(DAY_THURSDAY_ONLY);
    }

    public boolean friday() {
        return hasDaySet(DAY_FRIDAY_ONLY);
    }

    public boolean saturday() {
        return hasDaySet(DAY_SATURDAY_ONLY);
    }

    public boolean sunday() {
        return hasDaySet(DAY_SUNDAY_ONLY);
    }

    private boolean hasDaySet(int flag) {
        return (availability & flag) == flag;
    }
}
