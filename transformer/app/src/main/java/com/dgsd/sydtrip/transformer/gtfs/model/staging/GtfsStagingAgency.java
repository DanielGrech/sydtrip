package com.dgsd.sydtrip.transformer.gtfs.model.staging;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsAgency;

public class GtfsStagingAgency extends BaseStagingModel<GtfsAgency> {

    private final int id;

    private final String name;

    public GtfsStagingAgency(GtfsAgency gtfsObject) {
        super(gtfsObject);

        this.id = IdConverter.convertAgencyId(gtfsObject.getId());
        this.name = gtfsObject.getName();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "GtfsStagingAgency{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
