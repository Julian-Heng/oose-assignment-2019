package RouteTracker.controller.option;

import java.util.Map;

import RouteTracker.controller.GeoUtils;
import RouteTracker.controller.RouteTracker;
import RouteTracker.controller.gps.TrackTest;
import RouteTracker.model.Route;
import RouteTracker.model.TrackData;
import RouteTracker.model.exception.OptionException;
import RouteTracker.view.UserInterface;

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
     **/
    @Override
    public String doOption(String s) throws OptionException
    {
        Map<String,Route> routes = super.getApp().getRoutes();

        if (! routes.isEmpty())
        {
            if (! routes.containsKey(s))
            {
                throw new OptionException("\"" + s + "\" does not exist");
            }

            TrackData data = new TrackData(utils, ui, routes.get(s));
            TrackTest tracker = new TrackTest(data);
            tracker.run();
        }

        return "";
    }
}
