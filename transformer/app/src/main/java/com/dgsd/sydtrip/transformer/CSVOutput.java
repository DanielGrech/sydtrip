package com.dgsd.sydtrip.transformer;

import com.dgsd.sydtrip.transformer.exception.DatabaseOperationException;
import com.dgsd.sydtrip.transformer.gtfs.model.target.CalendarInformation;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVWriter;

public class CSVOutput {

    private final static Logger LOG = Logger.getLogger(Database.class.getName());

    private final String outputPath;

    private final Map<List<StopTime>, Integer> stopTimeCache;
    private final Map<Route, Integer> routeCache;
    private final Map<CalendarInformation, Integer> calInfoCache;

    private final AtomicInteger stopTimeGenerator;
    private final AtomicInteger routeGenerator;
    private final AtomicInteger calInfoGenerator;

    public CSVOutput(String outputPath) {
        this.outputPath = outputPath;

        this.stopTimeCache = new LinkedHashMap<>();
        this.stopTimeGenerator = new AtomicInteger(1);

        this.routeCache = new LinkedHashMap<>();
        this.routeGenerator = new AtomicInteger(1);

        this.calInfoCache = new LinkedHashMap<>();
        this.calInfoGenerator = new AtomicInteger(1);

        final File dir = new File(outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        } else if (!dir.isDirectory()) {
            throw new IllegalStateException(outputPath + " is not a directory!");
        }
    }

    public void persist(List<Trip> trips, List<Stop> stops, Collection<GraphEdge> stopGraph) {
        persistStops(stops);
        persistTrips(trips);
//        persistStopTimes();
        persistGraph(stopGraph);
    }

    private void persistGraph(Collection<GraphEdge> edges) {
        final CSVWriter writer = getWriter("network_graph.csv");
        try {
            for (GraphEdge edge : edges) {
                writer.writeNext(new String[]{
                        String.valueOf(edge.getFrom()),
                        String.valueOf(edge.getTo()),
                        String.valueOf(edge.getTripId()),
                        String.valueOf(edge.getDepartureTime()),
                        String.valueOf(edge.getArrivalTime())
                });
            }
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                throw new DatabaseOperationException(ex);
            }
        }
    }

    private void persistStopTimes() {
        final CSVWriter writer = getWriter("stop_times.csv");
        try {
            for (Map.Entry<List<StopTime>, Integer> entry : stopTimeCache.entrySet()) {
                for (StopTime stopTime : entry.getKey()) {
                    persistStopTime(writer, entry.getValue(), stopTime);
                }
            }
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                throw new DatabaseOperationException(ex);
            }
        }
    }

    private void persistStops(List<Stop> stops) {
        final CSVWriter writer = getWriter("stops.csv");
        try {
            for (Stop stop : stops) {
                writer.writeNext(new String[]{
                        String.valueOf(stop.getId()),
                        stop.getCode(),
                        stop.getName(),
                        String.valueOf(stop.getLat()),
                        String.valueOf(stop.getLng()),
                        String.valueOf(stop.getType()),
                        String.valueOf(stop.getParentStopId()),
                        stop.getPlatformCode()
                });
            }
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new DatabaseOperationException(e);
            }
        }
    }

    private void persistTrips(List<Trip> trips) {
        final CSVWriter tripWriter = getWriter("trips.csv");
        final CSVWriter routeWriter = getWriter("routes.csv");
        final CSVWriter calInfoWriter = getWriter("cal_info.csv");
        final CSVWriter calInfoExWriter = getWriter("cal_info_ex.csv");

        try {
            for (Trip trip : trips) {
                final Route route = trip.getRoute();
                final boolean routeHasBeenSavedAlready = route == null ?
                        true : routeCache.containsKey(route);
                final int routeId = getRouteId(route);

                final CalendarInformation calInfo = trip.getCalendarInformation();
                final boolean calInfoHasBeenSavedAlready = calInfo == null ?
                        true : calInfoCache.containsKey(calInfo);
                final int calInfoId = getCalInfoId(calInfo);

                tripWriter.writeNext(new String[]{
                        String.valueOf(trip.getId()),
                        trip.getHeadSign(),
                        String.valueOf(trip.getDirection()),
                        String.valueOf(trip.getBlockId()),
                        trip.isWheelchairAccessible() ? "1" : "0",
                        String.valueOf(routeId),
                        String.valueOf(getStopTimeListId(trip.getStops())),
                        String.valueOf(calInfoId)
                });

                if (!routeHasBeenSavedAlready) {
                    persistRoute(routeWriter, new Route(routeId, route));
                }

                if (!calInfoHasBeenSavedAlready) {
                    persistCalendarInfo(calInfoWriter, calInfoId, calInfo);

                    final Map<Integer, Integer> exceptions
                            = calInfo.getJulianDayToExceptionMap();
                    if (exceptions != null) {
                        for (Map.Entry<Integer, Integer> entry : exceptions.entrySet()) {
                            persistCalendarInfoEx(calInfoExWriter,
                                    trip.getId(), entry.getKey(), entry.getValue());
                        }
                    }
                }
            }
        } finally {
            try {
                tripWriter.close();
                routeWriter.close();
                calInfoWriter.close();
                calInfoExWriter.close();
            } catch (IOException e) {
                throw new DatabaseOperationException(e);
            }
        }
    }

    private void persistCalendarInfoEx(CSVWriter writer, int tripId, int julianDay, int exType) {
        writer.writeNext(new String[]{
                String.valueOf(tripId),
                String.valueOf(julianDay),
                String.valueOf(exType)
        });
    }

    private void persistCalendarInfo(CSVWriter writer, int tripId, CalendarInformation info) {
        writer.writeNext(new String[]{
                String.valueOf(tripId),
                String.valueOf(info.getStartJulianDate()),
                String.valueOf(info.getEndJulianDate()),
                String.valueOf(info.getAvailabilityBitmask())
        });
    }

    private void persistStopTime(CSVWriter writer, int tripId, StopTime stopTime) {
        writer.writeNext(new String[]{
                String.valueOf(tripId),
                String.valueOf(stopTime.getStopId()),
                String.valueOf(stopTime.getTime())
        });
    }

    private void persistRoute(CSVWriter writer, Route route) {
        writer.writeNext(new String[]{
                String.valueOf(route.getId()),
                route.getShortName(),
                route.getLongName(),
                String.valueOf(route.getColor())
        });
    }

    private int getStopTimeListId(List<StopTime> stops) {
        if (stops == null || stops.isEmpty()) {
            return -1;
        }

        final Integer id = stopTimeCache.get(stops);
        if (id == null) {
            final int retval = stopTimeGenerator.incrementAndGet();
            stopTimeCache.put(stops, retval);
            return retval;
        } else {
            return id;
        }
    }

    private int getRouteId(Route route) {
        if (route == null) {
            return -1;
        }

        final Integer id = routeCache.get(route);
        if (id == null) {
            final int retval = routeGenerator.incrementAndGet();
            routeCache.put(route, retval);
            return retval;
        } else {
            return id;
        }
    }

    private int getCalInfoId(CalendarInformation calInfo) {
        if (calInfo == null) {
            return -1;
        }

        final Integer id = routeCache.get(calInfo);
        if (id == null) {
            final int retval = calInfoGenerator.incrementAndGet();
            calInfoCache.put(calInfo, retval);
            return retval;
        } else {
            return id;
        }
    }

    private CSVWriter getWriter(String fileName) {
        try {
            return new CSVWriter(new FileWriter(outputPath + fileName));
        } catch (IOException e) {
            throw new DatabaseOperationException(e);
        }
    }
}
