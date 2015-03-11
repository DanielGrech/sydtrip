package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsStop;

public class GtfsStagingStop extends BaseStagingModel<GtfsStop> {

    private final int id;

    private final String code;

    private final String name;

    private final float lat;

    private final float lng;

    private final int type;

    private final int parentStopId;

    private final boolean wheelchairAccessible;

    private final String platformCode;

    public GtfsStagingStop(GtfsStop gtfsObject) {
        super(gtfsObject);

        id = IdConverter.convertStopId(gtfsObject.getId());
        code = gtfsObject.getCode();
        name = gtfsObject.getName();
        lat = safeParseFloat(gtfsObject.getLat(), Integer.MAX_VALUE);
        lng = safeParseFloat(gtfsObject.getLng(), Integer.MAX_VALUE);
        type = safeParseInt(gtfsObject.getLocationType(), 0);
        parentStopId = IdConverter.convertStopId(gtfsObject.getParentStation());
        wheelchairAccessible = safeParseInt(gtfsObject.getWheelchairBoarding(), 0) == 1;
        platformCode = gtfsObject.getPlatformCode();
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

    @Override
    public String toString() {
        return "GtfsStagingStop{" +
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
