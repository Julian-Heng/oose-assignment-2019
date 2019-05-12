package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

/**
 * PrintAllRoute option that prints only the routes in the route map, excluding
 * points, segments and sub-routes
 * @author Julian Heng (19473701)
 **/
public class PrintAllRoute extends Option
{
    public PrintAllRoute(Map<String,Route> routes)
    {
        super("Print all routes", "", false, routes);
    }

    @Override
    public String doOption(String s) throws OptionException
    {
        Map<String,Route> routes = super.getRoutes();
        String out = "";

        if (routes.isEmpty())
        {
            throw new OptionException(
                "No routes recorded, select \"Get route data\" " +
                "to create routes"
            );
        }

        for (Route r : routes.values())
        {
            String[] info = new String[8];
            String fmt = "%-18s:";
            int max;

            info[0] = "";
            info[1] = r.getName() + ": " + r.getDescription();
            info[2] = "";
            info[3] = String.format(fmt, "Start").replace(' ', '.');
            info[4] = String.format(fmt, "End").replace(' ', '.');
            info[5] = String.format(fmt, "Distance").replace(' ', '.');
            info[6] = String.format(fmt, "Vertical_Climb").replace(' ', '.');
            info[7] = String.format(fmt, "Vertical_Descent").replace(' ', '.');

            // Whitespace gets converted to period, so use '_' as placeholder
            // for spaces
            info[6] = info[6].replace('_', ' ');
            info[7] = info[7].replace('_', ' ');

            info[3] += " " + r.getStartPoint();
            info[4] += " " + r.getEndPoint();
            info[5] += " " + String.format("%.2fm", r.getDistance());
            info[6] += " " + String.format("%.2fm", r.getPositiveAltitude());
            info[7] += " " + String.format("%.2fm", r.getNegativeAltitude());

            max = maxLength(info);
            info[0] = String.format("%" + max + "s", " ").replace(' ', '=');
            info[2] = info[0];

            out += String.join("\n", info) + "\n\n";
        }

        return out.replaceAll("%", "%%");
    }

    private int maxLength(String[] arr)
    {
        int max = 0;
        for (String s : arr)
        {
            max = s.length() > max ? s.length() : max;
        }
        return max;
    }
}
