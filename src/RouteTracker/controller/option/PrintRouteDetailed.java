package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

/**
 * PrintRouteDetailed option for printing a user seleceted route including
 * points, segemnts and sub-routes
 * @author Julian Heng (19473701)
 **/
public class PrintRouteDetailed extends Option
{
    public PrintRouteDetailed(Map<String,Route> routes)
    {
        super("Print detailed route", "", true, routes);
    }

    // Logic actually needed for printing prompt, thus overring super's
    // getPrompt()
    @Override
    public String getPrompt()
    {
        Map<String,Route> routes = super.getRoutes();
        String prompt;

        if (routes.isEmpty())
        {
            prompt = "No routes created, press enter to go back to menu";
        }
        else
        {
            prompt = "\nRoutes:\n    " +
                     String.join("\n    ", routes.keySet()) +
                     "\n\nInput route name";
        }

        return prompt;
    }

    @Override
    public String doOption(String s) throws OptionException
    {
        Map<String,Route> routes = super.getRoutes();
        String out = "";

        if (! routes.isEmpty())
        {
            if (! routes.containsKey(s))
            {
                throw new OptionException("\"" + s + "\" does not exist");
            }

            // replaceAll method to make '%' a literal for String.format()
            out = routes.get(s).toString().replaceAll("%", "%%") + "\n\n";
        }

        return out;
    }
}
