package com.dgsd.android.data;

import com.dgsd.android.data.converter.CalendarInfoModelConverter;
import com.dgsd.android.data.converter.RouteModelConverter;
import com.dgsd.android.data.converter.StopModelConverter;
import com.dgsd.android.data.converter.StopTimeModelConverter;
import com.dgsd.android.data.converter.TripModelConverter;
import com.dgsd.android.data.db.DatabaseBackend;
import com.dgsd.android.data.model.DbCalendarInfo;
import com.dgsd.android.data.model.DbCalendarInfoEx;
import com.dgsd.android.data.model.DbRoute;
import com.dgsd.android.data.model.DbStop;
import com.dgsd.android.data.model.DbStopTime;
import com.dgsd.android.data.model.DbTrip;
import com.dgsd.sydtrip.model.CalendarInfo;
import com.dgsd.sydtrip.model.Route;
import com.dgsd.sydtrip.model.Stop;
import com.dgsd.sydtrip.model.StopTime;
import com.dgsd.sydtrip.model.Trip;

import java.util.List;

import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func1;
import rx.functions.Func2;

public class DataSource {

    private final DatabaseBackend db;

    public DataSource(DatabaseBackend dbBackend) {
        this.db = dbBackend;
    }

    public Observable<int[]> getTripIdsForStop(final int stopId) {
        return Observable.defer(new Func0<Observable<int[]>>() {
            @Override
            public Observable<int[]> call() {
                return Observable.just(db.getTripIdsForStop(stopId));
            }
        });
    }

    public Observable<int[]> getStopIdsForTrip(final int tripId) {
        return Observable.defer(new Func0<Observable<int[]>>() {
            @Override
            public Observable<int[]> call() {
                return Observable.just(db.getStopIdsForTrip(tripId));
            }
        });
    }

    public Observable<int[]> getStopIdsAtSameLocation(final int stopId) {
        return Observable.defer(new Func0<Observable<int[]>>() {
            @Override
            public Observable<int[]> call() {
                return Observable.just(db.getStopIdsAtSameLocation(stopId));
            }
        });
    }

    public Observable<Stop> getStop(final int stopId) {
        return Observable.defer(new Func0<Observable<DbStop>>() {
            @Override
            public Observable<DbStop> call() {
                return Observable.just(db.getStop(stopId));
            }
        }).filter(new Func1<DbStop, Boolean>() {
            @Override
            public Boolean call(DbStop dbStop) {
                return dbStop != null;
            }
        }).map(new Func1<DbStop, Stop>() {
            @Override
            public Stop call(DbStop dbStop) {
                return StopModelConverter.getInstance().convert(dbStop);
            }
        });
    }

    public Observable<Trip> getTrip(final int tripId) {
        return Observable.defer(new Func0<Observable<DbTrip>>() {
            @Override
            public Observable<DbTrip> call() {
                return Observable.just(db.getTrip(tripId));
            }
        }).filter(new Func1<DbTrip, Boolean>() {
            @Override
            public Boolean call(DbTrip dbTrip) {
                return dbTrip != null;
            }
        }).map(new Func1<DbTrip, Trip>() {
            @Override
            public Trip call(DbTrip dbTrip) {
                return TripModelConverter.getInstance().convert(dbTrip);
            }
        });
    }

    public Observable<Route> getRoute(final int routeId) {
        return Observable.defer(new Func0<Observable<DbRoute>>() {
            @Override
            public Observable<DbRoute> call() {
                return Observable.just(db.getRoute(routeId));
            }
        }).filter(new Func1<DbRoute, Boolean>() {
            @Override
            public Boolean call(DbRoute dbRoute) {
                return dbRoute != null;
            }
        }).map(new Func1<DbRoute, Route>() {
            @Override
            public Route call(DbRoute dbRoute) {
                return RouteModelConverter.getInstance().convert(dbRoute);
            }
        });
    }

    public Observable<CalendarInfo> getCalendarInfo(final int tripId, final int jd) {
        return Observable.defer(new Func0<Observable<DbCalendarInfo>>() {
            @Override
            public Observable<DbCalendarInfo> call() {
                return Observable.just(db.getCalendarInfo(tripId, jd));
            }
        }).filter(new Func1<DbCalendarInfo, Boolean>() {
            @Override
            public Boolean call(DbCalendarInfo dbCalInfo) {
                return dbCalInfo != null;
            }
        }).map(new Func1<DbCalendarInfo, CalendarInfo>() {
            @Override
            public CalendarInfo call(DbCalendarInfo dbCalInfo) {
                return CalendarInfoModelConverter.getInstance().convert(dbCalInfo);
            }
        });
    }

    public Observable<List<StopTime>> getStopTimes(final int tripId) {
        return Observable.defer(new Func0<Observable<List<DbStopTime>>>() {
            @Override
            public Observable<List<DbStopTime>> call() {
                return Observable.just(db.getStopTimes(tripId));
            }
        }).flatMap(new Func1<List<DbStopTime>, Observable<DbStopTime>>() {
            @Override
            public Observable<DbStopTime> call(List<DbStopTime> dbStopTimes) {
                return Observable.from(dbStopTimes);
            }
        }).map(new Func1<DbStopTime, StopTime>() {
            @Override
            public StopTime call(DbStopTime dbStopTime) {
                return StopTimeModelConverter.getInstance().convert(dbStopTime);
            }
        }).toSortedList(new Func2<StopTime, StopTime, Integer>() {
            @Override
            public Integer call(StopTime lhs, StopTime rhs) {
                return Integer.compare(lhs.getSecondsSinceMidnight(), rhs.getSecondsSinceMidnight());
            }
        });
    }

    public Observable<List<Stop>> getStops() {
        return Observable.defer(new Func0<Observable<List<DbStop>>>() {
            @Override
            public Observable<List<DbStop>> call() {
                return Observable.just(db.getStops());
            }
        }).flatMap(new Func1<List<DbStop>, Observable<DbStop>>() {
            @Override
            public Observable<DbStop> call(List<DbStop> dbStops) {
                return Observable.from(dbStops);
            }
        }).filter(new Func1<DbStop, Boolean>() {
            @Override
            public Boolean call(DbStop dbStop) {
                // Get parents only..
                return dbStop.getParentId() <= 0;
            }
        }).map(new Func1<DbStop, Stop>() {
            @Override
            public Stop call(DbStop dbStop) {
                return StopModelConverter.getInstance().convert(dbStop);
            }
        }).toSortedList(new Func2<Stop, Stop, Integer>() {
            @Override
            public Integer call(Stop lhs, Stop rhs) {
                if (lhs.getName() == null) {
                    return 1;
                } else if (rhs.getName() == null) {
                    return -1;
                } else {
                    return lhs.getName().compareTo(rhs.getName());
                }
            }
        });
    }

    public void saveStopTimes(List<DbStopTime> stopTimes) {
        db.saveStopTimes(stopTimes);
    }

    public void saveStops(List<DbStop> stops) {
        db.saveStops(stops);
    }

    public void saveTrips(List<DbTrip> trips) {
        db.saveTrips(trips);
    }

    public void saveRoutes(List<DbRoute> routes) {
        db.saveRoutes(routes);
    }

    public void saveCalendarInfo(List<DbCalendarInfo> calInfo) {
        db.saveCalendarInfo(calInfo);
    }

    public void saveCalendarInfoEx(List<DbCalendarInfoEx> calInfoEx) {
        db.saveCalendarInfoEx(calInfoEx);
    }
}
