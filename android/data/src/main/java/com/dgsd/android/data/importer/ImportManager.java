package com.dgsd.android.data.importer;

import com.dgsd.android.data.DataSource;
import com.dgsd.android.data.model.DbCalendarInfo;
import com.dgsd.android.data.model.DbCalendarInfoEx;
import com.dgsd.android.data.model.DbRoute;
import com.dgsd.android.data.model.DbStop;
import com.dgsd.android.data.model.DbStopTime;
import com.dgsd.android.data.model.DbTrip;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Action2;
import rx.functions.Func0;
import rx.functions.Func1;

public class ImportManager {

    public static class ExpectedFiles {
        public static final String CAL_INFO_EX = "cal_info_ex.csv";
        public static final String CAL_INFO = "cal_info.csv";
        public static final String ROUTES = "routes.csv";
        public static final String STOP_TIMES = "stop_times.csv";
        public static final String STOPS = "stops.csv";
        public static final String TRIPS = "trips.csv";

        public static final List<String> ALL = Arrays.asList(
                CAL_INFO, CAL_INFO_EX, ROUTES, STOP_TIMES, STOPS, TRIPS
        );
    }

    private final DataSource dataSource;

    public ImportManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Observable<List<String>> asObservable(String rootDirPath) {
        final File rootDir = new File(rootDirPath);
        return Observable.from(rootDir.listFiles())
                .filter(new Func1<File, Boolean>() {
                    @Override
                    public Boolean call(File file) {
                        return ExpectedFiles.ALL.contains(file.getName());
                    }
                }).doOnNext(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        try {
                            importFile(file);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }).collect(new Func0<List<String>>() {
                    @Override
                    public List<String> call() {
                        return new LinkedList<>();
                    }
                }, new Action2<List<String>, File>() {
                    @Override
                    public void call(List<String> files, File file) {
                        files.add(file.getAbsolutePath());
                    }
                });
    }

    private void importFile(File file) throws IOException {
        final CSVReader reader = new CSVReader(new FileReader(file));
        try {
            switch (file.getName()) {
                case ExpectedFiles.CAL_INFO_EX:
                    importCalInfoEx(reader);
                    break;
                case ExpectedFiles.CAL_INFO:
                    importCalInfo(reader);
                    break;
                case ExpectedFiles.ROUTES:
                    importRoutes(reader);
                    break;
                case ExpectedFiles.STOP_TIMES:
                    importStopTimes(reader);
                    break;
                case ExpectedFiles.STOPS:
                    importStops(reader);
                    break;
                case ExpectedFiles.TRIPS:
                    importTrips(reader);
                    break;
            }
        } finally {
            reader.close();
        }
    }

    private void importTrips(CSVReader reader) throws IOException {
        final List<DbTrip> models = new LinkedList<>();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            models.add(new DbTrip(nextLine));
        }

        dataSource.saveTrips(models);
    }

    private void importStops(CSVReader reader) throws IOException {
        final List<DbStop> models = new LinkedList<>();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            models.add(new DbStop(nextLine));
        }

        dataSource.saveStops(models);
    }

    private void importStopTimes(CSVReader reader) throws IOException {
        final List<DbStopTime> models = new LinkedList<>();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            models.add(new DbStopTime(nextLine));
        }

        dataSource.saveStopTimes(models);
    }

    private void importRoutes(CSVReader reader) throws IOException {
        final List<DbRoute> models = new LinkedList<>();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            models.add(new DbRoute(nextLine));
        }

        dataSource.saveRoutes(models);
    }

    private void importCalInfo(CSVReader reader) throws IOException {
        final List<DbCalendarInfo> models = new LinkedList<>();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            models.add(new DbCalendarInfo(nextLine));
        }

        dataSource.saveCalendarInfo(models);
    }

    private void importCalInfoEx(CSVReader reader) throws IOException {
        final List<DbCalendarInfoEx> models = new LinkedList<>();

        String[] nextLine;
        while ((nextLine = reader.readNext()) != null) {
            models.add(new DbCalendarInfoEx(nextLine));
        }

        dataSource.saveCalendarInfoEx(models);
    }
}
