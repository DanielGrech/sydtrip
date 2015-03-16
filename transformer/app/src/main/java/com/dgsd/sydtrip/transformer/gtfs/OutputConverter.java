package com.dgsd.sydtrip.transformer.gtfs;

import com.dgsd.sydtrip.transformer.CSVOutput;
import com.dgsd.sydtrip.transformer.GraphEdge;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
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

            final Set<GraphEdge> edges = createGraph(trips);

            new CSVOutput(dbFilePath + getFolderNameForRouteType(type))
                    .persist(trips, stops, edges);
        });
    }

    private Set<GraphEdge> createGraph(List<Trip> trips) {
        final HashSet<GraphEdge> graph = new LinkedHashSet<>();

        trips.forEach(trip -> {
            for (int i = 0, len = trip.getStops().size(); i < len; i++) {
                final StopTime current = trip.getStops().get(i);
                final StopTime next = i == (len - 1) ? null : trip.getStops().get(i + 1);

                if (next != null) {
                    final int from = current.getStopId();
                    final int to = next.getStopId();

                    graph.add(new GraphEdge(from, to,
                            trip.getId(), current.getTime(), next.getTime()));
                }
            }
        });

        return graph.stream()
                .map(e -> edgeFromParents(e, targetConverter.getStopMap()))
                .collect(toSet());
    }

    private GraphEdge edgeFromParents(GraphEdge edge, Map<Integer, Stop> stopIdToStop) {
        final Stop from = stopIdToStop.get(edge.getFrom());
        final Stop to = stopIdToStop.get(edge.getTo());

        final int fromIdToUse = from == null || from.getParentStopId() <= 0 ?
                edge.getFrom() : from.getParentStopId();
        final int toIdToUse = to == null || to.getParentStopId() <= 0 ?
                edge.getTo() : to.getParentStopId();

        return new GraphEdge(
                fromIdToUse, toIdToUse, edge.getTripId(),
                edge.getDepartureTime(), edge.getArrivalTime()
        );
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
