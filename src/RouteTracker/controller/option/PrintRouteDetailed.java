package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class PrintRouteDetailed implements Option
{
    // Required Option classfields
    private String name;
    private boolean requireInput;

    // Auxilary classfields
    private Map<String,Route> routes;

    public PrintRouteDetailed(Map<String,Route> routes)
    {
        name = "Print detailed route";
        requireInput = true;

        this.routes = routes;
    }

    @Override public String getMenuString() { return name; }

    @Override public String getPrompt()
    {
        return "\nRoutes:\n    " + String.join("\n    ", routes.keySet()) +
               "\n\nInput route name";
    }

    @Override public boolean getRequireInput() { return requireInput; }

    @Override
    public String doOption(String s) throws OptionException
    {
        if (! routes.containsKey(s))
        {
            throw new OptionException("\"" + s + "\" does not exist");
        }

        // replaceAll method to make '%' a literal for String.format()
        return routes.get(s).toString().replaceAll("%", "%%") + "\n\n";
    }
}
