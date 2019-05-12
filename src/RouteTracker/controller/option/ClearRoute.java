package RouteTracker.controller.option;

import java.util.*;

import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class ClearRoute extends Option
{
    public ClearRoute(Map<String,Route> routes)
    {
        super("Clear routes", "", false, routes);
    }

    // This option does not ouput anything
    @Override
    public String doOption(String s) throws OptionException
    {
        super.getRoutes().clear();
        return "";
    }
}
