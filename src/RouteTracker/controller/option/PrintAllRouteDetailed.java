package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

/**
 * PrintAllRouteDetailed option which prints all the routes including points,
 * segments and sub-routes
 * @author Julian Heng (19473701)
 **/
public class PrintAllRouteDetailed extends Option
{
    public PrintAllRouteDetailed(RouteTracker app)
    {
        super("Print all routes detailed", "", false, app);
    }

    @Override
    public String doOption(String s) throws OptionException
    {
        List<String> strLst = new ArrayList<>();
        Map<String,Route> routes = super.getApp().getRoutes();
        String out = "No routes recorded";

        if (! routes.isEmpty())
        {
            routes.forEach((k, v)->strLst.add(v.toString()));
            out = String.join("\n\n", strLst.stream().toArray(String[]::new));
            out = out.replaceAll("%", "%%") + "\n\n";
        }

        return out;
    }
}
