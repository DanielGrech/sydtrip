package com.dgsd.sydtrip.transformer.gtfs.model.target;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingAgency;

import org.apache.commons.lang3.StringUtils;

public class Agency {

    private final int id;

    private final String name;

    public Agency(GtfsStagingAgency agency) {
        this.id = agency.getId();
        this.name = StringUtils.isEmpty(agency.getName()) ? null : agency.getName();
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
