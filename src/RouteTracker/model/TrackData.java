package RouteTracker.model;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.controller.gps.*;
import RouteTracker.model.*;
import RouteTracker.view.*;

/**
 * TrackData class to hold shared information relating to the gps trackers
 * @author Julian Heng (19473701)
 **/
public class TrackData
{
    private GeoUtils utils;
    private UserInterface ui;
    private Route route;

    private Map<Point,Boolean> pointTable;
    private List<Point> points;
    private Point curr;
    private Point next;

    public TrackData(GeoUtils utils, UserInterface ui, Route route)
    {
        this.utils = utils;
        this.ui = ui;
        this.route = route;

        points = this.route.getAllPoints();
        pointTable = new HashMap<>();
        points.forEach((v)->pointTable.put(v, false));

        curr = points.remove(0);
        next = points.remove(0);
    }

    public GeoUtils getUtils() { return utils; }
    public UserInterface getUI() { return ui; }
    public Route getRoute() { return route; }
    public Map<Point,Boolean> getPointTable() { return pointTable; }
    public List<Point> getPoints() { return points; }
    public Point getCurr() { return curr; }
    public Point getNext() { return next; }

    public void setNext()
    {
        curr = next;

        if (! points.isEmpty())
        {
            next = points.remove(0);
        }
    }
}
