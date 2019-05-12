package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class PrintAllRouteDetailed extends Option
{
    public PrintAllRouteDetailed(Map<String,Route> routes)
    {
        super("Print all routes detailed", "", false, routes);
    }

    @Override
    public String doOption(String s) throws OptionException
    {
        List<String> strLst = new ArrayList<>();
        Map<String,Route> routes = super.getRoutes();
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
