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
public class RouteTracker
{
    public static void main(String[] args)
    {
        Map<String,Route> routes = new HashMap<>();

        GeoUtils utils = new GeoUtils();
        UserInterface ui = new ConsoleUI();
        Menu menu = new Menu(ui);
        menu.addOption(new GetRouteData(utils, routes));
        menu.addOption(new ClearRoute(routes));
        menu.addOption(new PrintAllRoute(routes));
        menu.addOption(new PrintRouteDetailed(routes));
        menu.addOption(new PrintAllRouteDetailed(routes));

        // Main program loop
        do
        {
            ui.print(menu.toString());
        } while (menu.executeOption(ui.readInteger("> ")));
    }
}
