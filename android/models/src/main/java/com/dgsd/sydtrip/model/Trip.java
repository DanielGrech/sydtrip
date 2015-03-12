package com.dgsd.sydtrip.model;

public final class Trip extends BaseModel {

    public enum Direction {
        // TODO!
    }

    private final int id;

    private final String headSign;

    private final Direction direction;

    private final String blockId;

    private final boolean wheelchairAccess;

    private final int routeId;

    private final int stopTimeId;

    public Trip(int id, String headSign, Direction direction, String blockId, boolean wheelchairAccess, int routeId, int stopTimeId) {
        this.id = id;
        this.headSign = headSign;
        this.direction = direction;
        this.blockId = blockId;
        this.wheelchairAccess = wheelchairAccess;
        this.routeId = routeId;
        this.stopTimeId = stopTimeId;
    }

    public int getId() {
        return id;
    }

    public String getHeadSign() {
        return headSign;
    }

    public Direction getDirection() {
        return direction;
    }

    public String getBlockId() {
        return blockId;
    }

    public boolean isWheelchairAccess() {
        return wheelchairAccess;
    }

    public int getRouteId() {
        return routeId;
    }

    public int getStopTimeId() {
        return stopTimeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        final Trip trip = (Trip) o;
        return this.id == trip.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
