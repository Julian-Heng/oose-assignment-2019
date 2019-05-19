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

    public Segment(GeoUtils utils, PointNode start,
                   PointNode end, String desc)
    {
        this.start = start;
        this.end = end;
        this.desc = desc;

        distance = utils.calcMetresDistance(this.start.getLatitude(),
                                            this.start.getLongitude(),
                                            this.end.getLatitude(),
                                            this.end.getLongitude());
    }

    @Override public String getName() { return ""; }
    @Override public String getDescription() { return desc; }

    // Make the get[Coords]() method call the starting node
    //
    // We aren't expecting to call these methods from segment so essentially
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
    public double getDeltaAltitude()
    {
        return getEndNode().getAltitude() - getStartNode().getAltitude();
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
