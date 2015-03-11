package com.dgsd.sydtrip.routing.model;

import com.dgsd.sydtrip.model.Route;
import com.dgsd.sydtrip.model.StopTime;
import com.dgsd.sydtrip.model.Trip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.LinkedList;
import java.util.List;

public class ItineryBlock {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final Trip trip;

    private final Route route;

    private final List<StopTime> stopTimes;

    public ItineryBlock(Trip trip, Route route, List<StopTime> stopsWithTimes) {
        this.trip = trip;
        this.route = route;
        this.stopTimes = new LinkedList <>();
        this.stopTimes.addAll(stopsWithTimes);
    }

    public Trip getTrip() {
        return trip;
    }

    public Route getRoute() {
        return route;
    }

    public List<StopTime> getStopsTimes() {
        return stopTimes;
    }

    public int getStartTime() {
        return stopTimes.isEmpty() ?
                -1 : stopTimes.get(0).getSecondsSinceMidnight();
    }

    public int getEndTime() {
        return stopTimes.isEmpty() ?
                -1 : stopTimes.get(stopTimes.size() - 1).getSecondsSinceMidnight();
    }

    public int getDuration() {
        final int start = getStartTime();
        final int end = getEndTime();

        if (start < 0 || end < 0) {
            return 0;
        } else {
            return end - start;
        }
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }
}
