package com.dgsd.android.sydtrip;

import com.dgsd.android.data.DataSource;
import com.dgsd.sydtrip.model.CalendarInfo;
import com.dgsd.sydtrip.model.Route;
import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopTime;
import com.dgsd.sydtrip.model.Trip;
import com.dgsd.sydtrip.routing.RoutingDataProvider;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import timber.log.Timber;

public class DataSourceRoutingDataProvider implements RoutingDataProvider {

    private final DataSource dataSource;

    public DataSourceRoutingDataProvider(DataSource source) {
        this.dataSource = source;
    }

    @Override
    public int[] getTripsForStopId(int stopId) {
        return this.dataSource.getTripIdsForStop(stopId).toBlocking().single();
    }

    @Override
    public int[] getStopIdsForTrip(int tripId) {
        return this.dataSource.getStopIdsForTrip(tripId).toBlocking().single();
    }

    @Override
    public int[] getStopIdsAtSameLocation(int stopId) {
        return this.dataSource.getStopIdsAtSameLocation(stopId).toBlocking().single();
    }

    @Override
    public Trip getTrip(int tripId) {
        try {
            return this.dataSource.getTrip(tripId).toBlocking().single();
        } catch (NoSuchElementException ex) {
            Timber.w("Couldn't get trip with id: %s", tripId);
        } catch (Throwable t) {
            Timber.e(t, "Couldn't get trip with id: %s", tripId);
        }

        return null;
    }

    @Override
    public Route getRoute(int routeId) {
        try {
            return this.dataSource.getRoute(routeId).toBlocking().single();
        } catch (NoSuchElementException ex) {
            Timber.w("Couldn't get route with id: %s", routeId);
        } catch (Throwable t) {
            Timber.e(t, "Couldn't get route with id: %s", routeId);
        }

        return null;
    }

    @Override
    public CalendarInfo getCalendarInfo(int tripId, int julianDay) {
        try {
            return this.dataSource.getCalendarInfo(tripId, julianDay).toBlocking().single();
        } catch (NoSuchElementException ex) {
            Timber.w("Couldn't get calendar info for trip: %s on day: %s", tripId, julianDay);
        } catch (Throwable t) {
            Timber.e(t, "Couldn't get calendar info for trip: %s on day: %s", tripId, julianDay);
        }

        return null;
    }

    public Stop getStop(int stopId) {
        try {
            return this.dataSource.getStop(stopId).toBlocking().single();
        } catch (NoSuchElementException ex) {
            Timber.w("Couldn't get stop with id: %s", stopId);
        } catch (Throwable t) {
            Timber.e(t, "Couldn't get stop with id: %s", stopId);
        }

        return null;
    }

    @Override
    public List<StopTime> getStopsAndTimesForTrip(int tripId) {
        try {
            return this.dataSource.getStopTimes(tripId).toBlocking().single();
        } catch (NoSuchElementException ex) {
            Timber.w("Couldn't get stops and times for trip with id: %s", tripId);
        } catch (Throwable t) {
            Timber.e(t, "Couldn't get stops and times for trip with id: %s", tripId);
        }
        return Collections.emptyList();
    }
}
