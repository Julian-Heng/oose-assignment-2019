package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;
import RouteTracker.controller.GeoUtils;

public class Segment implements PointNode
{
    private PointNode start;
    private PointNode end;
    private String desc;

    public Segment(PointNode start, PointNode end, String desc)
    {
        this.start = start;
        this.end = end;
        this.desc = desc;
    }

    @Override public String getName() { return ""; }
    @Override public String getDescription() { return desc; }

    // Make the get[Coords]() method call the starting node
    // We aren't expecting to call these methods from segment
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
    public double getDistance()
    {
        GeoUtils utils = new GeoUtils();
        return utils.calcMetresDistance(start.getLatitude(),
                                        start.getLongitude(),
                                        end.getLatitude(),
                                        end.getLongitude());
    }

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
    public String toString()
    {
        return desc + "\n" + start.toString() + "\n" + end.toString();
    }
}
