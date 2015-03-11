package com.dgsd.sydtrip.transformer.gtfs.model.target;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingTrip;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class Trip {

    private final int id;

    private final Route route;

    private final List<StopTime> stops;

    private final CalendarInformation calendarInformation;

    private final String headSign;

    private final int direction;

    private final String blockId;

    private final boolean wheelchairAccessible;

    public Trip(GtfsStagingTrip trip, List<StopTime> stops,
                Route route, CalendarInformation calendarInformation) {
        this.id = trip.getId();
        this.headSign = StringUtils.isEmpty(trip.getHeadSign()) ? "" : trip.getHeadSign().trim();
        this.direction = trip.getDirection();
        this.blockId = trip.getBlockId();
        this.wheelchairAccessible = trip.isWheelchairAccessible();

        this.stops = stops;
        this.route = route;
        this.calendarInformation = calendarInformation;
    }

    public int getId() {
        return id;
    }

    public Route getRoute() {
        return route;
    }

    public List<StopTime> getStops() {
        return stops;
    }

    public CalendarInformation getCalendarInformation() {
        return calendarInformation;
    }

    public String getHeadSign() {
        return headSign;
    }

    public int getDirection() {
        return direction;
    }

    public String getBlockId() {
        return blockId;
    }

    public boolean isWheelchairAccessible() {
        return wheelchairAccessible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Trip trip = (Trip) o;

        if (direction != trip.direction) return false;
        if (wheelchairAccessible != trip.wheelchairAccessible) return false;
        if (blockId != null ? !blockId.equals(trip.blockId) : trip.blockId != null) return false;
        if (calendarInformation != null ? !calendarInformation.equals(trip.calendarInformation) : trip.calendarInformation != null)
            return false;
        if (headSign != null ? !headSign.equals(trip.headSign) : trip.headSign != null)
            return false;
        if (route != null ? !route.equals(trip.route) : trip.route != null) return false;
        if (stops != null ? !stops.equals(trip.stops) : trip.stops != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = route != null ? route.hashCode() : 0;
        result = 31 * result + (stops != null ? stops.hashCode() : 0);
        result = 31 * result + (calendarInformation != null ? calendarInformation.hashCode() : 0);
        result = 31 * result + (headSign != null ? headSign.hashCode() : 0);
        result = 31 * result + direction;
        result = 31 * result + (blockId != null ? blockId.hashCode() : 0);
        result = 31 * result + (wheelchairAccessible ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", route=" + route +
                ", stops=" + stops +
                ", calendarInformation=" + calendarInformation +
                ", headSign='" + headSign + '\'' +
                ", direction=" + direction +
                ", blockId='" + blockId + '\'' +
                ", wheelchairAccessible=" + wheelchairAccessible +
                '}';
    }
}
