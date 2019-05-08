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
}
