package com.dgsd.sydtrip.routing.util;

import com.dgsd.sydtrip.model.Stop;

public class LocationUtils {
    private static final int EARTH_RADIUS_IN_METERS = 6371 * 1000;

    public static int distanceBetween(Stop lhs, Stop rhs) {
        return distanceBetween(lhs.getLat(), lhs.getLng(), rhs.getLat(), rhs.getLng());
    }

    /**
     * Returns the distance in meters between the two points
     * @param lat1
     * @param lon1
     * @param lat2
     * @param lon2
     * @return
     */
    public static int distanceBetween(double lat1, double lon1, double lat2, double lon2) {
        if(lat1 == lat2 && lon1 == lon2)
            return 0;

        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.asin(Math.sqrt(a));
        return (int) (EARTH_RADIUS_IN_METERS * c);
    }
}
