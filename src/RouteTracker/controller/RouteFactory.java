package RouteTracker.controller;

import java.util.*;
import RouteTracker.model.*;

public class RouteFactory
{
    private PointFactory pointMaker;
    private RouteParser parser;
    private GeoUtils utils;

    public RouteFactory(PointFactory pointMaker,
                        RouteParser parser,
                        GeoUtils utils)
    {
        this.pointMaker = pointMaker;
        this.parser = parser;
        this.utils = utils;
    }

    public Map<String,Route> make()
    {
        Map<String,Route> routes = new HashMap<>();

        return routes;
    }
}
