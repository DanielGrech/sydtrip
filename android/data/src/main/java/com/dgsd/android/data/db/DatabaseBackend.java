package com.dgsd.android.data.db;

import com.dgsd.android.data.model.DbCalendarInfo;
import com.dgsd.android.data.model.DbCalendarInfoEx;
import com.dgsd.android.data.model.DbGraphEdge;
import com.dgsd.android.data.model.DbRoute;
import com.dgsd.android.data.model.DbStop;
import com.dgsd.android.data.model.DbStopTime;
import com.dgsd.android.data.model.DbTrip;

import java.util.List;

public interface DatabaseBackend {

    public int[] getTripIdsForStop(int stopId);

    public int[] getStopIdsForTrip(int tripId);

    public int[] getStopIdsAtSameLocation(int stopId);

    public List<DbGraphEdge> getNetwork();

    public List<DbStop> getStops();

    public List<DbStopTime> getStopTimes(int tripId);

    public DbTrip getTrip(int tripId);

    public DbRoute getRoute(int routeId);

    public DbStop getStop(int stopId);

    public DbCalendarInfo getCalendarInfo(int tripId, int julianDay);

    public void saveStopTimes(List<DbStopTime> stopTimes);

    public void saveStops(List<DbStop> stops);

    public void saveTrips(List<DbTrip> trips);

    public void saveRoutes(List<DbRoute> routes);

    public void saveCalendarInfo(List<DbCalendarInfo> calInfo);

    public void saveCalendarInfoEx(List<DbCalendarInfoEx> calInfoEx);

    public void saveGraphEdges(List<DbGraphEdge> edges);

    public static final class Contract {
        private Contract() {
            throw new IllegalStateException("No instances..");
        }

        public static final class DynamicText {
            public static final String TABLE_NAME = "dynamic_text";

            public static final String COL_ID = "id";
            public static final String COL_VALUE = "value";
        }

        public static final class CalendarInfo {
            public static final String TABLE_NAME = "calendar_info";

            public static final String COL_TRIP_ID = "tripId";
            public static final String COL_START_DAY = "startDay";
            public static final String COL_END_DAY = "endDay";
            public static final String COL_AVAILABILITY = "availability";
        }

        public static final class CalendarInfoEx {
            public static final String TABLE_NAME = "calendar_info_ex";

            public static final String COL_TRIP_ID = "tripId";
            public static final String COL_JULIAN_DAY = "julianDay";
            public static final String COL_EXCEPTION_TYPE = "exceptionType";
        }

        public static final class Stops {
            public static final String TABLE_NAME = "stops";

            public static final String COL_ID = "id";
            public static final String COL_CODE = "code";
            public static final String COL_NAME = "name";
            public static final String COL_LAT = "lat";
            public static final String COL_LNG = "lng";
            public static final String COL_TYPE = "type";
            public static final String COL_PARENT_ID = "parentId";
            public static final String COL_PLATFORM_CODE = "platformCode";
        }

        public static final class StopTimes {
            public static final String TABLE_NAME = "stop_times";

            public static final String COL_STOP_ID = "stopId";
            public static final String COL_SECONDS_SINCE_MIDNIGHT = "secondsSinceMidnight";
            public static final String COL_STOP_TIME_ID = "stopTimeId";

        }

        public static final class Trips {
            public static final String TABLE_NAME = "trips";

            public static final String COL_ID = "id";
            public static final String COL_HEADSIGN = "headSign";
            public static final String COL_DIRECTION = "direction";
            public static final String COL_BLOCK_ID = "blockId";
            public static final String COL_WHEELCHAIR_ACCESS = "wheelchairAccess";
            public static final String COL_ROUTE_ID = "routeId";
            public static final String COL_STOP_TIME_ID = "stopTimeId";
        }

        public static final class Routes {
            public static final String TABLE_NAME = "routes";

            public static final String COL_ID = "id";
            public static final String COL_AGENCY_ID = "agencyId";
            public static final String COL_SHORT_NAME = "shortName";
            public static final String COL_LONG_NAME = "longName";
            public static final String COL_COLOR = "color";
        }
    }
}
