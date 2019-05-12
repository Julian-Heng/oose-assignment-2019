package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class PrintRouteDetailed extends Option
{
    public PrintRouteDetailed(Map<String,Route> routes)
    {
        super("Print detailed route", "", true, routes);
    }

    // Logic actually needed for printing prompt
    @Override
    public String getPrompt()
    {
        Map<String,Route> routes = super.getRoutes();
        return "\nRoutes:\n    " + String.join("\n    ", routes.keySet()) +
               "\n\nInput route name";
    }

    @Override
    public String doOption(String s) throws OptionException
    {
        Map<String,Route> routes = super.getRoutes();
        if (! routes.containsKey(s))
        {
            throw new OptionException("\"" + s + "\" does not exist");
        }

        // replaceAll method to make '%' a literal for String.format()
        return routes.get(s).toString().replaceAll("%", "%%") + "\n\n";
    }
}
