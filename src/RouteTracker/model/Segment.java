package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;
import RouteTracker.controller.GeoUtils;

/**
 * Segment class for storing 2 PointNodes as well as the description
 * @author Julian Heng (19473701)
 **/
public class Segment implements PointNode
{
    private PointNode start;
    private PointNode end;
    private String desc;
    private double distance;

    public Segment(PointNode start, PointNode end,
                   String desc, double distance)
    {
        this.start = start;
        this.end = end;
        this.desc = desc;
        this.distance = distance;
    }

    @Override public String getName() { return ""; }
    @Override public String getDescription() { return desc; }

    // Make the get[Coords]() method call the starting node
    //
    // We aren't expecting to call these methods on segment so essentially
    // they can be treated as stub methods
    @Override
    public double getLatitude()
    {
        return getStartNode().getLatitude();
    }

    @Override
    public double getLongitude()
    {
        return getStartNode().getLongitude();
    }

    @Override
    public double getAltitude()
    {
        return getStartNode().getAltitude();
    }

    @Override
    public double getDistance() { return distance; }

    @Override
    public double getPositiveAltitude()
    {
        double posAlt = start.getPositiveAltitude() +
                        end.getPositiveAltitude();

        if (Double.compare(posAlt, 0) == 0 &&
            Double.compare(getDeltaAltitude(), 0) >= 0)
        {
            posAlt = Math.abs(getDeltaAltitude());
        }

        return posAlt;
    }

    @Override
    public double getNegativeAltitude()
    {
        double negAlt = start.getNegativeAltitude() +
                        end.getNegativeAltitude();

        if (Double.compare(negAlt, 0) == 0 &&
            Double.compare(getDeltaAltitude(), 0) <= 0)
        {
            negAlt = Math.abs(getDeltaAltitude());
        }

        return negAlt;
    }

    @Override
    public double getDeltaAltitude()
    {
        return getEndPoint().getAltitude() - getStartPoint().getAltitude();
    }

    @Override public PointNode getStartNode() { return start; }
    @Override public PointNode getEndNode() { return end; }
    @Override public Point getStartPoint() { return start.getStartPoint(); }
    @Override public Point getEndPoint() { return end.getEndPoint(); }

    @Override
    public List<Point> getAllPoints()
    {
        List<Point> all = new ArrayList<>();
        all.addAll(start.getAllPoints());
        all.addAll(end.getAllPoints());
        return all;
    }

    @Override
    public String toString()
    {
        return desc + "\n" + start.toString() + "\n" + end.toString();
    }
}
