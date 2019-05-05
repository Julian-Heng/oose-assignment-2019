package RouteTracker.controller;
import java.lang.Math;

public class GeoUtils
{
    private static final int RADIUS = 6371000;

    public double calcDistance(double lat1, double long1,
                               double lat2, double long2)
    {
        double lat1r = Math.toRadians(lat1);
        double long1r = Math.toRadians(long1);
        double lat2r = Math.toRadians(lat2);
        double long2r = Math.toRadians(long2);
        double longAbs = Math.toRadians(Math.abs(long1 - long2));

        return Math.acos((Math.sin(lat1r) * Math.sin(lat2r)) +
                         (Math.cos(lat1r) * Math.cos(lat2r) *
                          Math.cos(longAbs))) * RADIUS;
    }
}
