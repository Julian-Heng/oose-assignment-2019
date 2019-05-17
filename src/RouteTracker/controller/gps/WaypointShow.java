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
    protected GeoUtils utils;
    protected UserInterface ui;

    protected List<Point> points;
    protected Point curr;
    protected Point next;

    public WaypointShow(GeoUtils utils, UserInterface ui, List<Point> points)
    {
        this.utils = utils;
        this.ui = ui;
        this.points = points;

        curr = points.remove(0);
        next = points.remove(0);
    }

    /**
     * Check if the new coordinates are closer to the next point in route
     *
     * @param latitude new latitude value
     * @param longitude new longitude value
     * @param altitude new altitude value
     **/
    @Override
    protected void locationReceived(double latitude,
                                    double longitude,
                                    double altitude)
    {
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
                curr = next;
                next = points.remove(0);
            }
        }
        else
        {
            ui.print("Current waypoint: " + curr.toString() + "\n");
        }

        ui.print("Next waypoint: " + next.toString() + "\n");
    }
}
