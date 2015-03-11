package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsRoute;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Route;

public class GtfsStagingRoute extends BaseStagingModel<GtfsRoute> {

    private final int id;

    private final int agencyId;

    private final String shortName;

    private final String longName;

    private final String desc;

    private final int routeType;

    private final int color;

    public GtfsStagingRoute(GtfsRoute gtfsObject) {
        super(gtfsObject);

        id = IdConverter.convertRouteId(gtfsObject.getId());
        agencyId = IdConverter.convertAgencyId(gtfsObject.getAgencyId());
        shortName = gtfsObject.getShortName();
        longName = gtfsObject.getLongName();
        desc = gtfsObject.getDesc();
        routeType = safeParseInt(gtfsObject.getType(), -1);
        color = safeParseInt(gtfsObject.getColor(), 16, 0xFFFFFF);
    }

    public int getId() {
        return id;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public String getDesc() {
        return desc;
    }

    public int getRouteType() {
        return routeType;
    }

    public int getColor() {
        return color;
    }

    @Override
    public String toString() {
        return "GtfsStagingRoute{" +
                "id=" + id +
                ", agencyId=" + agencyId +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", desc='" + desc + '\'' +
                ", routeType=" + routeType +
                ", color=" + color +
                '}';
    }
}
