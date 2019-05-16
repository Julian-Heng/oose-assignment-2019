package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;
import RouteTracker.view.*;

public class GpsShow extends GpsLocator
{
    protected UserInterface ui;

    public GpsShow(UserInterface ui)
    {
        this.ui = ui;
    }

    @Override
    protected void locationReceived(double latitude,
                                    double longitude,
                                    double altitude)
    {
        ui.print(latitude + ", " + longitude + ", " + altitude + "\n");
    }
}
