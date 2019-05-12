package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;

/**
 * Point class that implements a PointNode, stores the coordinates only
 * @author Julian Heng (19473701)
 **/
public class Point implements PointNode
{
    private double latitude;
    private double longitude;
    private double altitude;

    public Point(double latitude, double longitude, double altitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    @Override public String getName() { return ""; }
    @Override public String getDescription() { return ""; }
    @Override public double getLatitude() { return latitude; }
    @Override public double getLongitude() { return longitude; }
    @Override public double getAltitude() { return altitude; }
    @Override public double getDistance() { return 0.0; }
    @Override public double getDeltaAltitude() { return 0.0; }
    @Override public PointNode getStartNode() { return this; }
    @Override public PointNode getEndNode() { return this; }
    @Override public Point getStartPoint() { return this; }
    @Override public Point getEndPoint() { return this; }

    @Override
    public List<Point> getAllPoints()
    {
        List<Point> all = new ArrayList<>();
        all.add(this);
        return all;
    }

    public String toString()
    {
        return latitude + ", " + longitude + ", " + altitude;
    }
}
