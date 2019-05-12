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
        List<String> routeStr = new ArrayList<>();
        String out;

        super.getRoutes().forEach((k, v)->routeStr.add(v.toString()));
        out = String.join("\n\n", routeStr.stream().toArray(String[]::new));
        out = out.replaceAll("%", "%%") + "\n\n";
        return out;
    }
}
