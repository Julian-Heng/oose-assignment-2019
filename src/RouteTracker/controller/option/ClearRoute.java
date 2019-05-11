package RouteTracker.controller.option;

import java.util.*;

import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class ClearRoute implements Option
{
    // Required Option classfields
    private int label;
    private String name;
    private boolean requireInput;

    // Auxilary classfields
    private Map<String,Route> routes;

    public ClearRoute(int label, Map<String,Route> routes)
    {
        this.label = label;
        name = "Clear routes";
        requireInput = false;

        this.routes = routes;
    }

    @Override public int getLabel() { return label; }
    @Override public String getMenuString() { return name; }
    @Override public String getPrompt() { return ""; }
    @Override public boolean getRequireInput() { return requireInput; }

    @Override
    public String doOption(String s) throws OptionException
    {
        routes.clear();

        // This option does not ouput anything
        return "";
    }
}
