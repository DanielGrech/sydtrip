package com.dgsd.sydtrip.transformer;

import com.dgsd.sydtrip.transformer.gtfs.GtfsFile;
import com.dgsd.sydtrip.transformer.gtfs.OutputConverter;
import com.dgsd.sydtrip.transformer.gtfs.SourceConverter;
import com.dgsd.sydtrip.transformer.gtfs.StagingConverter;
import com.dgsd.sydtrip.transformer.gtfs.TargetConverter;
import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.BaseStagingModel;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendar;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendarDate;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingRoute;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStop;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingTrip;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static com.dgsd.sydtrip.transformer.gtfs.GtfsFile.ROUTES;
import static com.dgsd.sydtrip.transformer.gtfs.GtfsFile.STOP_TIMES;
import static java.util.stream.Collectors.toMap;

public class Application {

    private final static Logger LOG = Logger.getLogger(Application.class.getName());

    private final Map<GtfsFile, File> gtfsFiles;

    private final String databaseFilePath;

    public Application(File gtfsFolder, String databaseFilePath) {
        this.gtfsFiles = Arrays.asList(gtfsFolder.listFiles())
                .parallelStream()
                .filter(file -> (file.exists() && file.isFile()))
                .map(f -> Pair.of(GtfsFile.named(f.getName()), f))
                .filter(p -> p.getLeft() != null && p.getLeft().enabled())
                .collect(toMap(Pair::getLeft, Pair::getRight));

        this.databaseFilePath = databaseFilePath;
    }

    private Map<GtfsFile, List<? extends BaseGtfsModel>> getSourceModels() {
        try {
            LOG.info("Raw -> Source...");
            return gtfsFiles.entrySet()
                    .parallelStream()
                    .map(e -> Pair.of(e.getKey(),
                            SourceConverter.processSourceFile(e.getKey(), e.getValue())))
                    .filter(p -> p.getRight() != null && !p.getRight().isEmpty())
                    .collect(toMap(p -> p.getLeft(), p -> p.getRight()));
        } finally {
            LOG.info("Done");
        }
    }

    private Map<GtfsFile, List<? extends BaseStagingModel>> getStagingModels(
            Map<GtfsFile, List<? extends BaseGtfsModel>> sourceModels) {
        try {
            LOG.info("Source -> Staging...");
            return sourceModels.entrySet()
                    .parallelStream()
                    .map(e -> Pair.of(
                            e.getKey(), StagingConverter.processGtfsFile(e.getKey(), e.getValue())))
                    .filter(p -> p.getRight() != null && !p.getRight().isEmpty())
                    .collect(toMap(p -> p.getLeft(), p -> p.getRight()));
        } finally {
            LOG.info("Done");
        }
    }

    public void run() {
        final Map<GtfsFile, List<? extends BaseStagingModel>> fileToStagingModels
                = getStagingModels(getSourceModels());

        final List<GtfsStagingStopTime> stagingStopTimes
                = (List<GtfsStagingStopTime>) fileToStagingModels.get(STOP_TIMES);
        final List<GtfsStagingRoute> stagingRoutes
                = (List<GtfsStagingRoute>) fileToStagingModels.get(ROUTES);
        final List<GtfsStagingCalendar> stagingCal =
                (List<GtfsStagingCalendar>) fileToStagingModels.get(GtfsFile.CALENDAR);
        final List<GtfsStagingCalendarDate> stagingCalEx =
                (List<GtfsStagingCalendarDate>) fileToStagingModels.get(GtfsFile.CALENDAR_DATES);
        final List<GtfsStagingStop> stagingStops =
                (List<GtfsStagingStop>) fileToStagingModels.get(GtfsFile.STOPS);
        final List<GtfsStagingTrip> stagingTrips =
                (List<GtfsStagingTrip>) fileToStagingModels.get(GtfsFile.TRIPS);

        final TargetConverter targetConverter = new TargetConverter(stagingTrips,
                stagingStopTimes, stagingRoutes, stagingCal, stagingCalEx, stagingStops);

        final Map<Integer, List<Trip>> typeToTripMap = targetConverter.getTripMap();

        final OutputConverter outputConverter
                = new OutputConverter(databaseFilePath, targetConverter);
        outputConverter.convert();

        typeToTripMap.keySet().forEach(type -> {
//            Set<GraphEdge> edges = new HashSet<>();
//            tripsForType.forEach(trip -> {
//                for (int i = 0, len = trip.getStops().size(); i < len; i++) {
//                    final StopTime current = trip.getStops().get(i);
//                    final StopTime prev = i == 0 ? null : trip.getStops().get(i - 1);
//
//                    if (prev != null) {
//                        final int from = prev.getStopId();
//                        final int to = current.getStopId();
//                        final int cost = current.getTime() - prev.getTime();
//
//                        edges.add(new GraphEdge(from, to, cost));
//                    }
//                }
//            });
//
//            final Set<GraphEdge> graph = edges.stream()
//                    .map(edge -> edgeFromParents(edge, targetConverter.getStopMap()))
//                    .collect(toSet());

            // Add parent stops
        });
    }
}
