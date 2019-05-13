import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.controller.option.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;
import RouteTracker.view.*;

/**
 * RouteTracker Application
 * @author Julian Heng (19473701)
 **/
public class MainApp
{
    public static void main(String[] args)
    {
        RouteTracker app = new RouteTracker(args, new Menu(), new ConsoleUI());

        // Needed for creating options
        GeoUtils utils = new GeoUtils();

        // Set options
        app.addOption(new GetRouteData(utils, app));
        app.addOption(new ClearRoute(app));
        app.addOption(new PrintAllRoute(app));
        app.addOption(new PrintRouteDetailed(app));
        app.addOption(new PrintAllRouteDetailed(app));
        app.addOption(new Track(app, app.getUI(), new GpsStub()));
        app.run();
    }
}
