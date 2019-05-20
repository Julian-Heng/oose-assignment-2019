package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.view.*;

/**
 * WaypointShow class to show the current and next waypoint locations
 * @author Julian Heng (19473701)
 **/
public class WaypointShow extends GpsLocator
{
    private TrackData data;

    public WaypointShow(TrackData data)
    {
        this.data = data;
    }

    /**
     * Check if the new coordinates are closer to the next point in route
     *
     * @param latitude new latitude value
     * @param longitude new longitude value
     * @param altitude new altitude value
     **/
    @Override
    public void locationReceived(double latitude,
                                 double longitude,
                                 double altitude)
    {
        GeoUtils utils = data.getUtils();
        UserInterface ui = data.getUI();
        List<Point> points = data.getPoints();

        Point curr = data.getCurr();
        Point next = data.getNext();

        double distance;
        double deltaAlt;

        // Get distance to next point
        distance = utils.calcMetresDistance(latitude, longitude,
                                            next.getLatitude(),
                                            next.getLongitude());
        deltaAlt = Math.abs(altitude - next.getAltitude());

        // If the next waypoint is within 10m horizontally and 2m
        // vertically
        if (Double.compare(distance, 10) <= 0 &&
            Double.compare(deltaAlt, 2) <= 0)
        {
            // If there is no more points
            if (points.isEmpty())
            {
                ui.print("Reached end of route\n");
            }
            else
            {
                // Notify user
                ui.print("Reached waypoint: " + next.toString() + "\n");

                // Move next waypoint to current and get the next waypoint
                // in the list
                data.setNext();
                next = data.getNext();
            }
        }
        else
        {
            ui.print("Current waypoint: " + curr.toString() + "\n");
        }

        if (! points.isEmpty())
        {
            ui.print("Next waypoint: " + next.toString() + "\n");
        }
    }
}
