package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
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
    private GpsLocator gps;

    public Track(Map<String,Route> routes, UserInterface ui, GpsLocator gps)
    {
        super("Start tracking", "", true, routes);
        this.ui = ui;
        this.gps = gps;
    }

    // Logic actually needed for printing prompt, thus overring super's
    // getPrompt()
    @Override
    public String getPrompt()
    {
        Map<String,Route> routes = super.getRoutes();
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

    // Presumably, all output will be done within this option
    @Override
    public String doOption(String s) throws OptionException
    {
        Map<String,Route> routes = super.getRoutes();
        String out = "";
        Route r;

        double distance, posAlt, negAlt;

        if (! routes.isEmpty())
        {
            // Do A.4
            //
            // Details:
            //   1. Show the GPS location on the screen, whenever it is updated
            //   2. Show the remaining distance
            //   3. Determine which waypoint the user is up to
            //   4. Allow the user to manually indicate that they have reached
            //      a waypoint
            //
            // Discussion:
            // So we would need to a) update the GPS location by printing the
            // coordinates, as well as b) show the remaining distance. This
            // could be remedied by observers, where the action is getting new
            // coordinates.

            r = routes.get(s);
            distance = r.getDistance();
            posAlt = r.getPositiveAltitude();
            negAlt = r.getNegativeAltitude();
            ui.print(String.format("%.2fm, %.2fm, %.2fm\n",
                                   distance, posAlt, negAlt));
            for (Point p : r.getAllPoints())
            {
                System.out.println(p);
            }
        }

        return "";
    }
}
