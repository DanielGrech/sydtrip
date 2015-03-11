package com.dgsd.sydtrip.transformer.gtfs.model.source;

public class GtfsStop extends BaseGtfsModel {

    private String id;

    private String code;

    private String name;

    private String lat;

    private String lng;

    private String locationType;

    private String parentStation;

    private String wheelchairBoarding;

    private String platformCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLocationType() {
        return locationType;
    }

    public void setLocationType(String locationType) {
        this.locationType = locationType;
    }

    public String getParentStation() {
        return parentStation;
    }

    public void setParentStation(String parentStation) {
        this.parentStation = parentStation;
    }

    public String getWheelchairBoarding() {
        return wheelchairBoarding;
    }

    public void setWheelchairBoarding(String wheelchairBoarding) {
        this.wheelchairBoarding = wheelchairBoarding;
    }

    public String getPlatformCode() {
        return platformCode;
    }

    public void setPlatformCode(String platformCode) {
        this.platformCode = platformCode;
    }

    @Override
    public String toString() {
        return "Stop{" +
                "id='" + id + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", lat='" + lat + '\'' +
                ", lng='" + lng + '\'' +
                ", locationType='" + locationType + '\'' +
                ", parentStation='" + parentStation + '\'' +
                ", wheelchairBoarding='" + wheelchairBoarding + '\'' +
                ", platformCode='" + platformCode + '\'' +
                '}';
    }
}
