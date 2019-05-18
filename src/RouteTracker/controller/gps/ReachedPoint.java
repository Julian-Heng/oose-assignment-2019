package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.view.*;

/**
 * ReachedPoint class that extends the GpsLocator class. Takes in coordinates
 * to which the user has claimed that they have already reached a waypoint
 * @author Julian Heng (19473701)
 **/
public class ReachedPoint extends GpsLocator
{
    private UserInterface ui;
    private List<Point> points;
    private Map<Point,Boolean> pointTable;

    public ReachedPoint(GeoUtils utils, UserInterface ui, Route route)
    {
        this.ui = ui;
        points = route.getAllPoints();
        pointTable = new HashMap<>();
        points.forEach((v)->pointTable.put(v, false));
    }

    /**
     * When this function is called, it is asumed that an external class is
     * invoked. The user indicates they they reached waypoint, which this
     * method is invoked with the coordinates of the point. Because we're
     * overriding the GpsLocation method we cannot take a Point object (because
     * that would be too easy, duh), and instead have to deal with coordinates
     *
     * @param latitude user's current latitude
     * @param longitude user's current longitude
     * @param altitude user's current altitude
     **/
    @Override
    public void locationReceived(double latitude,
                                 double longitude,
                                 double altitude)
    {
        double distance;

        for (Point p : points)
        {
            if (! pointTable.get(p))
            {
                if (Double.compare(latitude, p.getLatitude()) == 0 &&
                    Double.compare(longitude, p.getLongitude()) == 0 &&
                    Double.compare(altitude, p.getAltitude()) == 0)
                {
                    pointTable.put(p, true);
                }

                break;
            }
        }
    }
}
