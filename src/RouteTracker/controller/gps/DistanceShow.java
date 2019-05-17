package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.view.*;

/**
 * DistanceShow class that extends the GpsLocator class. Prints the amount of
 * distance left for the current segment
 * @author Julian Heng (19473701)
 **/
public class DistanceShow extends GpsLocator
{
    protected GeoUtils utils;
    protected UserInterface ui;
    protected Route route;
    protected Point next;

    // Stats for current route
    protected double distance;
    protected double posAlt;
    protected double negAlt;

    // Stats for previous point
    protected double prevLat;
    protected double prevLong;
    protected double prevAlt;

    // Stats for current segment
    protected double distLeft;
    protected double altLeft;

    public DistanceShow(GeoUtils utils, UserInterface ui, Route route)
    {
        this.utils = utils;
        this.ui = ui;
        this.route = route;

        // Assuming that the user is already on the first point, thue
        // get the seconds point in the route
        next = route.getAllPoints().get(1);

        distance = route.getDistance();
        posAlt = route.getPositiveAltitude();
        negAlt = route.getNegativeAltitude();

        // Set to minimum value to indicated that we have not started yet
        prevLat = -Double.MAX_VALUE;
        prevLong = -Double.MAX_VALUE;
        prevAlt = -Double.MAX_VALUE;

        distLeft = -Double.MAX_VALUE;
        altLeft = -Double.MAX_VALUE;
    }

    /**
     * Calculates the distance between the last recorded point and
     * the new points
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
        double deltaAlt;

        // Get the distance remaining for the current segment
        distLeft = utils.calcMetresDistance(latitude, longitude,
                                             next.getLatitude(),
                                             next.getLongitude());
        altLeft = next.getAltitude() - altitude;

        if (! (Double.compare(prevLat, -Double.MAX_VALUE) == 0 &&
               Double.compare(prevLong, -Double.MAX_VALUE) == 0 &&
               Double.compare(prevAlt, -Double.MAX_VALUE) == 0))
        {

            // Get the distance remaining for the entire route
            distance -= utils.calcMetresDistance(latitude, longitude,
                                                prevLat, prevLong);
            deltaAlt = altitude - prevAlt;

            // Check if the distance in altitude is positive or negative
            if (Double.compare(deltaAlt, 0) >= 0)
            {
                posAlt -= Math.abs(deltaAlt);
            }
            else
            {
                negAlt -= Math.abs(deltaAlt);
            }
        }

        // Save new coordinates for next method call
        prevLat = latitude;
        prevLong = longitude;
        prevAlt = altitude;

        // Notify user for distance remaining in segment
        ui.print("Distance remaining in segment: %.2fm", distLeft);
        ui.print("Altitude remaining in segment: %.2fm", altLeft);

        // Notify user for distance remaining in route
        ui.print("Distance remaining in route: %.2fm\n", distance);
        ui.print("Vertical climb remaining: %.2fm\n", posAlt);
        ui.print("Vertical descent remaining: %.2fm\n", negAlt);
    }

    /**
     * Since WaypointShow class exists, which shows which waypoint the user
     * is up to and which waypoint is next, we would rather have the next
     * Point be set externally as we do not want this class to handle that
     * responsibility of determining which point in the route the user is at
     * @param p the next point to calculate distance with
     **/
    protected void setNext(Point p) { next = p; }
}
