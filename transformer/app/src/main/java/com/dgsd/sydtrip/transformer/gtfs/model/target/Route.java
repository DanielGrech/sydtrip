package com.dgsd.sydtrip.transformer.gtfs.model.target;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingRoute;

import org.apache.commons.lang3.StringUtils;

public class Route {

    public static final int TYPE_TRAM = 0;
    public static final int TYPE_SUBWAY = 1;
    public static final int TYPE_RAIL = 2;
    public static final int TYPE_BUS = 3;
    public static final int TYPE_FERRY = 4;
    public static final int TYPE_CABLE_CAR = 5;
    public static final int TYPE_GONDOLA = 6;
    public static final int TYPE_FUNICULAR = 7;

    private final int id;

    private final int agencyId;

    private final String shortName;

    private final String longName;

    private final int routeType;

    private final int color;

    public Route(GtfsStagingRoute route) {
        id = route.getId();
        agencyId = route.getAgencyId();
        shortName = StringUtils.isEmpty(route.getShortName()) ? "" : route.getShortName().trim();
        longName = StringUtils.isEmpty(route.getLongName()) ? "" : route.getLongName().trim();
        routeType = route.getRouteType();
        color = route.getColor();
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

    public int getRouteType() {
        return routeType;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Route route = (Route) o;

        if (agencyId != route.agencyId) return false;
        if (color != route.color) return false;
        if (routeType != route.routeType) return false;
        if (longName != null ? !longName.equals(route.longName) : route.longName != null)
            return false;
        if (shortName != null ? !shortName.equals(route.shortName) : route.shortName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = agencyId;
        result = 31 * result + (shortName != null ? shortName.hashCode() : 0);
        result = 31 * result + (longName != null ? longName.hashCode() : 0);
        result = 31 * result + routeType;
        result = 31 * result + color;
        return result;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id=" + id +
                ", agencyId=" + agencyId +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", routeType=" + routeType +
                ", color=" + color +
                '}';
    }
}
