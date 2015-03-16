package com.dgsd.sydtrip.routing;

import com.dgsd.sydtrip.model.CalendarInfo;
import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopPair;
import com.dgsd.sydtrip.model.StopTime;
import com.dgsd.sydtrip.routing.model.RoutingResult;
import com.dgsd.sydtrip.routing.util.AStarSearch;
import com.dgsd.sydtrip.routing.util.LocationUtils;

import java.util.List;

import gnu.trove.TIntArrayList;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntObjectHashMap;
import jodd.datetime.JDateTime;
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

                final Stop origParentStop = getParentStopOrDefault(stopIdsForOrig, orig.getId());
                final Stop destParentStop = getParentStopOrDefault(stopIdsForDest, dest.getId());

                final TIntArrayList tripsWhichStopAtOrig = getTripsForStopIds(stopIdsForOrig);
                final TIntArrayList tripsWhichStopAtDest = getTripsForStopIds(stopIdsForDest);

                dataProvider.getNetworkGraph().findBestPath(
                        origParentStop,
                        destParentStop,
                        new AStarSearch.Heuristic() {
                            @Override
                            public int score(Stop start, Stop goal, Stop toAssess) {
                                return LocationUtils.distanceBetween(goal, toAssess);
                            }
                        }
                );

                return Observable.empty();

//                final int[] tripsWhichStopAtOrig = getTripsForStopIds(stopIdsForOrig);
//                final int[] tripsWhichStopAtDest = getTripsForStopIds(stopIdsForDest);
//
//                final int[] intersection = intersect(tripsWhichStopAtOrig, tripsWhichStopAtDest);
//
//                final int julianDay = constraints.getJulianDay();
//                final int dayOfWeek = new JDateTime(new JulianDateStamp(julianDay, 0)).getDayOfWeek();
//
//                final RoutingResult rr = new RoutingResult(originAndDestination);
//
//                if (intersection.length > 0) {
//                    for (int tripId : intersection) {
//                        final CalendarInfo calInfo = dataProvider.getCalendarInfo(tripId, julianDay);
//                        if (calendarInfoIsValid(calInfo, dayOfWeek)) {
//                            final List<StopTime> times = dataProvider.getStopsAndTimesForTrip(tripId);
//                            if (tripIsInCorrectDirection(times, stopIdsForOrig, stopIdsForDest)) {
//                                final Trip trip = dataProvider.getTrip(tripId);
//                                final Route route = dataProvider.getRoute(trip.getRouteId());
//                                final RoutingItinery itinery
//                                        = RoutingItinery.createFromSingleBlock(trip, route, times);
//
//                                final int startId = getFirstStopIdIn(times, stopIdsForOrig);
//                                final int endId = getFirstStopIdIn(times, stopIdsForDest);
//
//                                itinery.setOriginStation(dataProvider.getStop(startId));
//                                itinery.setDestinationStation(dataProvider.getStop(endId));
//
//                                rr.add(itinery);
//                            }
//                        }
//                    }
//                } else {
//                    final TIntObjectHashMap origStopIdsByTrip
//                            = combine(tripsWhichStopAtOrig, getStopIdsForTrips(tripsWhichStopAtOrig));
//
//                    final TIntObjectHashMap destStopIdsByTrip
//                            = combine(tripsWhichStopAtDest, getStopIdsForTrips(tripsWhichStopAtDest));
//                }
//
//                rr.sortItineries();
//
//                return Observable.just(rr);
            }
        });
    }

    private Stop getParentStopOrDefault(int[] potentialStops, int defaultStopId) {
        Stop retval = null;
        for (int stopId : potentialStops) {
            final Stop stop = dataProvider.getStop(stopId);
            if (stop.getParentId() == 0) {
                retval = stop;
            } else if (retval == null || defaultStopId == stop.getId()) {
                retval = stop;
            }
        }
        return retval;
    }

    private boolean calendarInfoIsValid(CalendarInfo info, int dayOfWeek) {
        if (info != null) {
            switch (dayOfWeek) {
                case JDateTime.MONDAY:
                    return info.monday();
                case JDateTime.TUESDAY:
                    return info.tuesday();
                case JDateTime.WEDNESDAY:
                    return info.wednesday();
                case JDateTime.THURSDAY:
                    return info.thursday();
                case JDateTime.FRIDAY:
                    return info.friday();
                case JDateTime.SATURDAY:
                    return info.saturday();
                case JDateTime.SUNDAY:
                    return info.sunday();
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

    private TIntArrayList getTripsForStopIds(int[] stopIds) {
        TIntArrayList retval = new TIntArrayList();
        for (int i = 0, len = stopIds.length; i < len; i++) {
            retval.add(this.dataProvider.getTripsForStopId(stopIds[i]));
        }

        return retval;
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

    private TIntArrayList intersect(int[] lhs, int[] rhs) {
        final TIntHashSet set = new TIntHashSet();

        set.addAll(lhs);
        set.retainAll(rhs);

        return new TIntArrayList(set.toArray());
    }

}
