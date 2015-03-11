package com.dgsd.sydtrip.transformer.gtfs.model.source;

public class GtfsShape extends BaseGtfsModel {
    private String id;

    private String ptLat;

    private String ptLng;

    private String ptSequence;

    private String distTravelled;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPtLat() {
        return ptLat;
    }

    public void setPtLat(String ptLat) {
        this.ptLat = ptLat;
    }

    public String getPtLng() {
        return ptLng;
    }

    public void setPtLng(String ptLng) {
        this.ptLng = ptLng;
    }

    public String getPtSequence() {
        return ptSequence;
    }

    public void setPtSequence(String ptSequence) {
        this.ptSequence = ptSequence;
    }

    public String getDistTravelled() {
        return distTravelled;
    }

    public void setDistTravelled(String distTravelled) {
        this.distTravelled = distTravelled;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "id='" + id + '\'' +
                ", ptLat='" + ptLat + '\'' +
                ", ptLng='" + ptLng + '\'' +
                ", ptSequence='" + ptSequence + '\'' +
                ", distTravelled='" + distTravelled + '\'' +
                '}';
    }
}
