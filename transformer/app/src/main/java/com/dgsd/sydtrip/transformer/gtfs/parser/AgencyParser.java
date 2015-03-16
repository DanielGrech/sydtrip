package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsAgency;

import java.util.List;

public class AgencyParser extends CsvParser<GtfsAgency> {

    private static final String KEY_AGENCY_ID = "agency_id";
    private static final String KEY_AGENCY_NAME = "agency_name";
    private static final String KEY_AGENCY_URL = "agency_url";
    private static final String KEY_AGENCY_PHONE = "agency_phone";

    @Override
    protected GtfsAgency create(List<String> fields, List<String> values) {
        final GtfsAgency agency = new GtfsAgency();

        for (int i = 0, len = fields.size(); i < len; i++) {
            final String key = fields.get(i);
            final String value = values.get(i);

            switch (key) {
                case KEY_AGENCY_ID:
                    agency.setId(value);
                    break;
                case KEY_AGENCY_NAME:
                    agency.setName(value);
                    break;
                case KEY_AGENCY_URL:
                    agency.setUrl(value);
                    break;
                case KEY_AGENCY_PHONE:
                    agency.setPhone(value);
                    break;
            }
        }

        return agency;
    }
}
