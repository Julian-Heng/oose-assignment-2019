package RouteTracker.model;

import java.util.List;
import java.util.ArrayList;

import RouteTracker.controller.GeoUtils;

public class Route implements RouteNode
{
    private String name;
    private String description;
    private List<RouteNode> route;

    private static final double TOLERANCE = 0.000006;

    public Route(String name, String description)
    {
        this.name = name;
        this.description = description;
        route = new ArrayList<>();
    }

    /*
    public void add(RouteNode n)
    {
        if (n instanceof Route)
        {
            RouteNode last = this.getEnd();
        }

        route.add(n);
    }
    */

    /*
    public void add(RouteNode n)
    {
        route.add(n);
    }

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
        return route.get(0).getStart().getStart();
    }

    @Override
    public Point getEnd()
    {
        return route.get(route.size() - 1).getEnd();
    }

    @Override
    public int getSize()
    {
        return route.size();
    }
}
