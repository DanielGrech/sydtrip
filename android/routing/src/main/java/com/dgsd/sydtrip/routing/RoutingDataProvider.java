package com.dgsd.sydtrip.routing;

import com.dgsd.sydtrip.model.CalendarInfo;
import com.dgsd.sydtrip.model.Route;
import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopTime;
import com.dgsd.sydtrip.model.Trip;

import java.util.List;

public interface RoutingDataProvider {

    public int[] getTripsForStopId(int stopId);

    public int[] getStopIdsForTrip(int tripId);

    public int[] getStopIdsAtSameLocation(int stopId);

    public Stop getStop(int stopId);

    public Trip getTrip(int tripId);

    public Route getRoute(int routeId);

    public CalendarInfo getCalendarInfo(int tripId, int julianDay);

    public List<StopTime> getStopsAndTimesForTrip(int tripId);
}
