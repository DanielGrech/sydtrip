package com.dgsd.sydtrip.transformer.gtfs.model.target;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStop;

import org.apache.commons.lang3.StringUtils;

public class Stop {

    private final int id;

    private final String code;

    private final String name;

    private final float lat;

    private final float lng;

    private int type;

    private final int parentStopId;

    private final boolean wheelchairAccessible;

    private final String platformCode;

    public Stop(GtfsStagingStop stop) {
        id = stop.getId();
        code = stop.getCode();
        name = StringUtils.isEmpty(stop.getName()) ? null : stop.getName().trim();
        lat = stop.getLat();
        lng = stop.getLng();
        type = stop.getType();
        parentStopId = stop.getParentStopId();
        wheelchairAccessible = stop.isWheelchairAccessible();
        platformCode = stop.getPlatformCode();
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

    public int getType() {
        return type;
    }

    public int getParentStopId() {
        return parentStopId;
    }

    public boolean isWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setStopType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", type=" + type +
                ", parentStopId=" + parentStopId +
                ", wheelchairAccessible=" + wheelchairAccessible +
                ", platformCode='" + platformCode + '\'' +
                '}';
    }
}