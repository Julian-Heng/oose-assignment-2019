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
        int index = 0;

        for (String line : contents)
        {
            if (! line.isEmpty() &&
                Character.isLetter(line.charAt(0)))
            {
                String[] split = line.split(" ");
                if (! routes.containsKey(split[0]))
                {
                    routes.put(split[0], makeRoute(routes, inProgress, index));
                }
            }

            index++;
        }

        return routes;
    }

    // Route factory
    public Route makeRoute(Map<String,Route> routes,
                           Set<String> inProgress,
                           int start) throws RouteParserException
    {
        Route newRoute = null;

        ListIterator<String> iter = contents.listIterator(start + 1);
        String[] split;
        String line;
        boolean valid = true;

        String name;
        String description;

        split = contents.get(start).split(" ", 2);
        name = split[0];
        description = split[1];

        inProgress.add(name);
        newRoute = new Route(name, description);

        try
        {
            while (iter.hasNext() && valid)
            {
                line = iter.next();
                if (! line.isEmpty())
                {
                    split = line.split(",", 4);

                    // Is a sub-route
                    if (split.length > 3 && split[3].charAt(0) == '*')
                    {
                        String subRouteName;
                        Route subRoute;

                        subRouteName = split[3].substring(1, split[3].length());

                        // Recursion check
                        if (subRouteName.equals(name))
                        {
                            throw new RouteParserException(
                                "Sub-route is attempting to add itself"
                            );
                        }
                        else if (inProgress.contains(subRouteName))
                        {
                            throw new RouteParserException(
                                "Sub-route is attempting to add a route " +
                                "that depends on itself"
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
                                                 getRouteIndex(subRouteName));
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
                        valid = false;
                    }
                }
            }
        }
        catch (RouteParserException e)
        {
            throw new RouteParserException(e.getMessage());
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

    public int getRouteIndex(String name)
    {
        int index = 0;
        for (String line : contents)
        {
            if (line.startsWith(name))
            {
                break;
            }
            else
            {
                index++;
            }
        }
        return index;
    }

    public List<String> getContents()
    {
        return contents;
    }

    private boolean doubleRange(double num, double low, double high)
    {
        return ((num > low)  || Math.abs(num - low)  < TOLERANCE) &&
               ((high > num) || Math.abs(num - high) < TOLERANCE);
    }
}
