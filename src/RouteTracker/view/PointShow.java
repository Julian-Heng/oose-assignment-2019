package RouteTracker.view;

public class PointShow extends GpsLocator
{
    private UserInterface ui;

    public PointShow(UserInterface ui)
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
