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

    /**
     * Presumably, all output will be done within this option as the
     * device that provides the coordinates in GpsLocator calls the hook
     * methods which work independently to each other
     *
     * Also note: This method is testing code, this option is expected
     * to be replaced by a proper implementation of an actual Tracking
     * option
     *
     * Also note note: ReachedPoint is not going to be tested because
     *                 that requires user action and this test is automated
     *
     * Also note note note: The data is _not_ shared among the subclasses that
     *                      extends GpsLocator as it is presumed that they are
     *                      shared using threads(?) and that constructing them
     *                      is good enough to make them run on their own
     *                      threads. So to make this easier on me, all the
     *                      subclasses have their own copy of their own points
     *                      and their own local variables like next and curr
     *                      (both of which is used within DistanceShow and
     *                      WaypointShow). Yes, more work is needed on the
     *                      subclasses when properly implementing it, but
     *                      I need these classes to work with the provided
     *                      GpsLocator stub, so some compromises is needed
     **/
    @Override
    public String doOption(String s) throws OptionException
    {
        Map<String,Route> routes = super.getApp().getRoutes();

        if (! routes.isEmpty())
        {
            TrackTest tracker = new TrackTest(utils, ui, routes.get(s));
            tracker.run();
        }

        return "";
    }
}
