package RouteTracker.controller.gps;

public abstract class GpsLocator
{
    /**
    * When GpsLocator has received a new set of coordinates, it calls
    * this hook method.
    */
    protected abstract void locationReceived(double latitude,
                                             double longitude,
                                             double altitude);
}
