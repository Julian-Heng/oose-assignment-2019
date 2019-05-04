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
    public String getDescription()
    {
        return description;
    }

    @Override
    public int getSize()
    {
        return 1;
    }
}
