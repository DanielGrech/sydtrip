package com.dgsd.android.data.model;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;

import static com.dgsd.android.data.model.ModelUtils.assertImport;
import static com.dgsd.android.data.model.ModelUtils.getFloat;
import static com.dgsd.android.data.model.ModelUtils.getInt;

public class DbStop extends RealmObject {

    @Ignore
    public static final int TYPE_TRAM = 0;

    @Ignore
    public static final int TYPE_RAIL = 2;

    @Ignore
    public static final int TYPE_BUS = 3;

    @Ignore
    public static final int TYPE_FERRY = 4;

    private int id;

    private String code;

    private String name;

    private float lat;

    private float lng;

    private int type;

    private int parentId;

    private String platformCode;

    public DbStop() {
    }

    public DbStop(String[] csvImport) {
        assertImport(csvImport, 8);

        id = getInt(csvImport[0]);
        code = csvImport[1];
        name = csvImport[2];
        lat = getFloat(csvImport[3]);
        lng = getFloat(csvImport[4]);
        type = getInt(csvImport[5]);
        parentId = getInt(csvImport[6]);
        platformCode = csvImport[7];
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLng() {
        return lng;
    }

    public void setLng(float lng) {
        this.lng = lng;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }
}
