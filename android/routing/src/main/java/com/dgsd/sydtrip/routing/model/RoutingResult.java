package com.dgsd.sydtrip.routing.model;

import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopPair;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class RoutingResult implements Iterable<RoutingItinery> {

    private final List<RoutingItinery> itineries;

    private final StopPair originAndDest;

    public RoutingResult(StopPair originAndDest) {
        this.itineries = new LinkedList<>();
        this.originAndDest = originAndDest;
    }

    public void add(RoutingItinery itinery) {
        itineries.add(itinery);
    }

    public List<RoutingItinery> getItineries() {
        return itineries;
    }

    public Stop getOrigin() {
        return this.originAndDest.getFrom();
    }

    public Stop getDestination() {
        return this.originAndDest.getTo();
    }

    public void sortItineries() {
        Collections.sort(itineries, RoutingItinery.SORT_BY_START_TIME);
    }

    @Override
    public Iterator<RoutingItinery> iterator() {
        return itineries.iterator();
    }

    @Override
    public String toString() {
        return "RoutingResult{" +
                "itineries=" + itineries +
                '}';
    }
}
