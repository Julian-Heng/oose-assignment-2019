package RouteTracker.controller;

import java.io.IOException;
import java.util.*;

import RouteTracker.model.*;

public class RouteParser
{
    private GeoUtils util;
    private List<String> contents;

    public RouteParser(GeoUtils util)
    {
        this.util = util;
        this.contents = new ArrayList<>();
    }

    public void readFile(String url) throws RouteParserException
    {
        try
        {
            contents = new ArrayList<>(
                Arrays.asList(util.retrieveRouteData(url).split("\n"))
            );
        }
        catch (IOException e)
        {
            throw new RouteParserException(e.getMessage());
        }
    }

    public Map<String,Route> parseRoutes() throws RouteParserException
    {
        Map<String,Route> routes = new HashMap<>();
        Set<String> inProgress = new HashSet<>();
        Map<String,Integer> routeTable;
        Route route;

        routeTable = makeIndexes();

        for (Map.Entry<String,Integer> entry : routeTable.entrySet())
        {
            String[] split = contents.get(entry.getValue()).split(" ");

            if (! routes.containsKey(split[0]))
            {
                route = makeRoute(routes, inProgress,
                                  routeTable, entry.getValue());
                routes.put(split[0], route);
            }
        }

        return routes;
    }

    // Route factory
    public Route makeRoute(Map<String,Route> routes,
                           Set<String> inProgress,
                           Map<String,Integer> routeTable,
                           int start) throws RouteParserException
    {
        Route newRoute = null;
        Route subRoute = null;
        Point p1, p2;

        ListIterator<String> iter;
        String[] split;
        String line;
        boolean checkDistance;
        boolean endNode;

        String name;
        String description;

        String subRouteName;

        iter = contents.listIterator(start + 1);
        checkDistance = false;
        endNode = false;

        split = contents.get(start).split(" ", 2);

        // Validating route declaration
        if (split.length == 1)
        {
            throw new RouteParserException(
                "Error while parsing route " + split[0] +
                "\n" + (start + 1) + ": " + contents.get(start) +
                "\nMissing description for route"
            );
        }
        else
        {
            name = split[0];
            description = split[1];
        }

        // Flag route as in progress
        inProgress.add(name);
        newRoute = new Route(name, description);

        try
        {
            while (iter.hasNext() && ! endNode)
            {
                line = iter.next();

                // Skip if empty
                if (line.isEmpty())
                {
                    continue;
                }

                split = line.split(",", 4);

                // Is a sub-route
                if (split.length > 3 && split[3].charAt(0) == '*')
                {
                    p1 = makePoint(split);
                    subRouteName = split[3].substring(1, split[3].length());

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
                // Is a regular point
                else if (split.length >= 3)
                {
                    p1 = makePoint(split);
                    endNode = split.length == 3;
                }
                // Uh oh, something's wrong
                else
                {
                    // Check if it's a route declaration
                    if (split.length == 1)
                    {
                        split = line.split(" ", 2);
                        if (routeTable.containsKey(split[0]))
                        {
                            throw new RouteParserException(
                                "Error while parsing route " + name +
                                "\n" + formattedLineNo(line) +
                                "\nMissing terminating line?"
                            );
                        }
                    }

                    // Exhausted all checks
                    throw new RouteParserException(
                        "Unknown error while parsing route " + name +
                        "\n" + formattedLineNo(line)
                    );
                }

                if (checkDistance)
                {
                    p2 = newRoute.getEnd();

                    if (util.checkDistance(p1, p2))
                    {
                        throw new RouteParserException(
                            "Error while parsing route " + name +
                            "\n" + formattedLineNo(line) +
                            "\nDistance between sub-route and last point is " +
                            "too far away"
                        );
                    }

                    p2 = null;
                    checkDistance = false;
                }

                newRoute.add(p1);

                if (subRoute != null)
                {
                    // Set flag to check the distance for the next point
                    checkDistance = true;

                    p1 = newRoute.getEnd();
                    p2 = subRoute.getStart();

                    if (util.checkDistance(p1, p2))
                    {
                        throw new RouteParserException(
                            "Error while parsing route " + name +
                            "\n" + formattedLineNo(line) +
                            "\nDistance between sub-route and last point is " +
                            "too far away"
                        );
                    }

                    newRoute.add(subRoute);
                    subRoute = null;
                    p2 = null;
                }

                p1 = null;
            }
        }
        catch (RouteParserException e)
        {
            throw new RouteParserException(e.getMessage());
        }

        if (! endNode)
        {
            throw new RouteParserException(
                "Error while parsing route " + name +
                "\nMissing terminating line"
            );
        }

        // Unset in progress flag
        inProgress.remove(name);

        return newRoute;
    }

    // Point factory
    public Point makePoint(String[] info) throws RouteParserException
    {
        Point newPoint;
        double latitude, longitude, altitude;

        try
        {
            latitude = Double.parseDouble(info[0]);
            longitude = Double.parseDouble(info[1]);
            altitude = Double.parseDouble(info[2]);

            if (! util.doubleRange(latitude, -90.0, 90.0) ||
                ! util.doubleRange(longitude, -180.0, 180.0))
            {
                String errMsg = "Invalid coordinates, out of range: " +
                                    info[0] + ", " +
                                    info[1] + ", " + 
                                    info[2];

                throw new RouteParserException(errMsg);
            }

            newPoint = new Point(latitude, longitude, altitude);
            if (info.length > 3 && ! info[3].isEmpty())
            {
                newPoint.setDescription(info[3]);
            }
        }
        catch (NumberFormatException e)
        {
            String errMsg = "Invalid coordinates, format error " +
                                info[0] + ", " +
                                info[1] + ", " +
                                info[2];

            throw new RouteParserException(errMsg);
        }

        return newPoint;
    }

    // Makes a map of route and sub-route declaration along with the
    // location in the file contents so that we can have O(1) access
    // to the location of the declaration
    //
    // Throws RouteParserException when detects duplicate route declarations
    // and missing sub-route declarations
    public Map<String,Integer> makeIndexes() throws RouteParserException
    {
        Map<String,Integer> routeTable = new HashMap<>();
        Set<String> subRouteSet = new HashSet<>();
        Set<String> nameSet;
        int count = 0;

        for (String line : contents)
        {
            if (! line.isEmpty())
            {
                if (Character.isLetter(line.charAt(0)))
                {
                    String[] split = line.split(" ", 2);

                    if (routeTable.containsKey(split[0]))
                    {
                        throw new RouteParserException(
                            "Duplicate route: " + split[0]
                        );
                    }

                    routeTable.put(split[0], new Integer(count));
                }
                else
                {
                    String[] split = line.split(",", 4);

                    if (split.length > 3 && split[3].charAt(0) == '*')
                    {
                        subRouteSet.add(
                            split[3].substring(1, split[3].length())
                        );
                    }
                }
            }

            count++;
        }

        nameSet = new HashSet<>(routeTable.keySet());

        if (! nameSet.containsAll(subRouteSet))
        {
            Set<String> missingSet = new HashSet<>(subRouteSet);
            String errMsg = "";

            for (String name : missingSet)
            {
                if (! nameSet.contains(name))
                {
                    errMsg += "\n    " + name;
                }
            }

            throw new RouteParserException(
                "Sub-route(s) declaration not found:" + errMsg
            );
        }

        return routeTable;
    }

    private String formattedLineNo(String line)
    {
        return String.format("%d: %s", contents.indexOf(line) + 1, line);
    }
}
