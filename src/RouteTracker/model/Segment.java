package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;

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

    @Override
    public String getName()
    {
        return "";
    }

    @Override
    public String getDescription()
    {
        return desc;
    }

    @Override
    public PointNode getStartNode()
    {
        return start;
    }

    @Override
    public PointNode getEndNode()
    {
        return end;
    }

    @Override
    public Point getStartPoint()
    {
        return start.getStartPoint();
    }

    @Override
    public Point getEndPoint()
    {
        return end.getEndPoint();
    }
}
