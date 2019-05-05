package RouteTracker.controller;

import java.io.IOException;
import java.util.*;

import RouteTracker.model.*;

public class RouteParser
{
    private String filename;
    private List<String> contents;
    private static final double TOLERANCE = 0.0000001;

    public RouteParser(String filename)
    {
        this.filename = filename;
        this.contents = new ArrayList<>();
    }

    public void readFile() throws RouteParserException
    {
        FileIO file = new FileIO();

        try
        {
            contents = file.readFile(filename);
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

        ListIterator<String> iter;
        String[] split;
        String line;
        boolean checkDistance;
        boolean endNode;

        String name;
        String description;

        iter = contents.listIterator(start + 1);
        checkDistance = false;
        endNode = false;

        split = contents.get(start).split(" ", 2);
        name = split[0];
        description = split[1];

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
                    String subRouteName;
                    Route subRoute;

                    subRouteName = split[3].substring(1, split[3].length());

                    // Recursion check
                    if (inProgress.contains(subRouteName))
                    {
                        throw new RouteParserException(
                            "Sub-route is attempting to add a route that " +
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
                        subRoute = makeRoute(routes,
                                             inProgress,
                                             routeTable,
                                             routeTable.get(subRouteName));
                        routes.put(subRouteName, subRoute);
                    }

                    newRoute.add(subRoute);
                }
                // Is a regular point
                else if (split.length > 3)
                {
                    newRoute.add(makePoint(split));
                }
                else if (split.length == 3)
                {
                    newRoute.add(makePoint(split));
                    endNode = true;
                }
                // Uh oh, something's wrong
                else
                {
                    // Check if it's a route declaration
                    if (split.length == 1)
                    {
                        split = line.split(" ", 2);
                        if (split.length == 2)
                        {
                            throw new RouteParserException(
                                "Error parsing line: " + line +
                                "\nMissing terminating line?"
                            );
                        }
                    }

                    // Exhausted all checks
                    throw new RouteParserException(
                        "Unknown error parsing line: " + line
                    );
                }
            }
        }
        catch (RouteParserException e)
        {
            throw new RouteParserException(e.getMessage());
        }

        if (! endNode)
        {
            throw new RouteParserException(
                "Route " + name + " does not have a terminating line"
            );
        }

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

            if (! doubleRange(latitude, -90.0, 90.0) ||
                ! doubleRange(longitude, -180.0, 180.0))
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
            String errMsg = "Invalid coordinates format: " +
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

    private boolean doubleRange(double num, double low, double high)
    {
        return ((num > low)  || Math.abs(num - low)  < TOLERANCE) &&
               ((high > num) || Math.abs(num - high) < TOLERANCE);
    }
}
