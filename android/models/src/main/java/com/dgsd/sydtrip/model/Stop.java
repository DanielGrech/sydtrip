package com.dgsd.sydtrip.model;

public final class Stop extends BaseModel {

    public enum StopType {
        RAIL, BUS, LIGHTRAIL, FERRY, UNKNOWN;
    }

    private final int id;

    private final String code;

    private final String name;

    private final float lat;

    private final float lng;

    private final String platformCode;

    private final int parentId;

    private final StopType stopType;

    public Stop(int id, String code, String name, float lat, float lng,
                String platformCode, int parentId, StopType stopType) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.platformCode = platformCode;
        this.parentId = parentId;
        this.stopType = stopType;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public int getParentId() {
        return parentId;
    }

    public StopType getStopType() {
        return stopType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        final Stop stop = (Stop) o;
        return this.id == stop.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
