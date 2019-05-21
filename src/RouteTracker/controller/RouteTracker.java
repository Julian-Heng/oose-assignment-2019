package RouteTracker.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import RouteTracker.controller.option.Option;
import RouteTracker.model.Route;
import RouteTracker.model.exception.OptionException;
import RouteTracker.view.Menu;
import RouteTracker.view.UserInterface;

/**
 * RouteTracker class to hold the routes in the running app
 * @author Julian Heng (19473701)
 **/
public class RouteTracker
{
    private String inputFile;
    private Map<String,Route> routes;
    private List<Option> options;
    private Menu menu;
    private UserInterface ui;

    public RouteTracker(String[] args, Menu menu, UserInterface ui)
    {
        inputFile = args.length != 0 ? args[0] : "./dist/valid_1";

        routes = new HashMap<>();
        options = new ArrayList<>();

        this.menu = menu;
        this.ui = ui;

        this.menu.setUI(this.ui);
    }

    public Map<String,Route> getRoutes() { return routes; }
    public List<Option> getOptions() { return options; }
    public Menu getMenu() { return menu; }
    public UserInterface getUI() { return ui; }

    public void setRoutes(Map<String,Route> routes) { this.routes = routes; }
    public void setMenu(Menu menu) { this.menu = menu; }
    public void setUI(UserInterface ui) { this.ui = ui; }

    public void addOption(Option o)
    {
        options.add(o);
        menu.addOption(o);
    }

    public void run()
    {
        // Testing purpose code {{{
        try
        {
            options.get(0).doOption(inputFile);
            ui.print(options.get(2).doOption(""));
        }
        catch (OptionException e)
        {
            ui.printError(e.getMessage() + "\n\n");
        }
        // End testing purpose code }}}

        do
        {
            ui.print(menu.toString());
        } while (menu.executeOption(ui.readInteger("> ")));
    }
}
