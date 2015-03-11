package com.dgsd.sydtrip.routing.model;

import com.dgsd.sydtrip.model.Route;
import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopTime;
import com.dgsd.sydtrip.model.Trip;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RoutingItinery implements Iterable<ItineryBlock> {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final List<ItineryBlock> blocks;

    private Stop originStop;

    private Stop destinationStop;

    private Integer timeAtOrigin;
    private Integer timeAtDestination;

    public static RoutingItinery createFromSingleBlock(Trip trip, Route route, List<StopTime> stopAndTimes) {
        final ItineryBlock block = new ItineryBlock(trip, route, stopAndTimes);
        return new RoutingItinery(Collections.singletonList(block));
    }

    public RoutingItinery(List<ItineryBlock> blocks) {
        this.blocks = new LinkedList<>();
        this.blocks.addAll(blocks);
    }

    public List<ItineryBlock> getBlocks() {
        return blocks;
    }

    public int getStartTime() {
        return blocks.get(0).getStartTime();
    }

    public int getEndTime() {
        return blocks.get(blocks.size() - 1).getEndTime();
    }

    public int getTimeAtOrigin() {
        if (timeAtOrigin == null) {
            timeAtOrigin = getTimeAt(originStop);
        }

        return timeAtOrigin.intValue();
    }

    public int getTimeAtDestination() {
        if (timeAtDestination == null) {
            timeAtDestination = getTimeAt(destinationStop);
        }

        return timeAtDestination.intValue();
    }

    public int getDuration() {
        int totalSeconds = 0;
        for (ItineryBlock block : this) {
            totalSeconds += block.getDuration();
        }
        return totalSeconds;
    }

    public int getDurationOnboard() {
        return getTimeAtDestination() - getTimeAtOrigin();
    }

    public int getBestDisplayColor() {
        if (blocks.isEmpty()) {
            return 0xFFFFFFFF;
        } else {
            final int color = blocks.get(0).getRoute().getColor();
            final int red = (color >> 16) & 0xFF;
            final int green = (color >> 8) & 0xFF;
            final int blue = color & 0xFF;

            return (0xFF << 24) | (red << 16) | (green << 8) | blue;
        }
    }

    public Stop getOriginStation() {
        return originStop;
    }

    public Stop getDestinationStation() {
        return destinationStop;
    }

    public void setOriginStation(Stop stop) {
        this.originStop = stop;
    }

    public void setDestinationStation(Stop stop) {
        this.destinationStop = stop;
    }

    @Override
    public Iterator<ItineryBlock> iterator() {
        return blocks.iterator();
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    private int getTimeAt(Stop stop) {
        if (stop != null) {
            for (ItineryBlock block : this) {
                final List<StopTime> stopTimes = block.getStopsTimes();
                for (StopTime stopTime : stopTimes) {
                    if (stopTime.getStopId() == stop.getId()) {
                        return stopTime.getSecondsSinceMidnight();
                    }
                }
            }
        }

        return -1;
    }

    public static Comparator<RoutingItinery> SORT_BY_START_TIME = new Comparator<RoutingItinery>() {
        @Override
        public int compare(RoutingItinery lhs, RoutingItinery rhs) {
            return Integer.compare(lhs.getTimeAtOrigin(), rhs.getTimeAtDestination());
        }
    };
}
