package com.dgsd.android.data.model;

import io.realm.RealmObject;

import static com.dgsd.android.data.model.ModelUtils.assertImport;
import static com.dgsd.android.data.model.ModelUtils.getInt;

public class DbTrip extends RealmObject {

    private int id;

    private String headSign;

    private int direction;

    private String blockId;

    private int wheelchairAccess;

    private int routeId;

    public DbTrip() {
        super();
    }

    public DbTrip(String[] csvImport) {
        assertImport(csvImport, 6);

        id = getInt(csvImport[0]);
        headSign = csvImport[1];
        direction = getInt(csvImport[2]);
        blockId = csvImport[3];
        wheelchairAccess = getInt(csvImport[4]);
        routeId = getInt(csvImport[5]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getHeadSign() {
        return headSign;
    }

    public void setHeadSign(String headSign) {
        this.headSign = headSign;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getBlockId() {
        return blockId;
    }

    public void setBlockId(String blockId) {
        this.blockId = blockId;
    }

    public int getWheelchairAccess() {
        return wheelchairAccess;
    }

    public void setWheelchairAccess(int wheelchairAccess) {
        this.wheelchairAccess = wheelchairAccess;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }
}
