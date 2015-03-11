package com.dgsd.sydtrip.transformer.gtfs;

import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsAgency;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendar;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendarDate;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsRoute;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsShape;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStop;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsTrip;

import java.util.Arrays;

public enum GtfsFile {

    AGENCY("agency.txt", GtfsAgency.class),
    STOPS("stops.txt", GtfsStop.class),
    SHAPES("shapes.txt", GtfsShape.class),
    TRIPS("trips.txt", GtfsTrip.class),
    ROUTES("routes.txt", GtfsRoute.class),
    STOP_TIMES("stop_times.txt", GtfsStopTime.class),
    CALENDAR("calendar.txt", GtfsCalendar.class),
    CALENDAR_DATES("calendar_dates.txt", GtfsCalendarDate.class);

    private final String fileName;

    private final Class<? extends BaseGtfsModel> modelClass;

    private GtfsFile(String fileName, Class modelClass) {
        this.fileName = fileName;
        this.modelClass = modelClass;
    }

    public static GtfsFile named(String fileName) {
        for (GtfsFile file : values()) {
            if (file.fileName.equals(fileName)) {
                return file;
            }
        }

        return null;
    }

    public Class<? extends BaseGtfsModel> getModelClass() {
        return modelClass;
    }

    public boolean enabled() {
        return !Arrays.asList(SHAPES, AGENCY).contains(this);
    }
}
