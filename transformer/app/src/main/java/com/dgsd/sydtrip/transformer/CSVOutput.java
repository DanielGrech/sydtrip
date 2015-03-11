package com.dgsd.sydtrip.transformer;

import com.dgsd.sydtrip.transformer.exception.DatabaseOperationException;
import com.dgsd.sydtrip.transformer.gtfs.model.target.CalendarInformation;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import au.com.bytecode.opencsv.CSVWriter;

public class CSVOutput {

    private final static Logger LOG = Logger.getLogger(Database.class.getName());

    private final String outputPath;

    private final Map<String, Integer> dynamicTextCache;

    private final AtomicInteger dynamicTextGenerator;

    public CSVOutput(String outputPath) {
        this.outputPath = outputPath;
        this.dynamicTextCache = new LinkedHashMap<>();
        this.dynamicTextGenerator = new AtomicInteger(1);

        final File dir = new File(outputPath);
        if (!dir.exists()) {
            dir.mkdirs();
        } else if (!dir.isDirectory()) {
            throw new IllegalStateException(outputPath + " is not a directory!");
        }
    }

    public void persist(List<Trip> trips, List<Stop> stops) {
        persistStops(stops);
        persistTrips(trips);
//        persistDynamicText();
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
        LOG.info(String.format("Persisting %s stops", stops.size()));
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
        final CSVWriter stopTimeWriter = getWriter("stop_times.csv");
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
                        String.valueOf(route == null ? 0 : route.getId())
                });

                if (route != null) {
                    persistRoute(routeWriter, route);
                }

                final List<StopTime> stopTimes = trip.getStops();
                if (stopTimes != null) {
                    for (StopTime stopTime : stopTimes) {
                        persistStopTime(stopTimeWriter, trip.getId(), stopTime);
                    }
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
                stopTimeWriter.close();
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
        writer.writeNext(new String[] {
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
