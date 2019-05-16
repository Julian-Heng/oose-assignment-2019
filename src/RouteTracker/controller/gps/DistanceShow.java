package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;
import RouteTracker.view.*;

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

    @Override
    protected void locationReceived(double latitude,
                                    double longitude,
                                    double altitude)
    {
        if (! (Double.compare(prevLat, -Double.MAX_VALUE) == 0 &&
               Double.compare(prevLong, -Double.MAX_VALUE) == 0 &&
               Double.compare(prevAlt, -Double.MAX_VALUE) == 0))
        {
            double deltaAlt;

            distance -= utils.calcMetresDistance(latitude, longitude,
                                                prevLat, prevLong);
            deltaAlt = Math.abs(altitude - prevAlt);

            if (Double.compare(deltaAlt, 0) >= 0)
            {
                posAlt -= deltaAlt;
            }
            else
            {
                negAlt -= deltaAlt;
            }
        }

        prevLat = latitude;
        prevLong = longitude;
        prevAlt = altitude;

        ui.print("Distance remaining: %.2fm\n", distance);
        ui.print("Vertical climb remaining: %.2fm\n", posAlt);
        ui.print("Vertical descent remaining: %.2fm\n", negAlt);
    }
}
