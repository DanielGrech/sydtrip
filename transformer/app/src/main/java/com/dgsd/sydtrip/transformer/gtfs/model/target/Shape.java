package com.dgsd.sydtrip.transformer.gtfs.model.target;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingShape;
import com.dgsd.sydtrip.transformer.util.PolylineEncoderUtils;

import java.util.LinkedList;
import java.util.List;

public class Shape {

    private final int id;

    private final String polyLine;

    private final float totalDistance;

    public Shape(List<GtfsStagingShape> stagingShapes) {
        id = stagingShapes.isEmpty() ? -1 : stagingShapes.get(0).getId();
        totalDistance = (float) stagingShapes.stream()
                .mapToDouble(GtfsStagingShape::getDistTravelled)
                .sum();

        List<Float> points = new LinkedList<>();
        stagingShapes.forEach(shape -> {
            points.add(shape.getLat());
            points.add(shape.getLng());
        });
        polyLine = PolylineEncoderUtils.encode(points);
    }

    public int getId() {
        return id;
    }

    public String getPolyLine() {
        return polyLine;
    }

    public float getTotalDistance() {
        return totalDistance;
    }

    @Override
    public String toString() {
        return "Shape{" +
                "id=" + id +
                ", polyLine='" + polyLine + '\'' +
                ", totalDistance=" + totalDistance +
                '}';
    }
}
