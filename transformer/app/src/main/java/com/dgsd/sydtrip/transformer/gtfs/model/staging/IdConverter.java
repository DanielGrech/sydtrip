package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

class IdConverter {

    private static class CacheInfo {
        AtomicInteger idCounter;
        HashMap<String, Integer> cache;

        CacheInfo() {
            idCounter = new AtomicInteger(1);
            cache = new HashMap<>();
        }

        synchronized int convertId(String input) {
            if (StringUtils.isEmpty(input)) {
                return 0;
            }

            final Integer convertedValue = cache.get(input);
            if (convertedValue == null) {
                synchronized (idCounter) {
                    final int newId = idCounter.incrementAndGet();
                    cache.put(input, newId);
                    return newId;
                }
            } else {
                return convertedValue;
            }
        }
    }

    private CacheInfo serviceIdCache;
    private CacheInfo agencyIdCache;
    private CacheInfo routeIdCache;
    private CacheInfo shapeIdCache;
    private CacheInfo tripIdCache;
    private CacheInfo stopIdCache;

    private static IdConverter instance;

    public static int convertServiceId(String serviceId) {
        ensureInstance();
        return instance.serviceIdCache.convertId(serviceId);
    }

    public static int convertAgencyId(String agencyId) {
        ensureInstance();
        return instance.agencyIdCache.convertId(agencyId);
    }

    public static int convertRouteId(String routeId) {
        ensureInstance();
        return instance.routeIdCache.convertId(routeId);
    }

    public static int convertShapeId(String shapeId) {
        ensureInstance();
        return instance.shapeIdCache.convertId(shapeId);
    }

    public static int convertTripId(String tripId) {
        ensureInstance();
        return instance.tripIdCache.convertId(tripId);
    }

    public static int convertStopId(String stopId) {
        ensureInstance();
        return instance.stopIdCache.convertId(stopId);
    }

    private static void ensureInstance() {
        if (instance == null) {
            instance = new IdConverter();
        }
    }

    private IdConverter() {
        serviceIdCache = new CacheInfo();
        agencyIdCache = new CacheInfo();
        routeIdCache = new CacheInfo();
        shapeIdCache = new CacheInfo();
        tripIdCache = new CacheInfo();
        stopIdCache = new CacheInfo();
    }
}
