package com.dgsd.sydtrip.updater.model;

import com.google.gson.annotations.SerializedName;

public class ManifestFile {

    public static final String TYPE_RAIL = "RAIL";
    public static final String TYPE_FERRY = "FERRY";
    public static final String TYPE_LIGHTRAIL = "LIGHTRAIL";
    public static final String TYPE_BUS = "BUS";

    @SerializedName("url")
    private String urlPath;

    @SerializedName("type")
    private String type;

    public String getUrlPath() {
        return urlPath;
    }

    public String getType() {
        return type;
    }

    public boolean isRail() {
        return TYPE_RAIL.equals(type);
    }

    public boolean isBus() {
        return TYPE_BUS.equals(type);
    }

    public boolean isFerry() {
        return TYPE_FERRY.equals(type);
    }

    public boolean isLightrail() {
        return TYPE_LIGHTRAIL.equals(type);
    }

    public boolean isEnabled() {
        return isRail();
    }
}
