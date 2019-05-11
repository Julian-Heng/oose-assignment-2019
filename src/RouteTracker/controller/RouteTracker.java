import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.controller.option.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;
import RouteTracker.view.*;

public class RouteTracker
{
    public static void main(String[] args)
    {
        Map<String,Route> routes = new HashMap<>();

        GeoUtils utils = new GeoUtils();
        UserInterface ui = new ConsoleUI();
        Menu menu = new Menu(ui);
        menu.addOption(new GetRouteData(1, utils, routes));
        menu.addOption(new ClearRoute(2, routes));
        menu.addOption(new PrintRoute(3, routes));

        // Main program loop
        do
        {
            ui.print(menu.toString());
        } while (menu.executeOption(ui.readInteger("> ")));
    }
}
