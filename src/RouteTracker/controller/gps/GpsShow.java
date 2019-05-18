package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.view.*;

/**
 * GpsShow class that extends GpsLocator, displays the current coordinates
 * called
 * @author Julian Heng (19473701)
 **/
public class GpsShow extends GpsLocator
{
    // For the GpsShow class, we are simply printing out the coordinates, thus
    // we don't need the context of the points and the current location of the
    // user
    private UserInterface ui;

    public GpsShow(UserInterface ui)
    {
        this.ui = ui;
    }

    @Override
    public void locationReceived(double latitude,
                                 double longitude,
                                 double altitude)
    {
        ui.print(String.format("%.2f, %.2f, %.2f\n", latitude,
                                                     longitude,
                                                     altitude));
    }
}
