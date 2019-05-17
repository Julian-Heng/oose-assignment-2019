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

        if (! routes.isEmpty())
        {
            r = routes.get(s);

            for (Point p : r.getAllPoints())
            {
                System.out.println(p);
            }
        }

        return "";
    }
}
