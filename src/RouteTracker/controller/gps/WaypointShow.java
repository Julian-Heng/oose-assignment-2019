package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;
import RouteTracker.view.*;

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

    @Override
    protected void locationReceived(double latitude,
                                    double longitude,
                                    double altitude)
    {
        double distance;
        double deltaAlt;

        distance = utils.calcMetresDistance(latitude, longitude,
                                            next.getLatitude(),
                                            next.getLongitude());
        deltaAlt = Math.abs(altitude - next.getAltitude());

        if (Double.compare(distance, 10) <= 0 &&
            Double.compare(deltaAlt, 2) <= 0)
        {
            ui.print("Reached waypoint: " + next.toString() + "\n");
            next = points.remove(0);
        }
        else
        {
            ui.print("Current waypoint: " + curr.toString() + "\n");
        }

        ui.print("Next waypoint: " + next.toString() + "\n");
    }
}
