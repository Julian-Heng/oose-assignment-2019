package RouteTracker.controller.option;

import java.util.Map;

import RouteTracker.controller.RouteTracker;
import RouteTracker.model.Route;
import RouteTracker.model.exception.OptionException;

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
        Map<String,Route> routes = super.getApp().getRoutes();
        StringBuilder out = new StringBuilder();
        out.append("No routes recorded, select \"Get route data\" " +
                   "to create routes\n\n");

        if (! routes.isEmpty())
        {
            out.setLength(0);
            routes.forEach((k, v)->out.append(v.toString() + "\n\n"));
        }

        return out.toString().replaceAll("%", "%%");
    }
}
