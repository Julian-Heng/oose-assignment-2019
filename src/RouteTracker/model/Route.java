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
        if (route.isEmpty())
        {
            start = n.getStart();
        }

        end = n.getEnd();
        route.add(n);
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
        String str = name + ": " + description + "\n{\n";

        for (RouteNode n : route)
        {
            str += "    " + n.toString().replaceAll("\n", "\n    ") + "\n";
        }

        return str + "}";
    }
}
