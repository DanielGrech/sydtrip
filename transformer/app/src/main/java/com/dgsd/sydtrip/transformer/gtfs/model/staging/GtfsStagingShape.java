package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsShape;

import java.util.Comparator;

public class GtfsStagingShape extends BaseStagingModel<GtfsShape> {

    private final int id;

    private final float lat;

    private final float lng;

    private final int sequence;

    private final float distTravelled;

    public GtfsStagingShape(GtfsShape gtfsObject) {
        super(gtfsObject);

        id = IdConverter.convertShapeId(gtfsObject.getId());
        lat = safeParseFloat(gtfsObject.getPtLat(), Integer.MAX_VALUE);
        lng = safeParseFloat(gtfsObject.getPtLng(), Integer.MAX_VALUE);
        sequence = safeParseInt(gtfsObject.getPtSequence(), Integer.MAX_VALUE);
        distTravelled = safeParseFloat(gtfsObject.getDistTravelled(), 0);
    }

    public int getId() {
        return id;
    }

    public float getLat() {
        return lat;
    }

    public float getLng() {
        return lng;
    }

    public int getSequence() {
        return sequence;
    }

    public float getDistTravelled() {
        return distTravelled;
    }

    @Override
    public String toString() {
        return "GtfsStagingShape{" +
                "id=" + id +
                ", lat=" + lat +
                ", lng=" + lng +
                ", sequence=" + sequence +
                ", distTravelled=" + distTravelled +
                '}';
    }

    public static Comparator<GtfsStagingShape> SORT_BY_SEQUENCE = new Comparator<GtfsStagingShape>() {
        @Override
        public int compare(GtfsStagingShape lhs, GtfsStagingShape rhs) {
            return rhs.sequence - lhs.sequence;
        }
    };
}
