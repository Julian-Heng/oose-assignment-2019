package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;

public class Point implements PointNode
{
    private Map<String,Double> coords;

    public Point(double latitude, double longitude, double altitude)
    {
        coords = new LinkedHashMap<>();
        coords.put("Latitude", latitude);
        coords.put("Longitude", longitude);
        coords.put("Altitude", altitude);
    }

    @Override
    public String getName()
    {
        return "";
    }

    @Override
    public String getDescription()
    {
        return "";
    }

    @Override
    public double getLatitude()
    {
        return coords.get("Latitude");
    }

    @Override
    public double getLongitude()
    {
        return coords.get("Longitude");
    }

    @Override
    public double getAltitude()
    {
        return coords.get("Altitude");
    }

    @Override
    public double getDistance()
    {
        return 0.0;
    }

    public double getDeltaAltitude()
    {
        return 0.0;
    }

    @Override
    public PointNode getStartNode()
    {
        return this;
    }

    @Override
    public PointNode getEndNode()
    {
        return this;
    }

    @Override
    public Point getStartPoint()
    {
        return this;
    }

    @Override
    public Point getEndPoint()
    {
        return this;
    }

    public String toString()
    {
        return coords.get("Latitude") + ", " +
               coords.get("Longitude") + ", " +
               coords.get("Altitude");
    }
}
