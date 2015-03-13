package com.dgsd.android.data.model;

import io.realm.RealmObject;

import static com.dgsd.android.data.model.ModelUtils.assertImport;
import static com.dgsd.android.data.model.ModelUtils.getInt;

@SuppressWarnings("unused")
public class DbGraphEdge extends RealmObject {

    private int fromStop;

    private int toStop;

    private int cost;

    public DbGraphEdge() {

    }

    public DbGraphEdge(String[] csvImport) {
        assertImport(csvImport, 3);
        fromStop = getInt(csvImport[0]);
        toStop = getInt(csvImport[1]);
        cost = getInt(csvImport[2]);
    }

    public int getFromStop() {
        return fromStop;
    }

    public void setFromStop(int fromStop) {
        this.fromStop = fromStop;
    }

    public int getToStop() {
        return toStop;
    }

    public void setToStop(int toStop) {
        this.toStop = toStop;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
