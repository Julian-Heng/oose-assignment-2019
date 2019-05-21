package RouteTracker.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Route class that implements PointNode, able to store points and routes as
 * well as segments
 *
 * @author Julian Heng (19473701)
 **/
public class Route implements PointNode
{
    private String name;
    private String desc;

    // Containers for points and segments
    private List<PointNode> points;
    private List<Segment> segments;

    // Route distance statistics
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

    /**
     * A route specific method to add a new segment as other implementations
     * of PointNode does not have a list of segments
     * @param s The segment to be added
     **/
    public void add(Segment s)
    {
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

        // Update route distance statistics
        distance += s.getDistance();
        deltaAlt += s.getDeltaAltitude();
        posAlt += s.getPositiveAltitude();
        negAlt += s.getNegativeAltitude();
    }

    /**
     * Edge case for adding just a single PointNode
     * @param n The PointNode to be added
     **/
    public void add(PointNode n) { points.add(n); }


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
    @Override public double getPositiveAltitude() { return posAlt; }
    @Override public double getNegativeAltitude() { return negAlt; }
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
        // Recusive call
        return getStartNode().getStartPoint();
    }

    @Override
    public Point getEndPoint()
    {
        // Recusive call
        return getEndNode().getEndPoint();
    }

    /**
     * Aggregate all points in the points list into a single list, including
     * all subroutes
     * @return A list containing of points
     **/
    @Override
    public List<Point> getAllPoints()
    {
        List<Point> all = new ArrayList<>();
        points.forEach((p)->all.addAll(p.getAllPoints()));
        return all;
    }

    public String toString()
    {
        // Iterator to iterate through segments list along side the point list
        Iterator<Segment> iter = segments.iterator();
        String str;

        // Route header
        str = String.format("%s [%.2fm, %.2fm, +%.2fm, -%.2fm]: \n{\n",
                            name, distance, deltaAlt, posAlt, negAlt);

        for (PointNode n : points)
        {
            // Recusive call
            str += "    " + n.toString().replaceAll("\n", "\n    ");
            if (iter.hasNext())
            {
                str += ": " + iter.next().getDescription();
            }
            str += "\n";
        }
        str += "}";

        return str;
    }
}
