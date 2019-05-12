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

            r = routes.get(s);
            distance = r.getDistance();
            posAlt = r.getPositiveAltitude();
            negAlt = r.getNegativeAltitude();
            ui.print(String.format("%.2fm, %.2fm, %.2fm\n",
                                   distance, posAlt, negAlt));
        }

        return "";
    }
}
