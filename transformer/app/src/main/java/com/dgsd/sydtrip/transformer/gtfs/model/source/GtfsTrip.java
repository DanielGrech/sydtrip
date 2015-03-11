package com.dgsd.sydtrip.transformer.gtfs.model.source;

public class GtfsTrip extends BaseGtfsModel {

    private String id;

    private String routeId;

    private String serviceId;

    private String shapeId;

    private String tripHeadsign;

    private String directionId;

    private String blockId;

    private String wheelchairAccessible;

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getShapeId() {
        return shapeId;
    }

    public void setShapeId(String shapeId) {
        this.shapeId = shapeId;
    }

    public String getTripHeadsign() {
        return tripHeadsign;
    }

    public void setTripHeadsign(String tripHeadsign) {
        this.tripHeadsign = tripHeadsign;
    }

    public String getDirectionId() {
        return directionId;
    }

    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public String getWheelchairAccessible() {
        return wheelchairAccessible;
    }

    public void setWheelchairAccessible(String wheelchairAccessible) {
        this.wheelchairAccessible = wheelchairAccessible;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id='" + id + '\'' +
                ", routeId='" + routeId + '\'' +
                ", serviceId='" + serviceId + '\'' +
                ", shapeId='" + shapeId + '\'' +
                ", tripHeadsign='" + tripHeadsign + '\'' +
                ", directionId='" + directionId + '\'' +
                ", blockId='" + blockId + '\'' +
                ", wheelchairAccessible='" + wheelchairAccessible + '\'' +
                '}';
    }
}
