package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class PrintRoute extends Option
{
    public PrintRoute(Map<String,Route> routes)
    {
        super("Print route", "", false, routes);
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
            int max = 0;
            String[] info = new String[8];
            info[0] = "";
            info[1] = r.getName() + ": " + r.getDescription();
            info[2] = "";
            info[3] = "Start.............: " + r.getStartPoint();
            info[4] = "End...............: " + r.getEndPoint();
            info[5] = "Distance..........: ";
            info[6] = "Vertical Climb....: ";
            info[7] = "Vertical Descent..: ";

            info[5] += String.format("%.2fm", r.getDistance());
            info[6] += String.format("%.2fm", r.getPositiveAltitude());
            info[7] += String.format("%.2fm", r.getNegativeAltitude());

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
