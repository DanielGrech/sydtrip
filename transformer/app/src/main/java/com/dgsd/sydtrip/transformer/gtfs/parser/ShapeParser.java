package com.dgsd.sydtrip.transformer.gtfs.parser;

import com.dgsd.sydtrip.transformer.gtfs.model.source.GtfsShape;

import java.util.List;

public class ShapeParser extends CsvParser<GtfsShape> {

    private static final String KEY_SHAPE_ID = "shape_id";
    private static final String KEY_SHAPE_PT_LAT = "shape_pt_lat";
    private static final String KEY_SHAPE_PT_LON = "shape_pt_lon";
    private static final String KEY_SHAPE_PT_SEQUENCE = "shape_pt_sequence";
    private static final String KEY_SHAPE_DIST_TRAVELLED = "shape_dist_traveled";

    @Override
    protected GtfsShape create(List<String> fields, List<String>  values) {
        final GtfsShape shape = new GtfsShape();

        for (int i = 0, size = fields.size(); i < size; i++) {
            final String key = fields.get(i);
            final String value = values.get(i);

            switch (key) {
                case KEY_SHAPE_ID:
                    shape.setId(value);
                    break;
                case KEY_SHAPE_PT_LAT:
                    shape.setPtLat(value);
                    break;
                case KEY_SHAPE_PT_LON:
                    shape.setPtLng(value);
                    break;
                case KEY_SHAPE_PT_SEQUENCE:
                    shape.setPtSequence(value);
                    break;
                case KEY_SHAPE_DIST_TRAVELLED:
                    shape.setDistTravelled(value);
                    break;
            }
        }

        return shape;
    }
}
