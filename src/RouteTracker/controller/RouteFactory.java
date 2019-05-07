package RouteTracker.controller;

import java.util.*;

import RouteTracker.model.*;
import RouteTracker.controller.*;

public class RouteFactory
{
    private RouteParser parser;
    private List<String> contents;
    private PointFactory pointMaker;

    public RouteFactory(RouteParser parser,
                        List<String> contents,
                        PointFactory pointMaker)
    {
        this.parser = parser;
        this.contents = contents;
        this.pointMaker = pointMaker;
    }

    public Map<String,Route> makeRoutes() throws RouteFactoryException
    {
        Map<String,Route> routes = new HashMap<>();
        Set<String> inProgress = new HashSet<>();
        Map<String,Integer> routeTable;

        Route route;
        String routeName = "";
        String line = "";


        try
        {
            routeTable = parser.makeIndex();

            for (Map.Entry<String,Integer> entry : routeTable.entrySet())
            {
                line = contents.get(entry.getValue());
                routeName = parser.parseRouteName(line);

                if (! routes.containsKey(routeName))
                {
                    route = makeRoute(routes, inProgress,
                                      routeTable, entry.getValue());
                    routes.put(routeName, route);
                }
            }
        }
        catch (RouteParserException e)
        {
            throw new RouteFactoryException(
                e.getMessage() + "\n" + formattedLineNo(line)
            );
        }

        return routes;
    }

    public Route makeRoute(Map<String,Route> routes,
                           Set<String> inProgress,
                           Map<String,Integer> routeTable,
                           int start) throws RouteFactoryException
    {
        Route newRoute = null;
        Route subRoute = null;
        Point p1, p2;

        ListIterator<String> iter;
        String line;
        boolean checkDistance;
        boolean endPoint;

        String name;
        String description;

        double distance;

        iter = contents.listIterator(start + 1);
        checkDistance = false;
        endPoint = false;

        line = contents.get(start);

        try
        {
            name = parser.parseRouteName(line);
            description = parser.parseRouteDescription(line);
        }
        catch (RouteParserException e)
        {
            throw new RouteFactoryException(
                e.getMessage() + "\n" + formattedLineNo(line)
            );
        }

        // Flag route as in progress
        inProgress.add(name);
        newRoute = new Route(name, description);

        try
        {
            while (iter.hasNext() && ! endPoint)
            {
                line = iter.next();

                if (! line.isEmpty())
                {
                    if (parser.isSubRoute(line))
                    {
                        String subRouteName;

                        p2 = pointMaker.makePoint(line);
                        subRouteName = p2.getName().substring(1);

                        // Recursion check
                        if (inProgress.contains(subRouteName))
                        {
                            throw new RouteParserException(
                                "Error while parsing route " + name +
                                "\n" + formattedLineNo(line) +
                                "\nSub-route is attempting to add a route that " +
                                "depends on itself"
                            );
                        }
                        // Check if sub-route already exists in the route map
                        else if (routes.containsKey(subRouteName))
                        {
                            subRoute = routes.get(subRouteName);
                        }
                        else
                        {
                            // Start recursion
                            subRoute = makeRoute(routes, inProgress, routeTable,
                                                 routeTable.get(subRouteName));
                            routes.put(subRouteName, subRoute);
                        }
                    }
                    else if (parser.isPoint(line))
                    {
                        p2 = pointMaker.makePoint(line);
                        endPoint = parser.isEndPoint(line);
                    }
                    // Uh oh, something's wrong
                    else
                    {
                        // Check if it's a route declaration
                        if (parser.isRoute(line) &&
                            routeTable.containsKey(parser.parseRouteName(line)))
                        {
                            throw new RouteParserException(
                                "Error while parsing route " + name +
                                "\n" + formattedLineNo(line) +
                                "\nMissing terminating line?"
                            );
                        }

                        // Exhausted all checks
                        throw new RouteParserException(
                            "Unknown error while parsing route " + name +
                            "\n" + formattedLineNo(line)
                        );
                    }

                    p1 = newRoute.getEnd();

                    if (checkDistance)
                    {
                        if (! parser.validateDistance(p1, p2))
                        {
                            throw new RouteParserException(
                                "Error while parsing route " + name +
                                "\n" + formattedLineNo(line) +
                                "\nDistance between last point and sub-route is " +
                                "too far away"
                            );
                        }

                        checkDistance = false;
                    }

                    newRoute.add(p2);

                    // Check if p2 is the first point in the route
                    if (p1 != null)
                    {
                        newRoute.updateDistance(parser.getDistance(p1, p2));
                        newRoute.updateAltitude(p1.getAltitude() - p2.getAltitude());
                    }

                    if (subRoute != null)
                    {
                        // Set flag to check the distance for the next point
                        checkDistance = true;

                        p1 = newRoute.getEnd();
                        p2 = subRoute.getStart();

                        if (! parser.validateDistance(p1, p2))
                        {
                            throw new RouteParserException(
                                "Error while parsing route " + name +
                                "\n" + formattedLineNo(line) +
                                "\nDistance between last point and sub-route is " +
                                "too far away"
                            );
                        }

                        newRoute.add(subRoute);
                        newRoute.updateDistance(parser.getDistance(p1, p2));
                        newRoute.updateAltitude(p2.getAltitude() - p1.getAltitude());

                        subRoute = null;
                        p2 = null;
                    }

                    p1 = null;
                }
            }
        }
        catch (RouteParserException | PointFactoryException e)
        {
            throw new RouteFactoryException(e.getMessage());
        }

        if (! endPoint)
        {
            throw new RouteFactoryException(
                "Error while parsing route " + name +
                "\nMissing terminating line"
            );
        }

        // Unset in progress flag
        inProgress.remove(name);

        return newRoute;
    }

    private String formattedLineNo(String line)
    {
        return (contents.indexOf(line) + 1) + ": " + line;
    }
}
