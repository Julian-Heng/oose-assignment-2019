package RouteTracker.controller.factory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import RouteTracker.controller.GeoUtils;
import RouteTracker.controller.RouteParser;
import RouteTracker.model.Point;
import RouteTracker.model.PointNode;
import RouteTracker.model.Route;
import RouteTracker.model.Segment;
import RouteTracker.model.exception.PointFactoryException;
import RouteTracker.model.exception.RouteFactoryException;

/**
 * RouteFactory class that creates Route objects
 * @author Julian Heng (19473701)
 **/
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

    /**
     * Makes multiple Route into a map
     * @return A newly created route map
     * @throws RouteFactoryException Thrown when there's a problem with
     *                               the route declaration, such as
     *                               sub-route's not close enough to the
     *                               point in the route
     **/
    public Map<String,Route> make() throws RouteFactoryException
    {
        // Routes in object form
        Map<String,Route> routes = new HashMap<>();
        Set<String> inProgress = new HashSet<>();

        // Routes in string form
        Map<List<String>,List<List<String>>> routeTable;
        Map<String,List<String>> routeNameTable;

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

    /**
     * A recursive method of creating routes/sub-routes
     * @param route          The name and description of the route
     * @param routes         The map containing all routes
     * @param routeTable     The map containing all routes declaration
     * @param routeNameTable The map containing only the route name and
     *                       route description
     * @param inProgress     The set for checking recursion loop
     * @return A new Route object
     * @throws RouteFactoryException
     **/
    private Route
    makeRoute(List<String> route, Map<String,Route> routes,
              Map<List<String>,List<List<String>>> routeTable,
              Map<String,List<String>> routeNameTable,
              Set<String> inProgress) throws RouteFactoryException
    {
        Route r;
        Route subRoute = null;
        PointNode p1 = null;
        PointNode p2 = null;
        double distance;

        // Fetch all point declarations from the routeTable
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
                "Error while parsing route \"" + routeName + "\"\n" +
                "Sub-route is attempting to add a route that " +
                "depends on itself"
            );
        }
        else
        {
            r = new Route(routeName, routeDesc);
            inProgress.add(routeName);
        }

        try
        {
            for (List<String> point : points)
            {
                // Keep references from the previous iteration
                p1 = p2;
                segDesc1 = segDesc2;
                segDesc2 = point.get(3);
                p2 = pointMaker.make(point);

                /**
                 * The distanceCheckFlag flag is set after encountering a
                 * sub-route. Because the sub-route point in the main route can
                 * be very close to the actual route's starting point, we would
                 * have to perform a check to see if it is close or exact.
                 * Thus, we can assume that p1 is the sub-route added from the
                 * previous iteration and p2 is the next point from the main
                 * route that is exactly the same or very close to the end of
                 * the sub-route. Hence, the getEndPoint() and getStartPoint()
                 * method calls in checkDistance().
                 **/
                if (distanceCheckFlag)
                {
                    if (! checkDistance(p1, p2))
                    {
                        throw new RouteFactoryException(
                            "Error while parsing route \"" +
                            routeName + "\'\nDistance between last point " +
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
                        // Create a new segment because the sub-route
                        // starting point is not always the same coordinates
                        distance = getDistance(p1.getStartPoint(),
                                               p2.getStartPoint());
                        r.add(new Segment(p1, p2, segDesc1, distance));
                    }

                    p1 = p2;
                    p2 = subRoute;

                    if (! checkDistance(p1, p2))
                    {
                        throw new RouteFactoryException(
                            "Error while parsing route \"" +
                            routeName + "\"\nDistance between last point " +
                            "and sub-route is too far away"
                        );
                    }

                    r.add(new Segment(p1, p2, segDesc2, p2.getDistance()));

                    // Make the next iteration to use sub-route's ending node
                    // and not the sub-route again
                    p2 = p2.getEndNode();
                    distanceCheckFlag = true;
                }
                else
                {
                    // Skip if first iteration
                    if (p1 != null)
                    {
                        // Start point to get the Point, not PointNode
                        distance = getDistance(p1.getStartPoint(),
                                               p2.getStartPoint());
                        r.add(new Segment(p1, p2, segDesc1, distance));
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

    /**
     * Check if the distance between 2 points is within 10m horizontally and
     * 2m vertically
     * @param n1 The first point
     * @param n2 The second point
     * @return True or False
     **/
    private boolean checkDistance(PointNode n1, PointNode n2)
    {
        double distance;
        double deltaAlt;

        Point p1 = n1.getEndPoint();
        Point p2 = n2.getStartPoint();

        // Check distance between main route's sub-route and sub-route's
        // starting point
        distance = getDistance(p1, p2);

        // Altitude check
        deltaAlt = Math.abs(p1.getAltitude() - p2.getAltitude());

        return Double.compare(distance, 10) <= 0 &&
               Double.compare(deltaAlt, 2) <= 0;
    }

    /**
     * Wrapper function to get the distance between two points
     * @param p1 The first point
     * @param p2 The second point
     * @return The distance between the two points
     **/
    private double getDistance(Point p1, Point p2)
    {
        return utils.calcMetresDistance(p1.getLatitude(), p1.getLongitude(),
                                        p2.getLatitude(), p2.getLongitude());
    }
}
