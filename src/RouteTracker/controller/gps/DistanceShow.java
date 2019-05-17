package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.view.*;

/**
 * GpsShow class that extends GpsLocator, displays the current coordinates
 * called
 * @author Julian Heng (19473701)
 **/
public class DistanceShow extends GpsLocator
{
    protected GeoUtils utils;
    protected UserInterface ui;

    protected double distance;
    protected double posAlt;
    protected double negAlt;

    protected double prevLat;
    protected double prevLong;
    protected double prevAlt;

    public DistanceShow(GeoUtils utils, UserInterface ui,
                        double distance, double posAlt, double negAlt)
    {
        this.utils = utils;
        this.distance = distance;
        this.posAlt = posAlt;
        this.negAlt = negAlt;

        // Set to the minimum double value to indicate that we have not started
        // yet
        prevLat = -Double.MAX_VALUE;
        prevLong = -Double.MAX_VALUE;
        prevAlt = -Double.MAX_VALUE;
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
        // If not the first iteration
        if (! (Double.compare(prevLat, -Double.MAX_VALUE) == 0 &&
               Double.compare(prevLong, -Double.MAX_VALUE) == 0 &&
               Double.compare(prevAlt, -Double.MAX_VALUE) == 0))
        {
            double deltaAlt;

            // Get the change in distance and deduct from total distance
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

        // Notify user for distance remaining
        ui.print("Distance remaining: %.2fm\n", distance);
        ui.print("Vertical climb remaining: %.2fm\n", posAlt);
        ui.print("Vertical descent remaining: %.2fm\n", negAlt);
    }
}
