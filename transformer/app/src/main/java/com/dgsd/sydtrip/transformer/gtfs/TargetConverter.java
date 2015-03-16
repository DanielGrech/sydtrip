package com.dgsd.sydtrip.transformer.gtfs;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendar;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendarDate;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingRoute;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStop;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingTrip;
import com.dgsd.sydtrip.transformer.gtfs.model.target.CalendarInformation;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class TargetConverter {

    private final Logger LOG = Logger.getLogger(TargetConverter.class.getName());

    final List<GtfsStagingTrip> stagingTrips;
    final List<GtfsStagingStopTime> stagingStopTimes;
    final List<GtfsStagingRoute> stagingRoutes;
    final List<GtfsStagingCalendar> stagingCal;
    final List<GtfsStagingCalendarDate> stagingCalEx;
    final List<GtfsStagingStop> stagingStops;

    private Map<Integer, List<StopTime>> stopTimeMap;

    private Map<Integer, Route> routeMap;

    private Map<Integer, GtfsStagingCalendar> calendarMap;

    private Map<Integer, List<GtfsStagingCalendarDate>> calendarExMap;

    private Map<Integer, Stop> stopMap;

    private Map<Integer, List<Trip>> tripMap;

    public TargetConverter(
            List<GtfsStagingTrip> stagingTrips,
            List<GtfsStagingStopTime> stagingStopTimes,
            List<GtfsStagingRoute> stagingRoutes,
            List<GtfsStagingCalendar> stagingCal,
            List<GtfsStagingCalendarDate> stagingCalEx,
            List<GtfsStagingStop> stagingStops) {
        this.stagingTrips = stagingTrips;
        this.stagingStopTimes = stagingStopTimes;
        this.stagingRoutes = stagingRoutes;
        this.stagingCal = stagingCal;
        this.stagingCalEx = stagingCalEx;
        this.stagingStops = stagingStops;
    }

    public Map<Integer, List<StopTime>> getStopTimeMap() {
        if (stopTimeMap == null) {
            stopTimeMap = convertStopTimes(stagingStopTimes);
        }
        return stopTimeMap;
    }

    public Map<Integer, Route> getRouteMap() {
        if (routeMap == null) {
            routeMap = convertRoutes(stagingRoutes);
        }
        return routeMap;
    }

    public Map<Integer, Stop> getStopMap() {
        if (stopMap == null) {
            stopMap = convertStops(stagingStops);
        }
        return stopMap;
    }

    public Map<Integer, List<GtfsStagingCalendarDate>> getCalendarExceptionMap() {
        if (calendarExMap == null) {
            calendarExMap = convertCalendarExceptions(stagingCalEx);
        }
        return calendarExMap;
    }

    public Map<Integer, GtfsStagingCalendar> getCalendarMap() {
        if (calendarMap == null) {
            calendarMap = convertCalendar(stagingCal);
        }
        return calendarMap;
    }

    public Map<Integer, List<Trip>> getTripMap() {
        if (tripMap == null) {
            tripMap = convertTrips(stagingTrips).parallelStream().collect(toSet())
                    .parallelStream()
                    .filter(t -> Objects.nonNull(t.getRoute()))
                    .collect(groupingBy(t -> t.getRoute().getRouteType()));
        }
        return tripMap;
    }

    private Map<Integer, List<StopTime>> convertStopTimes(List<GtfsStagingStopTime> times) {
        return execute("Stop Time", (Task<Map<Integer, List<StopTime>>>)() -> {
            if (times == null || times.isEmpty()) {
                return Collections.emptyMap();
            }

            final Map<Integer, List<StopTime>> retval = new HashMap<>();
            final Map<Integer, List<GtfsStagingStopTime>> segmentedByTripId
                    = times.stream().collect(groupingBy(GtfsStagingStopTime::getTripId));

            segmentedByTripId.keySet().forEach(key -> {
                retval.put(key, segmentedByTripId.get(key)
                        .parallelStream()
                        .sorted(GtfsStagingStopTime.SORT_BY_SEQUENCE)
                        .map(stagingTime -> new StopTime(stagingTime))
                        .collect(toList()));
            });

            return retval;
        });
    }

    private Map<Integer, Route> convertRoutes(List<GtfsStagingRoute> routes) {
        return execute("Routes", () -> routes.stream()
                .map(stagingRoute -> new Route(stagingRoute))
                .collect(toMap(Route::getId, Function.identity(), (id, route) -> route)));
    }

    private Map<Integer, GtfsStagingCalendar> convertCalendar(List<GtfsStagingCalendar> calendars) {
        return execute("Calendars", () -> calendars.stream()
                .collect(toMap(GtfsStagingCalendar::getServiceId,
                        Function.identity(), (serviceId, cal) -> cal)));
    }

    private Map<Integer, List<GtfsStagingCalendarDate>> convertCalendarExceptions
            (List<GtfsStagingCalendarDate> exceptions) {
        return execute("Calendar Ex", () -> exceptions.stream()
                .sorted(GtfsStagingCalendarDate.SORT_BY_JULIAN_DAY)
                .collect(groupingBy(GtfsStagingCalendarDate::getServiceId)));
    }

    private Map<Integer, Stop> convertStops(List<GtfsStagingStop> stops) {
        return execute("Stops", () -> stops.stream()
                .map(s -> new Stop(s))
                .collect(toMap(Stop::getId, Function.identity(), (id, stop) -> stop)));
    }

    private List<Trip> convertTrips(List<GtfsStagingTrip> trips) {
        final Map<Integer, Route> routesMap = getRouteMap();
        final Map<Integer, GtfsStagingCalendar> calMap = getCalendarMap();
        final Map<Integer, List<GtfsStagingCalendarDate>> calExMap = getCalendarExceptionMap();
        final Map<Integer, List<StopTime>> stopTimesMap = getStopTimeMap();

        return execute("Trips", () -> trips.parallelStream().map(stagingTrip -> {
            final List<StopTime> stopTimes = stopTimesMap.get(stagingTrip.getId());
            final Route route = routesMap.get(stagingTrip.getRouteId());

            final GtfsStagingCalendar stagingCalendar
                    = calMap.get(stagingTrip.getServiceId());
            final List<GtfsStagingCalendarDate> stagingCalendarDates
                    = calExMap.get(stagingTrip.getServiceId());

            final CalendarInformation calInfo;
            if (stagingCalendar == null) {
                calInfo = null;
            } else {
                calInfo = new CalendarInformation(stagingCalendar, stagingCalendarDates);
            }

            return new Trip(stagingTrip, stopTimes, route, calInfo);
        }).collect(toList()));
    }

    private <T> T execute(String name, Task<T> t) {
        try {
            LOG.info(String.format("[%s] Staging -> Target", name));
            return t.execute();
        } finally {
            LOG.info(String.format("[%s] Done", name));
        }
    }

    private interface Task<T> {
        T execute();
    }
}
