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
    private Point start;
    private Point end;

    private double distance;
    private double posAlt;
    private double negAlt;
    private double deltaAlt;

    // Mutable ArrayList
    private List<RouteNode> route;

    // Routes can be mutable, but the name (key) cannot be mutable
    private Map<String,Route> subRoutes;

    public Route(String name, String description)
    {
        this.name = name;
        this.description = description;

        this.start = null;
        this.end = null;

        this.distance = 0.0;
        this.posAlt = 0.0;
        this.negAlt = 0.0;
        this.deltaAlt = 0.0;

        route = new ArrayList<>();
        subRoutes = new HashMap<>();
    }

    public void add(RouteNode n)
    {
        if (route.isEmpty())
        {
            start = n.getStart();
        }

        end = n.getEnd();
        route.add(n);
    }

    public void updateDistance(double distance)
    {
        this.distance += distance;
    }

    public void updateAltitude(double deltaAlt)
    {
        if (Double.compare(deltaAlt, 0.0) < 0)
        {
            negAlt += Math.abs(deltaAlt);
        }
        else
        {
            posAlt += Math.abs(deltaAlt);
        }

        this.deltaAlt = posAlt - negAlt;
    }

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
        return start;
    }

    @Override
    public Point getEnd()
    {
        return end;
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
        String str = String.format("%s [%.2fm, %.2fm, +%.2fm, -%.2fm]: %s\n{\n",
                                   name, distance,
                                   deltaAlt, posAlt, negAlt,
                                   description);

        for (RouteNode n : route)
        {
            str += "    " + n.toString().replaceAll("\n", "\n    ") + "\n";
        }

        return str + "}";
    }
}
