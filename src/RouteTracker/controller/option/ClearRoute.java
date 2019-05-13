package RouteTracker.controller.option;

import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

/**
 * ClearRoute option that clears the route map
 * @author Julian Heng (19473701)
 **/
public class ClearRoute extends Option
{
    public ClearRoute(RouteTracker app)
    {
        super("Clear routes", "", false, app);
    }

    // This option does not ouput anything
    @Override
    public String doOption(String s) throws OptionException
    {
        super.getApp().getRoutes().clear();
        return "";
    }
}
