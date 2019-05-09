package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;

public class Route implements PointNode
{
    String name;
    String desc;
    private List<PointNode> points;
    private List<Segment> segments;

    public Route(String name, String desc)
    {
        this.name = name;
        this.desc = desc;
        points = new ArrayList<>();
        segments = new ArrayList<>();
    }

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
            points.add(s.getStartNode());
        }

        points.add(s.getEndNode());
    }

    @Override
    public String getName()
    {
        return name;
    }

    @Override
    public String getDescription()
    {
        return desc;
    }

    @Override
    public PointNode getStartNode()
    {
        return points.get(0);
    }

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
}
