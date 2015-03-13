package com.dgsd.sydtrip.transformer;

import com.dgsd.sydtrip.transformer.exception.BadConfigurationException;
import com.dgsd.sydtrip.transformer.gtfs.GtfsFile;
import com.dgsd.sydtrip.transformer.gtfs.StagingConverter;
import com.dgsd.sydtrip.transformer.gtfs.TargetConverter;
import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.BaseStagingModel;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingAgency;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendar;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendarDate;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingRoute;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingShape;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStop;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingTrip;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Agency;
import com.dgsd.sydtrip.transformer.gtfs.model.target.CalendarInformation;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Shape;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;
import com.dgsd.sydtrip.transformer.gtfs.parser.IParser;
import com.dgsd.sydtrip.transformer.gtfs.parser.ParserFactory;
import com.dgsd.sydtrip.transformer.util.CompressionUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.logging.Logger;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

public class Application {

    private final static Logger LOG = Logger.getLogger(Application.class.getName());

    private final File rootGtfsFolder;

    private final String databaseFilePath;

    public static void main(String[] args) {
        if (args == null || args.length != 2) {
            fatalError("Usage: transformer GTFS_FOLDER OUTPUT_DB_FILE");
        }

        Application.create(args[0], args[1]).run();
    }

    private static Application create(String gtfsFolder, String databaseFilePath) {
        final File folder = new File(gtfsFolder);
        if (!folder.exists()) {
            fatalError(String.format("%s does not exist", gtfsFolder));
        } else if (!folder.isDirectory()) {
            fatalError(String.format("%s needs to be the unzipped, root GTFS folder", gtfsFolder));
        } else if (folder.listFiles().length == 0) {
            fatalError(String.format("%s is empty!", gtfsFolder));
        } else {
            if (StringUtils.isEmpty(databaseFilePath)) {
                databaseFilePath = "";
            } else if (!databaseFilePath.endsWith("/")) {
                databaseFilePath += "/";
            }

            return new Application(folder, databaseFilePath);
        }

        return null;
    }

    private Application(File gtfsFolder, String databaseFilePath) {
        this.rootGtfsFolder = gtfsFolder;
        this.databaseFilePath = databaseFilePath;
    }

    private void run() {
        LOG.info("Processing files");
        final Map<GtfsFile, List<? extends BaseStagingModel>> fileToStagingModels
                = Arrays.asList(rootGtfsFolder.listFiles())
                .parallelStream()
                .filter(file -> (file.exists() && file.isFile()))
                .map(file -> Pair.of(file, GtfsFile.named(file.getName())))
                .filter(pair -> (pair.getRight() != null && pair.getRight().enabled()))
                .map(pair -> processGtfsFile(pair))
                .collect(toMap(GtfsFileWithStagingModels::getFile, f -> f.models));
        LOG.info("Finished staging transform");

        final Map<Integer, List<StopTime>> tripIdToStopTimes = TargetConverter.convertStopTimes(
                (List<GtfsStagingStopTime>) fileToStagingModels.get(GtfsFile.STOP_TIMES));
        LOG.info("Converted stop times..");

        final Map<Integer, Route> routeMap = ((List<GtfsStagingRoute>) fileToStagingModels
                .get(GtfsFile.ROUTES))
                .stream()
                .map(stagingRoute -> new Route(stagingRoute))
                .collect(toMap(Route::getId, Function.identity(), (id, route) -> route));
        LOG.info("Converted routes..");

        final Map<Integer, GtfsStagingCalendar> calendarMap =
                ((List<GtfsStagingCalendar>) fileToStagingModels.get(GtfsFile.CALENDAR))
                        .stream()
                        .collect(toMap(GtfsStagingCalendar::getServiceId, Function.identity(), (serviceId, cal) -> cal));
        LOG.info("Converted calendars..");

        Map<Integer, List<GtfsStagingCalendarDate>> calendarDateExceptionMap =
                ((List<GtfsStagingCalendarDate>) fileToStagingModels.get(GtfsFile.CALENDAR_DATES))
                        .stream()
                        .sorted(GtfsStagingCalendarDate.SORT_BY_JULIAN_DAY)
                        .collect(groupingBy(GtfsStagingCalendarDate::getServiceId));
        LOG.info("Converted calendar exceptions..");

        final List<Stop> stops = ((List<GtfsStagingStop>) fileToStagingModels.get
                (GtfsFile.STOPS)).stream().map(staging -> new Stop(staging)).collect(toList());
        LOG.info("Converted stops..");

        final List<Trip> trips = ((List<GtfsStagingTrip>) fileToStagingModels.get(GtfsFile.TRIPS))
                .parallelStream()
                .map(stagingTrip -> {
                    final List<StopTime> stopTimes = tripIdToStopTimes.get(stagingTrip.getId());
                    final Route route = routeMap.get(stagingTrip.getRouteId());

                    final GtfsStagingCalendar stagingCalendar
                            = calendarMap.get(stagingTrip.getServiceId());
                    final List<GtfsStagingCalendarDate> stagingCalendarDates
                            = calendarDateExceptionMap.get(stagingTrip.getServiceId());

                    final CalendarInformation calInfo;
                    if (stagingCalendar == null) {
                        calInfo = null;
                    } else {
                        calInfo = new CalendarInformation(stagingCalendar, stagingCalendarDates);
                    }

                    return new Trip(stagingTrip, stopTimes, route, calInfo);
                })
                .collect(toList());

        final Map<Integer, List<Trip>> typeToTripMap = trips.parallelStream().collect(toSet())
                .parallelStream()
                .filter(trip -> Objects.nonNull(trip.getRoute()))
                .collect(groupingBy(trip -> trip.getRoute().getRouteType()));

        LOG.info("Converted trips..");

        typeToTripMap.keySet().forEach(type -> {
            final String folderName;
            switch (type) {
                case Route.TYPE_BUS:
                    folderName = "bus/";
                    break;
                case Route.TYPE_RAIL:
                    folderName = "rail/";
                    break;
                case Route.TYPE_FERRY:
                    folderName = "ferry/";
                    break;
                case Route.TYPE_TRAM:
                    folderName = "lightrail/";
                    break;
                default:
                    return;
            }

            final List<Trip> tripsForType = typeToTripMap.get(type);

            final Set<Integer> stopIds = new HashSet<>();
            tripsForType.forEach(trip -> {
                trip.getStops().forEach(stopTime -> {
                    stopIds.add(stopTime.getStopId());
                });
            });

            Set<GraphEdge> edges = new HashSet<>();
            tripsForType.forEach(trip -> {
                for (int i = 0, len = trip.getStops().size(); i < len; i++) {
                    final StopTime current = trip.getStops().get(i);
                    final StopTime prev = i == 0 ? null : trip.getStops().get(i - 1);

                    if (prev != null) {
                        final int from = prev.getStopId();
                        final int to = current.getStopId();
                        final int cost = current.getTime() - prev.getTime();

                        edges.add(new GraphEdge(from, to, cost));
                    }
                }
            });

            final Map<Integer, Stop> stopIdToStop
                    = stops.stream().collect(toMap(stop -> stop.getId(), stop -> stop));


            final Set<GraphEdge> graph = edges.stream()
                    .map(edge -> edgeFromParents(edge, stopIdToStop))
                    .collect(toSet());

            // Add parent stops
            stopIds.addAll(
                    stops.parallelStream()
                            .filter(s -> s.getParentStopId() > 0)
                            .filter(s -> stopIds.contains(s.getId()))
                            .mapToInt(Stop::getParentStopId)
                            .boxed()
                            .collect(toList())
            );

            final List<Stop> stopsForType = stops.parallelStream()
                    .filter(stop -> stopIds.contains(stop.getId()))
                    .collect(toList());

            stopsForType.forEach(s -> s.setStopType(type));

            new CSVOutput(databaseFilePath + folderName)
                    .persist(tripsForType, stopsForType, graph);
        });
    }

    private GraphEdge edgeFromParents(GraphEdge edge, Map<Integer, Stop> stopIdToStop) {
        final Stop from = stopIdToStop.get(edge.getFrom());
        final Stop to = stopIdToStop.get(edge.getTo());

        final int fromIdToUse = from == null || from.getParentStopId() <= 0 ?
                edge.getFrom() : from.getParentStopId();
        final int toIdToUse = to == null || to.getParentStopId() <= 0 ?
                edge.getTo() : to.getParentStopId();

        return new GraphEdge(fromIdToUse, toIdToUse, edge.getCost());
    }

    private GtfsFileWithStagingModels processGtfsFile(Pair<File, GtfsFile> fileAndGtfsFile) {
        final File file = fileAndGtfsFile.getLeft();
        final GtfsFile gtfsFile = fileAndGtfsFile.getRight();

        LOG.info(gtfsFile + " - Processing");

        final IParser<? extends BaseGtfsModel> parser
                = ParserFactory.getParser(gtfsFile.getModelClass());
        List<? extends BaseGtfsModel> models = parser.parseRows(file);

        LOG.info(gtfsFile + " - Finished processing ");

        try {
            LOG.info(gtfsFile + " - staging transform ");
            final List<BaseStagingModel> stagingModels = models.parallelStream()
                    .map(StagingConverter::convert)
                    .filter(Objects::nonNull)
                    .map(object -> (BaseStagingModel) object)
                    .collect(toList());
            return new GtfsFileWithStagingModels(gtfsFile, stagingModels);
        } finally {
            LOG.info(gtfsFile + " - finished staging transform ");
        }
    }

    private static void fatalError(String message) {
        throw new BadConfigurationException(message);
    }

    private class GtfsFileWithStagingModels {
        final GtfsFile gtfsFile;

        final List<BaseStagingModel> models;

        private GtfsFileWithStagingModels(GtfsFile gtfsFile, List<BaseStagingModel> models) {
            this.gtfsFile = gtfsFile;
            this.models = models;
        }

        public GtfsFile getFile() {
            return gtfsFile;
        }
    }
}
