package RouteTracker.controller.option;

import RouteTracker.controller.RouteTracker;
import RouteTracker.model.exception.OptionException;

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

    // This option does not output anything
    @Override
    public String doOption(String s) throws OptionException
    {
        super.getApp().getRoutes().clear();
        return "";
    }
}
