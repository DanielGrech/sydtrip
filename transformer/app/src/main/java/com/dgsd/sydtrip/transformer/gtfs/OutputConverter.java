package com.dgsd.sydtrip.transformer.gtfs;

import com.dgsd.sydtrip.transformer.CSVOutput;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

public class OutputConverter {

    private final String dbFilePath;
    private final TargetConverter targetConverter;

    public OutputConverter(String dbFilePath, TargetConverter targetConverter) {
        this.dbFilePath = dbFilePath;
        this.targetConverter = targetConverter;
    }

    public void convert() {
        this.targetConverter.getTripMap().keySet().forEach(type -> {
            final List<Trip> trips = targetConverter.getTripMap().get(type);

            final Set<Integer> stopIds = getStopIds(trips);
            final List<Stop> stops = getStops(stopIds, type);

            new CSVOutput(dbFilePath + getFolderNameForRouteType(type))
                    .persist(trips, stops, null);
        });
    }

    private List<Stop> getStops(Set<Integer> stopIds, int type) {
        final List<Stop> stops = targetConverter.getStopMap()
                .values().parallelStream().filter(s -> stopIds.contains(s.getId()))
                .collect(toList());
        stops.forEach(s -> s.setStopType(type));

        return stops;
    }

    private Set<Integer> getStopIds(List<Trip> trips) {
        final Set<Integer> retval = trips.stream()
                .flatMap(t -> t.getStops().stream())
                .map(StopTime::getStopId)
                .collect(toSet());

        retval.addAll(
                targetConverter.getStopMap().values()
                        .parallelStream()
                        .filter(s -> s.getParentStopId() > 0)
                        .filter(s -> retval.contains(s.getId()))
                        .map(Stop::getParentStopId)
                        .collect(toList())
        );

        return retval;
    }

    private String getFolderNameForRouteType(int type) {
        switch (type) {
            case Route.TYPE_BUS:
                return "bus/";
            case Route.TYPE_RAIL:
                return "rail/";
            case Route.TYPE_FERRY:
                return "ferry/";
            case Route.TYPE_TRAM:
                return "lightrail/";
            default:
                return "unknown/";
        }
    }
}
