package RouteTracker.view;

public class GpsShow extends GpsLocator
{
    private UserInterface ui;

    public GpsShow(UserInterface ui)
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
