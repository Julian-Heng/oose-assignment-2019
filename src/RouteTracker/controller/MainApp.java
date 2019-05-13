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
        RouteTracker app = new RouteTracker();
        List<Option> options = new ArrayList<>();

        GeoUtils utils = new GeoUtils();
        UserInterface ui = new ConsoleUI();
        Menu menu = new Menu(ui);

        // Make options collection so that we can reference it to chain(?)
        // option's doOption() method
        options.add(new GetRouteData(utils, app));
        options.add(new ClearRoute(app));
        options.add(new PrintAllRoute(app));
        options.add(new PrintRouteDetailed(app));
        options.add(new PrintAllRouteDetailed(app));
        options.add(new Track(app, ui, new GpsStub()));

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
