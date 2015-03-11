package com.dgsd.android.data.converter;

import com.dgsd.android.data.model.DbRoute;
import com.dgsd.sydtrip.model.Route;

public class RouteModelConverter implements DbToModelConverter<DbRoute, Route> {

    private static RouteModelConverter instance;


    public static RouteModelConverter getInstance() {
        if (instance == null) {
            instance = new RouteModelConverter();
        }
        return instance;
    }

    private RouteModelConverter() {

    }

    @Override
    public Route convert(DbRoute dbRoute) {
        return new Route(
                dbRoute.getId(), dbRoute.getShortName(), dbRoute.getLongName(), dbRoute.getColor());
    }
}
