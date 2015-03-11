package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsTrip;

public class GtfsStagingTrip extends BaseStagingModel<GtfsTrip> {

    private final int id;

    private final int routeId;

    private final int serviceId;

    private final int shapeId;

    private final String headSign;

    private final int direction;

    private final String blockId;

    private final boolean wheelchairAccessible;

    public GtfsStagingTrip(GtfsTrip gtfsObject) {
        super(gtfsObject);

        id = IdConverter.convertTripId(gtfsObject.getId());
        routeId = IdConverter.convertRouteId(gtfsObject.getRouteId());
        serviceId = IdConverter.convertServiceId(gtfsObject.getServiceId());
        shapeId = IdConverter.convertShapeId(gtfsObject.getShapeId());
        headSign = gtfsObject.getTripHeadsign();
        direction = safeParseInt(gtfsObject.getDirectionId(), 0);
        blockId = gtfsObject.getBlockId();
        wheelchairAccessible = safeParseInt(gtfsObject.getWheelchairAccessible(), 0) == 1;
    }

    public int getId() {
        return id;
    }

    public int getRouteId() {
        return routeId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public int getShapeId() {
        return shapeId;
    }

    public String getHeadSign() {
        return headSign;
    }

    public int getDirection() {
        return direction;
    }

    public String getBlockId() {
        return blockId;
    }

    public boolean isWheelchairAccessible() {
        return wheelchairAccessible;
    }

    @Override
    public String toString() {
        return "GtfsStagingTrip{" +
                "id=" + id +
                ", routeId=" + routeId +
                ", serviceId=" + serviceId +
                ", shapeId=" + shapeId +
                ", headSign='" + headSign + '\'' +
                ", direction=" + direction +
                ", blockId='" + blockId + '\'' +
                ", wheelchairAccessible=" + wheelchairAccessible +
                '}';
    }
}
