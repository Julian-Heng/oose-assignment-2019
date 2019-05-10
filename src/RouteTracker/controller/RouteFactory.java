package RouteTracker.controller;

import java.util.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

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

    public Map<String,Route> make() throws RouteFactoryException
    {
        Map<List<String>,List<List<String>>> routeTable;
        Set<String> inProgress = new HashSet<>();
        Map<String,Route> routes = new HashMap<>();

        routeTable = parser.getRouteTable();

        /*
        try
        {
        */
            for (Map.Entry<List<String>,List<List<String>>> e : routeTable.entrySet())
            {
                Route r;
                List<String> route = e.getKey();
                List<List<String>> points = e.getValue();
                String routeName = route.get(0);

                if (! routes.containsKey(route.get(0)))
                {
                    r = makeRoute(route, routes, routeTable, inProgress);
                    routes.put(route.get(0), r);
                }
            }
        /*
        }
        catch (RouteParserException e)
        {
            throw new RouteFactoryException(e.getMessage());
        }
        */

        return routes;
    }

    private Route makeRoute(List<String> route,
                           Map<String,Route> routes,
                           Map<List<String>,List<List<String>>> routeTable,
                           Set<String> inProgress) throws RouteFactoryException
    {
        Route r;
        Segment s;
        PointNode p1 = null;
        PointNode p2 = null;

        List<List<String>> points = routeTable.get(route);
        String routeName = route.get(0);
        String routeDesc = route.get(1);

        String segDesc1 = "";
        String segDesc2 = "";

        if (inProgress.contains(routeName))
        {
        }
        else
        {
            inProgress.add(routeName);
        }

        r = new Route(routeName, routeDesc);

        /*
        System.out.println(routeName + ": " + routeDesc);
        for (List<String> point : points)
        {
            System.out.println(point);
        }
        System.out.println();
        */

        try
        {
            for (List<String> point : points)
            {
                segDesc2 = point.get(3);
                if (! segDesc2.isEmpty() && segDesc2.charAt(0) == '*')
                {
                    String subName = segDesc2.substring(1);
                    Route subRoute;
                    if (routes.containsKey(subName))
                    {
                        p2 = routes.get(subName);
                    }
                    else
                    {
                        List<String> subRouteInfo = null;
                        // Need to make a separate map...
                        for (List<String> routeEntry : routeTable.keySet())
                        {
                            if (routeEntry.get(0).equals(subName))
                            {
                                subRouteInfo = routeEntry;
                                break;
                            }
                        }

                        subRoute = makeRoute(subRouteInfo, routes,
                                             routeTable, inProgress);
                        routes.put(subName, subRoute);
                        p2 = subRoute;
                    }
                }
                else
                {
                    p2 = pointMaker.make(point);
                }

                if (p1 != null)
                {
                    s = new Segment(p1, p2, segDesc1);
                    r.add(s);
                }

                p1 = p2;
                segDesc1 = segDesc2;
            }
        }
        catch (PointFactoryException e)
        {
            throw new RouteFactoryException(e.getMessage());
        }

        return r;
    }
}
