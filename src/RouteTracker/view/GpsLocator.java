package RouteTracker.view;

// This is probably an observer class to be implemented
public abstract class GpsLocator
{
    /**
     * When GpsLocator has received a new set of coordinates, it calls
     * this hook method.
     **/
    protected abstract void locationReceived(double latitude,
                                             double longitude,
                                             double altitude);
}
