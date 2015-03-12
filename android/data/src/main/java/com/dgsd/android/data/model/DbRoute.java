package com.dgsd.android.data.model;

import io.realm.RealmObject;

import static com.dgsd.android.data.model.ModelUtils.assertImport;
import static com.dgsd.android.data.model.ModelUtils.getInt;

@SuppressWarnings("unused")
public class DbRoute extends RealmObject {

    private int id;

    private int agencyId;

    private String shortName;

    private String longName;

    private int color;

    public DbRoute() {
    }

    public DbRoute(String[] csvImport) {
        assertImport(csvImport, 5);

        id = getInt(csvImport[0]);
        agencyId = getInt(csvImport[1]);
        shortName = csvImport[2];
        longName = csvImport[3];
        color = getInt(csvImport[4]);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(int agencyId) {
        this.agencyId = agencyId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
