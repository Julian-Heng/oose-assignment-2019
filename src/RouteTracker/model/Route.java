package RouteTracker.model;

import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;

import RouteTracker.controller.GeoUtils;

public class Route implements RouteNode
{
    private String name;
    private String description;

    // Mutable ArrayList
    private List<RouteNode> route;

    // Routes can be mutable, but the name (key) cannot be mutable
    private Map<String,Route> subRoutes;

    public Route(String name, String description)
    {
        this.name = name;
        this.description = description;

        route = new ArrayList<>();
        subRoutes = new HashMap<>();
    }

    public void add(RouteNode n)
    {
        route.add(n);
    }

    /*
    public void addRoute(Route r, RouteNode n) throws RouteAddException
    {
        Point thisLast = getEnd();
        Point nextFirst = r.getStart();

        double thisLat = thisLast.getLatitude();
        double thisLong = thisLast.getLongitude();
        double thisAlt = thisLast.getAltitude();

        double nextLat = nextFirst.getLatitude();
        double nextLong = nextFirst.getLongitude();
        double nextAlt = nextFirst.getAltitude();

        double distance = new GeoUtils().calcDistance(thisLat, thisLong,
                                                      nextLat, nextLong);

        if (Math.abs(distance - 10) > TOLERANCE)
        {
            throw new RouteAddException(
                "Subroute's starting point is not within 10m horizontally"
            );
        }
        else if (Math.abs(thisAlt - nextAlt) < TOLERANCE)
        {
            throw new RouteAddException(
                "Subroute's starting point is not within 2m vertically"
            );
        }
        else
        {
            route.add(r);
        }
    }
    */

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public Point getStart()
    {
        return route.get(0).getStart();
    }

    @Override
    public Point getEnd()
    {
        return route.get(route.size() - 1).getEnd();
    }

    @Override
    public int getSize()
    {
        int size = 0;

        for (RouteNode n : route)
        {
            size += n.getSize();
        }

        return size;
    }

    public String toString()
    {
        String str = name + ": " + description + "\n{\n";

        for (RouteNode n : route)
        {
            str += "    " + n.toString().replaceAll("\n", "\n    ") + "\n";
        }

        return str + "}";
    }
}
