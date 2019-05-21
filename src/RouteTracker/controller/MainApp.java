import RouteTracker.controller.GeoUtils;
import RouteTracker.controller.RouteTracker;
import RouteTracker.controller.option.*;
import RouteTracker.view.ConsoleUI;
import RouteTracker.view.Menu;
import RouteTracker.view.UserInterface;

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
        app.addOption(new Track(app, utils, app.getUI()));

        // Begin
        app.run();
    }
}
