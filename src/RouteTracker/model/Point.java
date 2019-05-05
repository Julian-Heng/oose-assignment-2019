package RouteTracker.model;

public class Point implements RouteNode
{
    private double latitude;
    private double longitude;
    private double altitude;
    private String description;

    public Point(double latitude, double longitude, double altitude)
    {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;

        // Description is optional, thus will be set using the
        // setDescription() method
        this.description = "";
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public double getAltitude()
    {
        return altitude;
    }

    // Implementing methods in RouteNode

    @Override
    public Point getStart()
    {
        return this;
    }

    @Override
    public Point getEnd()
    {
        return this;
    }

    @Override
    public String getName()
    {
        return description;
    }

    @Override
    public String getDescription()
    {
        return description;
    }

    @Override
    public int getSize()
    {
        return 1;
    }

    public String toString()
    {
        return String.format("%.2f, %.2f, %.2f: %s", latitude,
                                                     longitude,
                                                     altitude,
                                                     description);
    }
}
