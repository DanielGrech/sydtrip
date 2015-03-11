package com.dgsd.sydtrip.transformer.gtfs;

import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingShape;
import com.dgsd.sydtrip.transformer.gtfs.model.staging.GtfsStagingStopTime;
import com.dgsd.sydtrip.transformer.gtfs.model.target.Shape;
import com.dgsd.sydtrip.transformer.gtfs.model.target.StopTime;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class TargetConverter {

    public static Map<Integer, Shape> convertShapes(List<GtfsStagingShape> shapes) {
        if (shapes == null || shapes.isEmpty()) {
            return Collections.emptyMap();
        }

        return shapes.stream()
                .collect(groupingBy(GtfsStagingShape::getId)).values()
                .parallelStream()
                .map(list ->
                        list.stream().sorted(GtfsStagingShape.SORT_BY_SEQUENCE).collect(toList()))
                .map(list -> new Shape(list))
                .collect(toMap(Shape::getId, Function.identity()));
    }

    public static Map<Integer, List<StopTime>> convertStopTimes(List<GtfsStagingStopTime> times) {
        if (times == null || times.isEmpty()) {
            return Collections.emptyMap();
        }

        final Map<Integer, List<StopTime>> retval = new HashMap<>();
        final Map<Integer, List<GtfsStagingStopTime>> segmentedByTripId
                = times.stream().collect(groupingBy(GtfsStagingStopTime::getTripId));

        segmentedByTripId.keySet().forEach(key -> {
            retval.put(key, segmentedByTripId.get(key)
                    .parallelStream()
                    .sorted(GtfsStagingStopTime.SORT_BY_SEQUENCE)
                    .map(stagingTime -> new StopTime(stagingTime))
                    .collect(toList()));
        });

        return retval;
    }
}
