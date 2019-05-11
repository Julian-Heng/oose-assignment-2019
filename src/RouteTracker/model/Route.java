package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;

public class Route implements PointNode
{
    private String name;
    private String desc;

    private List<PointNode> points;
    private List<Segment> segments;

    private double distance;
    private double posAlt;
    private double negAlt;
    private double deltaAlt;

    public Route(String name, String desc)
    {
        this.name = name;
        this.desc = desc;

        points = new ArrayList<>();
        segments = new ArrayList<>();

        distance = 0.0;
        posAlt = 0.0;
        negAlt = 0.0;
        deltaAlt = 0.0;
    }

    public void add(Segment s)
    {
        double tmpDeltaAlt;

        // Add segment to the segment list
        segments.add(s);

        // If points list is empty, that means that we have to add
        // both the starting and ending points in the segment
        //
        // Otherwise, we only add the end because the end of one segment
        // and the start of the next segment is the same point
        if (points.isEmpty())
        {
            add(s.getStartNode());
        }

        add(s.getEndNode());

        distance += s.getDistance();
        tmpDeltaAlt = s.getDeltaAltitude();
        deltaAlt += tmpDeltaAlt;

        if (Double.compare(tmpDeltaAlt, 0) >= 0)
        {
            posAlt += Math.abs(tmpDeltaAlt);
        }
        else
        {
            negAlt += Math.abs(tmpDeltaAlt);
        }
    }

    // An edge-case for when a route only has one point, thus unable
    // to create a segment
    public void add(PointNode n) { points.add(n); }

    // Route specific getters
    public double getPositiveAltitude() { return posAlt; }
    public double getNegativeAltitude() { return negAlt; }

    @Override public String getName() { return name; }
    @Override public String getDescription() { return desc; }

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

    @Override public double getDistance() { return distance; }
    @Override public double getDeltaAltitude() { return deltaAlt; }
    @Override public PointNode getStartNode() { return points.get(0); }

    @Override
    public PointNode getEndNode()
    {
        return points.get(points.size() - 1);
    }

    @Override
    public Point getStartPoint()
    {
        return getStartNode().getStartPoint();
    }

    @Override
    public Point getEndPoint()
    {
        return getEndNode().getEndPoint();
    }

    public String toString()
    {
        Iterator<Segment> iter = segments.iterator();
        String str;
        String segDesc;

        str = name + " [";
        str += String.format("%.2fm, %.2fm, +%.2fm, -%.2fm]: ",
                             distance, deltaAlt, posAlt, negAlt);
        str += desc + "\n{\n";

        for (PointNode n : points)
        {
            str += "    " + n.toString().replaceAll("\n", "\n    ");
            if (iter.hasNext())
            {
                str += ": " + iter.next().getDescription();
            }

            str += "\n";
        }

        return str + "}";

    }
}
