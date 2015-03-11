package com.dgsd.sydtrip.model;

public final class Route extends BaseModel {

    private final int id;

    private final String shortName;

    private final String longName;

    private final int color;

    public Route(int id, String shortName, String longName, int color) {
        this.id = id;
        this.shortName = shortName;
        this.longName = longName;
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public String getShortName() {
        return shortName;
    }

    public String getLongName() {
        return longName;
    }

    public int getColor() {
        return color;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || o.getClass() != getClass()) {
            return false;
        }

        final Route route = (Route) o;
        return this.id == route.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
