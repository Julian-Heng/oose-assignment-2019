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
        Map<String,List<String>> routeNameTable;
        Set<String> inProgress = new HashSet<>();
        Map<String,Route> routes = new HashMap<>();

        routeTable = parser.getRouteTable();
        routeNameTable = parser.getRouteNameTable();

        for (List<String> route : routeTable.keySet())
        {
            if (! routes.containsKey(route.get(0)))
            {
                routes.put(
                    route.get(0),
                    makeRoute(route, routes, routeTable,
                              routeNameTable, inProgress)
                );
            }
        }

        return routes;
    }

    private Route makeRoute(List<String> route,
                           Map<String,Route> routes,
                           Map<List<String>,List<List<String>>> routeTable,
                           Map<String,List<String>> routeNameTable,
                           Set<String> inProgress) throws RouteFactoryException
    {
        Route r;
        Route subRoute = null;
        PointNode p1 = null;
        PointNode p2 = null;

        List<List<String>> points = routeTable.get(route);
        String routeName = route.get(0);
        String routeDesc = route.get(1);

        String segDesc1 = "";
        String segDesc2 = "";

        boolean distanceCheckFlag = false;

        // Recursion check
        if (inProgress.contains(routeName))
        {
            throw new RouteFactoryException(
                "Error while parsing route " + routeName + "\n" +
                "Sub-route is attempting to add a route that " +
                "depends on itself"
            );
        }
        else
        {
            inProgress.add(routeName);
        }

        r = new Route(routeName, routeDesc);

        try
        {
            for (List<String> point : points)
            {
                // Keep references from the previous iteration
                p1 = p2;
                segDesc1 = segDesc2;
                segDesc2 = point.get(3);
                p2 = pointMaker.make(point);

                // The distanceCheckFlag flag is set after encountering a
                // sub-route. Because the sub-route point in the main route can
                // be very close to the actual route's starting point, we would
                // have to perform a check to see if it is close or exact.
                // Thus, we can assume that p1 is the sub-route added from the
                // previous iteration and p2 is the next point from the main
                // route that is exactly the same or very close to the end of
                // the sub-route. Hence, the getEndPoint() and getStartPoint()
                // method calls.
                if (distanceCheckFlag)
                {
                    if (! checkDistance(p1, p2))
                    {
                        throw new RouteFactoryException(
                            "Error while parsing route " +
                            routeName + "\nDistance betwwen last point " +
                            "and sub-route is too far away"
                        );
                    }

                    distanceCheckFlag = false;
                }

                // Point is a sub route
                if (! segDesc2.isEmpty() && segDesc2.charAt(0) == '*')
                {
                    String subName = segDesc2.substring(1);
                    distanceCheckFlag = true;

                    if (! routes.containsKey(subName))
                    {
                        // Recursion
                        routes.put(
                            subName,
                            makeRoute(routeNameTable.get(subName),
                                      routes, routeTable,
                                      routeNameTable, inProgress)
                        );
                    }

                    subRoute = routes.get(subName);

                    // Check if first iteration
                    if (p1 != null)
                    {
                        r.add(new Segment(p1, p2, segDesc1));
                    }

                    p1 = p2;
                    p2 = subRoute;

                    if (! checkDistance(p1, p2))
                    {
                        throw new RouteFactoryException(
                            "Error while parsing route " +
                            routeName + "\nDistance betwwen last point " +
                            "and sub-route is too far away"
                        );
                    }

                    r.add(new Segment(p1, p2, segDesc2));
                    distanceCheckFlag = true;
                }
                else
                {
                    // Check if first iteration
                    if (p1 != null)
                    {
                        r.add(new Segment(p1, p2, segDesc1));
                    }
                    // Edge case for routes with only one point
                    else if (points.size() == 1)
                    {
                        r.add(p2);
                    }
                }
            }
        }
        catch (PointFactoryException e)
        {
            throw new RouteFactoryException(e.getMessage());
        }

        return r;
    }

    private boolean checkDistance(PointNode n1, PointNode n2)
    {
        double distance;

        // Check distance between main route's sub-route and
        // sub-route's starting point
        distance = utils.calcMetresDistance(
            n1.getEndPoint().getLatitude(),
            n1.getEndPoint().getLongitude(),
            n2.getStartPoint().getLatitude(),
            n2.getStartPoint().getLongitude()
        );

        return Double.compare(distance, 10) <= 0;
    }
}
