package com.dgsd.sydtrip.transformer.gtfs;

import com.dgsd.sydtrip.transformer.exception.StagingTransformationException;
import com.dgsd.sydtrip.transformer.gtfs.model.source.BaseGtfsModel;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsAgency;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendar;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsCalendarDate;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsRoute;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsShape;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStop;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsTrip;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.BaseStagingModel;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingAgency;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendar;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingCalendarDate;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingRoute;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingShape;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStop;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingTrip;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

public class StagingConverter {

    private final static Logger LOG = Logger.getLogger(StagingConverter.class.getName());

    private static final HashMap<Class, Constructor> constructorCache
            = new HashMap<>();

    public static List<? extends BaseStagingModel> processGtfsFile(GtfsFile gtfsFile,
                                                             List<? extends BaseGtfsModel> models) {
        try {
            LOG.info(gtfsFile + " - staging transform ");
            return models.parallelStream()
                    .map(StagingConverter::convert)
                    .filter(Objects::nonNull)
                    .map(object -> (BaseStagingModel) object)
                    .collect(toList());
        } finally {
            LOG.info(gtfsFile + " - finished staging transform ");
        }
    }

    public static <T extends BaseGtfsModel, S extends BaseStagingModel<T>> S convert(T model) {
        final Class<T> cls = (Class<T>) model.getClass();
        if (cls.equals(GtfsAgency.class)) {
            return (S) StagingConverter.convert((GtfsAgency) model, GtfsStagingAgency.class);
        } else if(cls.equals(GtfsCalendar.class)) {
            return (S) StagingConverter.convert((GtfsCalendar) model, GtfsStagingCalendar.class);
        } else if(cls.equals(GtfsCalendarDate.class)) {
            return (S) StagingConverter.convert((GtfsCalendarDate) model, GtfsStagingCalendarDate.class);
        } else if(cls.equals(GtfsRoute.class)) {
            return (S) StagingConverter.convert((GtfsRoute) model, GtfsStagingRoute.class);
        } else if(cls.equals(GtfsShape.class)) {
            return (S) StagingConverter.convert((GtfsShape) model, GtfsStagingShape.class);
        } else if(cls.equals(GtfsStop.class)) {
            return (S) StagingConverter.convert((GtfsStop) model, GtfsStagingStop.class);
        } else if(cls.equals(GtfsStopTime.class)) {
            return (S) StagingConverter.convert((GtfsStopTime) model, GtfsStagingStopTime.class);
        } else if(cls.equals(GtfsTrip.class)) {
            return (S) StagingConverter.convert((GtfsTrip) model, GtfsStagingTrip.class);
        }

        return null;
    }

    private static <T extends BaseGtfsModel, S extends BaseStagingModel<T>>
    S convert(T input, Class<S> cls) {
        try {
            Constructor<S> constructor = constructorCache.get(input.getClass());
            if (constructor == null) {
                constructor = cls.getConstructor(input.getClass());
                constructorCache.put(input.getClass(), constructor);
            }

            return constructor.newInstance(input);
        } catch (Exception ex) {
            throw new StagingTransformationException(ex);
        }
    }
}
