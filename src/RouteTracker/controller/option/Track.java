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
    private UserInterface ui;

    public Track(RouteTracker app, UserInterface ui)
    {
        super("Start tracking", "", true, app);
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
        String out = "";
        Route r;

        double prevLat, prevLong, prevAlt;
        double nextLat, nextLong, nextAlt;
        double deltaLat, deltaLong, deltaAlt;

        prevLat = prevLong = prevAlt = -Double.MAX_VALUE;
        nextLat = nextLong = nextAlt = -Double.MAX_VALUE;

        if (! routes.isEmpty())
        {
            r = routes.get(s);

            r.getAllPoints().forEach((v)->ui.print(v.toString() + "\n"));
            ui.print("\n");

            for (Point p : r.getAllPoints())
            {
                nextLat = p.getLatitude();
                nextLong = p.getLongitude();
                nextAlt = p.getAltitude();

                // Skip over first iteration or same points
                if ((Double.compare(prevLat, -Double.MAX_VALUE) == 0 &&
                     Double.compare(prevLong, -Double.MAX_VALUE) == 0 &&
                     Double.compare(prevAlt, -Double.MAX_VALUE) == 0) ||
                    (Double.compare(prevLat, nextLat) == 0 &&
                     Double.compare(prevLong, nextLong) == 0 &&
                     Double.compare(prevAlt, nextAlt) == 0))
                {
                    prevLat = nextLat;
                    prevLong = nextLong;
                    prevAlt = nextAlt;

                    continue;
                }

                // Fake the next coordinates
                deltaLat = (nextLat - prevLat) / 5;
                deltaLong = (nextLong - prevLong) / 5;
                deltaAlt = (nextAlt - prevAlt) / 5;

                for (int i = 0; i < 5; i++)
                {
                    // Begin calling the trackers using the provided
                    // coordinates
                    prevLat += deltaLat;
                    prevLong += deltaLong;
                    prevAlt += deltaAlt;
                }

                prevLat = nextLat;
                prevLong = nextLong;
                prevAlt = nextAlt;
            }
        }

        return "";
    }
}
