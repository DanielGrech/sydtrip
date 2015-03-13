package com.dgsd.sydtrip.transformer;

import com.dgsd.sydtrip.transformer.exception.DatabaseOperationException;
import com.dgsd.sydtrip.transformer.gtfs.model.target.CalendarInformation;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVWriter;

public class CSVOutput {

    private final static Logger LOG = Logger.getLogger(Database.class.getName());

    private final String outputPath;

    private final Map<String, Integer> dynamicTextCache;

    private final Map<List<StopTime>, Integer> stopTimeCache;

    private final AtomicInteger dynamicTextGenerator;

    private final AtomicInteger stopTimeGenerator;

    public CSVOutput(String outputPath) {
        this.outputPath = outputPath;
        this.dynamicTextCache = new LinkedHashMap<>();
        this.dynamicTextGenerator = new AtomicInteger(1);

        this.stopTimeCache = new LinkedHashMap<>();
        this.stopTimeGenerator = new AtomicInteger(1);

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
        persistStopTimes();
        persistGraph(stopGraph);
//        persistDynamicText();
    }

    private void persistGraph(Collection<GraphEdge> edges) {
        final CSVWriter writer = getWriter("network_graph.csv");
        try {
            for (GraphEdge edge : edges) {
                writer.writeNext(new String[]{
                        String.valueOf(edge.getFrom()),
                        String.valueOf(edge.getTo()),
                        String.valueOf(edge.getCost())
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

    private void persistDynamicText() {
        final CSVWriter writer = getWriter("text.csv");
        try {
            for (Map.Entry<String, Integer> entry : dynamicTextCache.entrySet()) {
                writer.writeNext(new String[]{
                        entry.getKey(),
                        String.valueOf(entry.getValue())
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

    private void persistStops(List<Stop> stops) {
        final CSVWriter writer = getWriter("stops.csv");
        try {
            for (Stop stop : stops) {
                writer.writeNext(new String[]{
                        String.valueOf(stop.getId()),
                        stop.getCode(),
//                        String.valueOf(getDynamicStringId(stop.getName())),
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

                tripWriter.writeNext(new String[]{
                        String.valueOf(trip.getId()),
//                        String.valueOf(getDynamicStringId(trip.getHeadSign())),
                        trip.getHeadSign(),
                        String.valueOf(trip.getDirection()),
                        String.valueOf(trip.getBlockId()),
                        trip.isWheelchairAccessible() ? "1" : "0",
                        String.valueOf(route == null ? 0 : route.getId()),
                        String.valueOf(getStopTimeListId(trip.getStops()))
                });

                if (route != null) {
                    persistRoute(routeWriter, route);
                }

                final CalendarInformation calInfo = trip.getCalendarInformation();
                if (calInfo != null) {
                    persistCalendarInfo(calInfoWriter, trip.getId(), calInfo);

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
                String.valueOf(route.getAgencyId()),
//                String.valueOf(getDynamicStringId(route.getShortName())),
//                String.valueOf(getDynamicStringId(route.getLongName())),
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

//    private int getDynamicStringId(String text) {
//        if (StringUtils.isEmpty(text)) {
//            return 0;
//        }
//
//        final Integer id = dynamicTextCache.get(text);
//        if (id == null) {
//            final int retval = dynamicTextGenerator.incrementAndGet();
//            dynamicTextCache.put(text, retval);
//            return retval;
//        } else {
//            return id;
//        }
//    }

    private CSVWriter getWriter(String fileName) {
        try {
            return new CSVWriter(new FileWriter(outputPath + fileName));
        } catch (IOException e) {
            throw new DatabaseOperationException(e);
        }
    }
}
