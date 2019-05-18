package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.controller.gps.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;
import RouteTracker.view.*;

/**
 * Track option for printing a user seleceted route including
 * points, segemnts and sub-routes
 * @author Julian Heng (19473701)
 **/
public class Track extends Option
{
    private GeoUtils utils;
    private UserInterface ui;

    public Track(RouteTracker app, GeoUtils utils, UserInterface ui)
    {
        super("Start tracking", "", true, app);
        this.utils = utils;
        this.ui = ui;
    }

    // Logic actually needed for printing prompt, thus overring super's
    // getPrompt()
    @Override
    public String getPrompt()
    {
        Map<String,Route> routes = super.getApp().getRoutes();
        String prompt;

        if (routes.isEmpty())
        {
            prompt = "No routes created, press enter to go back to menu";
        }
        else
        {
            prompt = "\nRoutes:\n    " +
                     String.join("\n    ", routes.keySet()) +
                     "\n\nInput route name";
        }

        return prompt;
    }

    // Presumably, all output will be done within this option as the
    // device that provides the coordinates in GpsLocator calls the hook
    // methods which work independently to each other
    //
    // Also note: This method is testing code, this option is expected
    // to be replaced by a proper implementation of an actual Tracking
    // option
    @Override
    public String doOption(String s) throws OptionException
    {
        Map<String,Route> routes = super.getApp().getRoutes();
        List<Point> points;
        List<GpsLocator> trackers = new ArrayList<>();
        String out = "";
        Route r;

        double prevLat, prevLong, prevAlt;
        double nextLat, nextLong, nextAlt;
        double deltaLat, deltaLong, deltaAlt;

        prevLat = prevLong = prevAlt = -Double.MAX_VALUE;
        nextLat = nextLong = nextAlt = -Double.MAX_VALUE;

        if (! routes.isEmpty())
        {
            boolean first = true;
            r = routes.get(s);
            points = r.getAllPoints();

            trackers.add(new DistanceShow(utils, ui, r));
            trackers.add(new GpsShow(ui));

            // Call getAllPoints() again because it messes with the test
            // code
            trackers.add(new WaypointShow(utils, ui, r.getAllPoints()));

            for (Point p : points)
            {
                if (first)
                {
                    prevLat = p.getLatitude();
                    prevLong = p.getLongitude();
                    prevAlt = p.getAltitude();
                    first = false;
                    continue;
                }

                nextLat = p.getLatitude();
                nextLong = p.getLongitude();
                nextAlt = p.getAltitude();

                // Fake the next coordinates
                deltaLat = (nextLat - prevLat) / 5;
                deltaLong = (nextLong - prevLong) / 5;
                deltaAlt = (nextAlt - prevAlt) / 5;

                for (int i = 0; i < 6; i++)
                {
                    for (GpsLocator tracker : trackers)
                    {
                        ui.print("========================================\n");
                        ui.print((Object)tracker.getClass().getName() + "\n");
                        ui.print("========================================\n");
                        tracker.locationReceived(prevLat,
                                                 prevLong,
                                                 prevAlt);
                        ui.print("\n");
                    }
                    // Begin calling the trackers using the provided
                    // coordinates
                    prevLat += deltaLat;
                    prevLong += deltaLong;
                    prevAlt += deltaAlt;
                }

                prevLat = nextLat;
                prevLong = nextLong;
                prevAlt = nextAlt;

                ui.print("\n\n");
            }
        }

        return "";
    }
}
