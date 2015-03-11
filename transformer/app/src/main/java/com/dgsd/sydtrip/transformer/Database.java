package com.dgsd.sydtrip.transformer;

import com.dgsd.sydtrip.transformer.exception.DatabaseCreationException;
import com.dgsd.sydtrip.transformer.exception.DatabaseOperationException;
import com.dgsd.sydtrip.transformer.gtfs.model.target.CalendarInformation;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Stop;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Trip;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

public class Database implements AutoCloseable {

    private final static Logger LOG = Logger.getLogger(Database.class.getName());

    private static final String STOP_INSERT_TEMPLATE
            = "INSERT OR REPLACE INTO stops VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String TRIP_INSERT_TEMPLATE
            = "INSERT OR REPLACE INTO trips VALUES(?, ?, ?, ?, ?, ?)";
    private static final String ROUTE_INSERT_TEMPLATE
            = "INSERT OR REPLACE INTO routes VALUES(?, ?, ?, ?, ?)";
    private static final String STOP_TIME_INSERT_TEMPLATE
            = "INSERT OR REPLACE INTO stop_times VALUES(?, ?, ?)";
    private static final String DYNAMIC_TEXT_INSERT_TEMPLATE
            = "INSERT OR REPLACE INTO dynamic_text VALUES(?, ?)";
    private static final String CAL_INFO_INSERT_TEMPLATE
            = "INSERT OR REPLACE INTO calendar_info VALUES(?, ?, ?, ?)";
    private static final String CAL_INFO_EX_INSERT_TEMPLATE
            = "INSERT OR REPLACE INTO calendar_info_ex VALUES(?, ?, ?)";

    private final Connection connection;

    private final String fileName;

    private final Map<String, Integer> dynamicTextCache;

    private final AtomicInteger dynamicTextGenerator;

    public Database(String fileName) {
        this.fileName = fileName;
        this.dynamicTextCache = new LinkedHashMap<>();
        this.dynamicTextGenerator = new AtomicInteger(1);
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseCreationException(e);
        }
    }

    public void create() {
        try (final Statement statement = connection.createStatement()) {

            LOG.info(String.format("[Creating %s]", fileName));

            LOG.info("Creating dynamic text table");
            statement.execute(loadResource("sql/table_dynamic_text.sql"));

            LOG.info("Creating calendar info table");
            statement.execute(loadResource("sql/table_calendar_info.sql"));

            LOG.info("Creating calendar info exceptions table");
            statement.execute(loadResource("sql/table_calendar_info_ex.sql"));

            LOG.info("Creating stops table");
            statement.execute(loadResource("sql/table_stops.sql"));

            LOG.info("Creating stop times table");
            statement.execute(loadResource("sql/table_stop_times.sql"));

            LOG.info("Creating trips table");
            statement.execute(loadResource("sql/table_trips.sql"));

            LOG.info("Creating routes table");
            statement.execute(loadResource("sql/table_routes.sql"));

            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException(e);
        }
    }

    public void compressTo(String outputFile) {
        try {
            final Process process = new ProcessBuilder("sqlite3", fileName, ".dump").start();
            final InputStream isr = new BufferedInputStream(process.getInputStream());

            LZMA2Options options = new LZMA2Options();
            options.setPreset(9);

            try (final XZOutputStream out
                         = new XZOutputStream(new FileOutputStream(outputFile), options)) {
                final byte[] buf = new byte[8192];
                int size;
                while ((size = isr.read(buf)) != -1) {
                    out.write(buf, 0, size);
                }

                out.finish();
            }
        } catch (IOException ex) {
            throw new DatabaseOperationException(ex);
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new DatabaseOperationException(e);
        }
    }

    public void persist(List<Trip> trips, List<Stop> stops) {
        try {
            persistStops(stops);
            persistTrips(trips);
            persistDynamicText();
            connection.commit();
        } catch (SQLException e) {
            throw new DatabaseOperationException(e);
        }
    }

    private void persistDynamicText() throws SQLException {
        try (final PreparedStatement statement
                     = connection.prepareStatement(DYNAMIC_TEXT_INSERT_TEMPLATE)) {
            for (Map.Entry<String, Integer> entry : dynamicTextCache.entrySet()) {
                setInt(statement, 1, entry.getValue());
                statement.setString(2, entry.getKey());
                statement.addBatch();
            }

            statement.executeBatch();
        }
    }

    private void persistTrips(List<Trip> trips) throws SQLException {
        try (final PreparedStatement tripStatement = connection.prepareStatement(TRIP_INSERT_TEMPLATE)) {
            try (final PreparedStatement routeStatement = connection.prepareStatement(ROUTE_INSERT_TEMPLATE)) {
                try (final PreparedStatement stopTimeStatement
                             = connection.prepareStatement(STOP_TIME_INSERT_TEMPLATE)) {
                    try (final PreparedStatement calInfoStatement
                                 = connection.prepareStatement(CAL_INFO_INSERT_TEMPLATE)) {
                        try (final PreparedStatement calInfoExceptionStatement
                                     = connection.prepareStatement(CAL_INFO_EX_INSERT_TEMPLATE)) {
                            for (Trip trip : trips) {
                                int tripIdx = 1;
                                setInt(tripStatement, tripIdx++, trip.getId());
                                setInt(tripStatement, tripIdx++, getDynamicStringId(trip.getHeadSign()));
                                setInt(tripStatement, tripIdx++, trip.getDirection());
                                tripStatement.setString(tripIdx++, trip.getBlockId());
                                setInt(tripStatement, tripIdx++, trip.isWheelchairAccessible() ? 1 : 0);

                                final Route route = trip.getRoute();
                                if (route == null) {
                                    setInt(tripStatement, tripIdx++, 0);
                                } else {
                                    setInt(tripStatement, tripIdx++, route.getId());

                                    int routeIdx = 1;
                                    setInt(routeStatement, routeIdx++, route.getId());
                                    setInt(routeStatement, routeIdx++, route.getAgencyId());
                                    setInt(routeStatement, routeIdx++,
                                            getDynamicStringId(route.getShortName()));
                                    setInt(routeStatement, routeIdx++,
                                            getDynamicStringId(route.getLongName()));
                                    setInt(routeStatement, routeIdx++, route.getColor());
                                    routeStatement.addBatch();
                                }

                                final List<StopTime> stopTimes = trip.getStops();
                                if (stopTimes != null) {
                                    for (StopTime stopTime : stopTimes) {
                                        int stopTimeIdx = 1;
                                        setInt(stopTimeStatement, stopTimeIdx++, trip.getId());
                                        setInt(stopTimeStatement, stopTimeIdx++, stopTime.getStopId());
                                        setInt(stopTimeStatement, stopTimeIdx++, stopTime.getTime());
                                        stopTimeStatement.addBatch();
                                    }
                                }

                                final CalendarInformation calInfo = trip.getCalendarInformation();
                                if (calInfo != null) {
                                    int calInfoIdx = 1;
                                    setInt(calInfoStatement, calInfoIdx++, trip.getId());
                                    setInt(calInfoStatement, calInfoIdx++, calInfo.getStartJulianDate());
                                    setInt(calInfoStatement, calInfoIdx++, calInfo.getEndJulianDate());
                                    setInt(calInfoStatement, calInfoIdx++, calInfo.getAvailabilityBitmask());

                                    final Map<Integer, Integer> exceptions
                                            = calInfo.getJulianDayToExceptionMap();
                                    if (exceptions != null) {
                                        for (Map.Entry<Integer, Integer> entry : exceptions.entrySet()) {
                                            int calInfoExceptionIdx = 1;
                                            setInt(calInfoExceptionStatement,
                                                    calInfoExceptionIdx++, trip.getId());
                                            setInt(calInfoExceptionStatement,
                                                    calInfoExceptionIdx++, entry.getKey());
                                            setInt(calInfoExceptionStatement,
                                                    calInfoExceptionIdx++, entry.getValue());

                                            calInfoExceptionStatement.addBatch();
                                        }
                                    }


                                    calInfoStatement.addBatch();
                                }

                                tripStatement.addBatch();
                            }

                            calInfoStatement.executeBatch();
                            calInfoExceptionStatement.executeBatch();
                            stopTimeStatement.executeBatch();
                            routeStatement.executeBatch();
                            tripStatement.executeBatch();
                        }
                    }
                }
            }
        }
    }

    private void persistStops(List<Stop> stops) throws SQLException {
        LOG.info("Persisting stops...");
        try (final PreparedStatement statement = connection.prepareStatement(STOP_INSERT_TEMPLATE)) {
            for (Stop stop : stops) {
                int idx = 1;
                setInt(statement, idx++, stop.getId());
                statement.setString(idx++, stop.getCode());
                setInt(statement, idx++, getDynamicStringId(stop.getName()));
                statement.setFloat(idx++, stop.getLat());
                statement.setFloat(idx++, stop.getLng());
                setInt(statement, idx++, stop.getType());
                setInt(statement, idx++, stop.getParentStopId());
                statement.setString(idx++, stop.getPlatformCode());

                statement.addBatch();
            }

            statement.executeBatch();
        }
        LOG.info("Finished persisting stops...");
    }

    private int getDynamicStringId(String text) {
        if (StringUtils.isEmpty(text)) {
            return 0;
        }

        final Integer id = dynamicTextCache.get(text);
        if (id == null) {
            final int retval = dynamicTextGenerator.incrementAndGet();
            dynamicTextCache.put(text, retval);
            return retval;
        } else {
            return id;
        }
    }

    private static String loadResource(final String path) {
        try (InputStream stream
                     = Thread.currentThread().getContextClassLoader().getResourceAsStream(path)) {
            return IOUtils.toString(stream);
        } catch (final IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void setInt(PreparedStatement pStatement, int pos, int value) throws SQLException {
        if (value == 0) {
            pStatement.setNull(pos, Types.INTEGER);
        } else {
            pStatement.setInt(pos, value);
        }
    }
}
