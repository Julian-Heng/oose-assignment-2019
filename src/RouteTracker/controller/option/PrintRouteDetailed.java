package RouteTracker.controller.option;

import java.util.Map;

import RouteTracker.controller.RouteTracker;
import RouteTracker.model.Route;
import RouteTracker.model.exception.OptionException;

/**
 * PrintRouteDetailed option for printing a user seleceted route including
 * points, segemnts and sub-routes
 * @author Julian Heng (19473701)
 **/
public class PrintRouteDetailed extends Option
{
    public PrintRouteDetailed(RouteTracker app)
    {
        super("Print detailed route", "", true, app);
    }

    // Logic actually needed for printing prompt, thus overring super's
    // getPrompt()
    @Override
    public String getPrompt()
    {
        Map<String,Route> routes = super.getApp().getRoutes();
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
        Map<String,Route> routes = super.getApp().getRoutes();
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
