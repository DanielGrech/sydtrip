package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsAgency;
import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendar;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendarDate;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsRoute;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsShape;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStop;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsTrip;

import java.util.HashMap;
import java.util.Map;

public class ParserFactory {

    private static Map<Class, IParser> classToParserMap = new HashMap<>();

    public static <T extends BaseGtfsModel> IParser<T> getParser(Class<T> cls) {
        if (classToParserMap.containsKey(cls)) {
            return classToParserMap.get(cls);
        }

        final IParser parser;
        if (cls.equals(GtfsAgency.class)) {
            parser = new AgencyParser();
        } else if (cls.equals(GtfsStop.class)) {
            parser = new StopParser();
        } else if (cls.equals(GtfsShape.class)) {
            parser = new ShapeParser();
        } else if (cls.equals(GtfsTrip.class)) {
            parser = new TripParser();
        } else if (cls.equals(GtfsRoute.class)) {
            parser = new RouteParser();
        } else if (cls.equals(GtfsStopTime.class)) {
            parser = new StopTimeParser();
        } else if (cls.equals(GtfsCalendar.class)) {
            parser = new CalendarParser();
        } else if (cls.equals(GtfsCalendarDate.class)) {
            parser = new CalendarDateParser();
        } else {
            throw new IllegalArgumentException("Unknown class: " + cls);
        }

        return (IParser<T>) parser;
    }
}
