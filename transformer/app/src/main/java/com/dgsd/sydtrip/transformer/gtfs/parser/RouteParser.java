package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsRoute;

import java.util.List;

public class RouteParser extends CsvParser<GtfsRoute> {

    private static final String KEY_ROUTE_ID = "route_id";
    private static final String KEY_AGENCY_ID = "agency_id";
    private static final String KEY_SHORT_NAME = "route_short_name";
    private static final String KEY_LONG_NAME = "route_long_name";
    private static final String KEY_ROUTE_DESC = "route_desc";
    private static final String KEY_ROUTE_TYPE = "route_type";
    private static final String KEY_ROUTE_COLOR = "route_color";
    private static final String KEY_ROUTE_TEXT_COLOR = "route_text_color";

    @Override
    protected GtfsRoute create(List<String> fields, String... values) {
        final GtfsRoute route = new GtfsRoute();

        for (int i = 0, len = fields.size(); i < len; i++) {
            final String key = fields.get(i);
            final String value = values[i];

            switch (key) {
                case KEY_ROUTE_ID:
                    route.setId(value);
                    break;
                case KEY_AGENCY_ID:
                    route.setAgencyId(value);
                    break;
                case KEY_SHORT_NAME:
                    route.setShortName(value);
                    break;
                case KEY_LONG_NAME:
                    route.setLongName(value);
                    break;
                case KEY_ROUTE_DESC:
                    route.setDesc(value);
                    break;
                case KEY_ROUTE_TYPE:
                    route.setType(value);
                    break;
                case KEY_ROUTE_COLOR:
                    route.setColor(value);
                    break;
                case KEY_ROUTE_TEXT_COLOR:
                    route.setTextColor(value);
                    break;
            }
        }

        return route;
    }
}
