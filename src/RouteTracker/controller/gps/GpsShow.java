package RouteTracker.controller.gps;

import RouteTracker.model.TrackData;

/**
 * GpsShow class that extends GpsLocator, displays the current coordinates
 * called
 * @author Julian Heng (19473701)
 **/
public class GpsShow extends GpsLocator
{
    protected TrackData data;

    protected GpsShow(TrackData data)
    {
        this.data = data;
    }

    @Override
    protected void locationReceived(double latitude,
                                    double longitude,
                                    double altitude)
    {
        data.getUI().print(String.format("%.2f, %.2f, %.2f\n", latitude,
                                                               longitude,
                                                               altitude));
    }
}
