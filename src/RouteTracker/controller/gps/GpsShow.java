package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.model.*;
import RouteTracker.view.*;

/**
 * GpsShow class that extends GpsLocator, displays the current coordinates
 * called
 * @author Julian Heng (19473701)
 **/
public class GpsShow extends GpsLocator
{
    private TrackData data;

    public GpsShow(TrackData data)
    {
        this.data = data;
    }

    @Override
    public void locationReceived(double latitude,
                                 double longitude,
                                 double altitude)
    {
        data.getUI().print(String.format("%.2f, %.2f, %.2f\n", latitude,
                                                               longitude,
                                                               altitude));
    }
}
