package com.dgsd.sydtrip.transformer;

import com.dgsd.sydtrip.transformer.exception.DatabaseOperationException;
import com.dgsd.sydtrip.transformer.gtfs.model.target.CalendarInformation;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import org.supercsv.io.CsvListWriter;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.quote.QuoteMode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CSVOutput {

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
        try {
            persistStops(stops);
            persistTrips(trips);
//        persistStopTimes();
            persistGraph(stopGraph);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private void persistGraph(Collection<GraphEdge> edges) throws IOException {
        final CsvListWriter writer = getWriter("network_graph.csv");
        try {
            for (GraphEdge edge : edges) {
                writer.write(
                        edge.getFrom(),
                        edge.getTo(),
                        edge.getTripId(),
                        edge.getDepartureTime(),
                        edge.getArrivalTime()
                );
            }
        } finally {
            try {
                writer.close();
            } catch (IOException ex) {
                throw new DatabaseOperationException(ex);
            }
        }
    }

    private void persistStopTimes() throws IOException {
        final CsvListWriter writer = getWriter("stop_times.csv");
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

    private void persistStops(List<Stop> stops) throws IOException {
        final CsvListWriter writer = getWriter("stops.csv");
        try {
            for (Stop stop : stops) {
                writer.write(
                        stop.getId(),
                        stop.getCode(),
                        stop.getName(),
                        stop.getLat(),
                        stop.getLng(),
                        stop.getType(),
                        stop.getParentStopId(),
                        stop.getPlatformCode()
                );
            }
        } finally {
            try {
                writer.close();
            } catch (IOException e) {
                throw new DatabaseOperationException(e);
            }
        }
    }

    private void persistTrips(List<Trip> trips) throws IOException {
        final CsvListWriter tripWriter = getWriter("trips.csv");
        final CsvListWriter routeWriter = getWriter("routes.csv");
        final CsvListWriter calInfoWriter = getWriter("cal_info.csv");
        final CsvListWriter calInfoExWriter = getWriter("cal_info_ex.csv");

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

                tripWriter.write(
                        trip.getId(),
                        trip.getHeadSign(),
                        trip.getDirection(),
                        trip.getBlockId(),
                        trip.isWheelchairAccessible() ? 1 : 0,
                        routeId,
                        getStopTimeListId(trip.getStops()),
                        calInfoId
                );

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

    private void persistCalendarInfoEx(CsvListWriter writer, int tripId, int julianDay, int exType) throws IOException {
        writer.write(
                tripId,
                julianDay,
                exType
        );
    }

    private void persistCalendarInfo(CsvListWriter writer, int tripId, CalendarInformation info) throws IOException {
        writer.write(
                tripId,
                info.getStartJulianDate(),
                info.getEndJulianDate(),
                info.getAvailabilityBitmask()
        );
    }

    private void persistStopTime(CsvListWriter writer, int tripId, StopTime stopTime) throws IOException {
        writer.write(
                tripId,
                stopTime.getStopId(),
                stopTime.getTime()
        );
    }

    private void persistRoute(CsvListWriter writer, Route route) throws IOException {
        writer.write(
                route.getId(),
                route.getShortName(),
                route.getLongName(),
                route.getColor()
        );
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

    @SuppressWarnings("unused")
    private CsvListWriter getWriter(String fileName) {
        try {
            final QuoteMode qm = (csvColumn, context, preference) -> {
                try {
                    if (csvColumn != null) {
                        float val = Float.valueOf(csvColumn);
                    }
                    return false;
                } catch (Throwable t) {
                    return true;
                }
            };

            return new CsvListWriter(new FileWriter(outputPath + fileName),
                    new CsvPreference.Builder(CsvPreference.STANDARD_PREFERENCE)
                            .useQuoteMode(qm)
                            .build());
        } catch (IOException e) {
            throw new DatabaseOperationException(e);
        }
    }
}
