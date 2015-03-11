package com.dgsd.sydtrip.routing;

import com.dgsd.sydtrip.model.CalendarInfo;
import com.dgsd.sydtrip.model.Route;
import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopPair;
import com.dgsd.sydtrip.model.StopTime;
import com.dgsd.sydtrip.model.Trip;
import com.dgsd.sydtrip.routing.model.RoutingItinery;
import com.dgsd.sydtrip.routing.model.RoutingResult;

import java.util.Arrays;
import java.util.List;

import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;
import jodd.datetime.JDateTime;
import jodd.datetime.JulianDateStamp;
import rx.Observable;
import rx.functions.Func0;

public class RoutingEngine {

    RoutingDataProvider dataProvider;

    public RoutingEngine(RoutingDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public Observable<RoutingResult> route(final StopPair originAndDestination,
                                           final RoutingContraints constraints) {
        final Stop orig = originAndDestination.getFrom();
        final Stop dest = originAndDestination.getTo();

        return Observable.defer(new Func0<Observable<RoutingResult>>() {
            @Override
            public Observable<RoutingResult> call() {
                final int[] stopIdsForOrig = dataProvider.getStopIdsAtSameLocation(orig.getId());
                final int[] stopIdsForDest = dataProvider.getStopIdsAtSameLocation(dest.getId());

                final int[] tripsWhichStopAtOrig = getTripsForStopIds(stopIdsForOrig);
                final int[] tripsWhichStopAtDest = getTripsForStopIds(stopIdsForDest);

                final int[] intersection = intersect(tripsWhichStopAtOrig, tripsWhichStopAtDest);

                final int julianDay = constraints.getJulianDay();
                final int dayOfWeek = new JDateTime(new JulianDateStamp(julianDay, 0)).getDayOfWeek();

                final RoutingResult rr = new RoutingResult(originAndDestination);

                if (intersection.length > 0) {
                    for (int tripId : intersection) {
                        final CalendarInfo calInfo = dataProvider.getCalendarInfo(tripId, julianDay);
                        if (calendarInfoIsValid(calInfo, dayOfWeek)) {
                            final List<StopTime> times = dataProvider.getStopsAndTimesForTrip(tripId);
                            if (tripIsInCorrectDirection(times, stopIdsForOrig, stopIdsForDest)) {
                                final Trip trip = dataProvider.getTrip(tripId);
                                final Route route = dataProvider.getRoute(trip.getRouteId());
                                final RoutingItinery itinery
                                        = RoutingItinery.createFromSingleBlock(trip, route, times);

                                final int startId = getFirstStopIdIn(times, stopIdsForOrig);
                                final int endId = getFirstStopIdIn(times, stopIdsForDest);

                                itinery.setOriginStation(dataProvider.getStop(startId));
                                itinery.setDestinationStation(dataProvider.getStop(endId));

                                rr.add(itinery);
                            }
                        }
                    }
                } else {
                    final TIntObjectHashMap origStopIdsByTrip
                            = combine(tripsWhichStopAtOrig, getStopIdsForTrips(tripsWhichStopAtOrig));

                    final TIntObjectHashMap destStopIdsByTrip
                            = combine(tripsWhichStopAtDest, getStopIdsForTrips(tripsWhichStopAtDest));
                }

                rr.sortItineries();

                return Observable.just(rr);
            }
        });
    }

    private boolean calendarInfoIsValid(CalendarInfo info, int dayOfWeek) {
        if (info != null) {
            switch (dayOfWeek) {
                case JDateTime.MONDAY: return info.monday();
                case JDateTime.TUESDAY: return info.tuesday();
                case JDateTime.WEDNESDAY: return info.wednesday();
                case JDateTime.THURSDAY: return info.thursday();
                case JDateTime.FRIDAY: return info.friday();
                case JDateTime.SATURDAY: return info.saturday();
                case JDateTime.SUNDAY: return info.sunday();
            }
        }

        return false;
    }

    private int getFirstStopIdIn(List<StopTime> times, int[] stopIds) {
        for (StopTime time : times) {
            for (int stopId : stopIds) {
                if (time.getStopId() == stopId) {
                    return stopId;
                }
            }
        }

        return -1;
    }

    private boolean tripIsInCorrectDirection(List<StopTime> times, int[] origIds, int[] destIds) {
        if (times == null) {
            return false;
        }

        Integer origTime = null, destTime = null;
        for (StopTime time : times) {
            if (origTime == null) {
                for (int origId : origIds) {
                    if (time.getStopId() == origId) {
                        origTime = time.getSecondsSinceMidnight();
                        break;
                    }
                }
            }

            if (destTime == null) {
                for (int destId : destIds) {
                    if (time.getStopId() == destId) {
                        destTime = time.getSecondsSinceMidnight();
                        break;
                    }
                }
            }

            if (origTime != null && destTime != null) {
                if (destTime.intValue() < origTime.intValue()) {
                    // At origin before destination, must be opposite direction..
                    return false;
                }
            }
        }

        if (origTime == null || destTime == null) {
            throw new RuntimeException("GOD DAMMIT!");
        }

        return true;
    }

    private int[][] getStopIdsForTrips(int[] tripIds) {
        final int[][] retval = new int[tripIds.length][];
        for (int i = 0, len = tripIds.length; i < len; i++) {
            retval[i] = dataProvider.getStopIdsForTrip(tripIds[i]);
        }
        return retval;
    }

    private int[] getTripsForStopIds(int[] stopIds) {
        int[] retval = null;
        for (int i = 0, len = stopIds.length; i < len; i++) {
            int[] tripIds = this.dataProvider.getTripsForStopId(stopIds[i]);
            if (retval == null) {
                retval = tripIds;
            } else {
                final int origLen = retval.length;

                retval = Arrays.copyOf(retval, origLen + tripIds.length);
                System.arraycopy(tripIds, 0, retval, origLen, tripIds.length);
            }
        }

        return retval == null ? new int[0] : retval;
    }

    private TIntObjectHashMap combine(int[] tripIds, int[][] stopIdsByTrip) {
        if (tripIds.length != stopIdsByTrip.length) {
            throw new IllegalStateException("tripIds.length != stopIdsByTrip.length");
        }

        final TIntObjectHashMap retval = new TIntObjectHashMap();
        for (int i = 0, len = tripIds.length; i < len; i++) {
            retval.put(tripIds[i], stopIdsByTrip[i]);
        }

        return retval;
    }

    private int[] intersect(int[] lhs, int[] rhs) {
        final TIntHashSet set = new TIntHashSet();

        set.addAll(lhs);
        set.retainAll(rhs);

        return set.toArray();
    }

}
