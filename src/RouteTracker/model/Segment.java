package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;

public class Segment
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
}
