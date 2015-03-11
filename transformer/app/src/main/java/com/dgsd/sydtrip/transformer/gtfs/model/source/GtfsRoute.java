package com.dgsd.sydtrip.transformer.gtfs.model.source;

public class GtfsRoute extends BaseGtfsModel {

    private String id;

    private String agencyId;

    private String shortName;

    private String longName;

    private String desc;

    private String type;

    private String color;

    private String textColor;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAgencyId() {
        return agencyId;
    }

    public void setAgencyId(String agencyId) {
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public String toString() {
        return "Route{" +
                "id='" + id + '\'' +
                ", agencyId='" + agencyId + '\'' +
                ", shortName='" + shortName + '\'' +
                ", longName='" + longName + '\'' +
                ", desc='" + desc + '\'' +
                ", type='" + type + '\'' +
                ", color='" + color + '\'' +
                ", textColor='" + textColor + '\'' +
                '}';
    }
}
