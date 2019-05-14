package RouteTracker.view;

public class DistanceShow extends GpsLocator
{
    private UserInterface ui;

    public DistanceShow(UserInterface ui)
    {
        this.ui = ui;
    }

    @Override
    protected void locationReceived(double latitude,
                                    double longitude,
                                    double altitude)
    {
    }
}
