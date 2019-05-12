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
        List<Option> options = new ArrayList<>();

        GeoUtils utils = new GeoUtils();
        UserInterface ui = new ConsoleUI();
        Menu menu = new Menu(ui);

        // Make options collection so that we can reference it to chain(?)
        // option's doOption() method
        options.add(new GetRouteData(utils, routes));
        options.add(new ClearRoute(routes));
        options.add(new PrintAllRoute(routes));
        options.add(new PrintRouteDetailed(routes));
        options.add(new PrintAllRouteDetailed(routes));

        options.forEach((v)->menu.addOption(v));

        try
        {
            // Testing purpose code {{{

            // Make the routes for either the program arguments or hardcoded
            // String
            options.get(0).doOption(
                args[0] != null ? args[0] : "./dist/valid_1"
            );

            // Print all the routes as per specification
            ui.print(options.get(2).doOption(""));

            // }}} End testing code
        }
        catch (OptionException e)
        {
            ui.printError(e.getMessage() + "\n\n");
        }

        // Main program loop
        do
        {
            ui.print(menu.toString());
        } while (menu.executeOption(ui.readInteger("> ")));
    }
}
