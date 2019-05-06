package RouteTracker.controller;

import java.lang.Math;
import java.io.*;

import RouteTracker.model.*;

public class GeoUtils
{
    private static final int RADIUS = 6371000;
    private static final double TOLERANCE = 0.000001;

    public double calcMetresDistance(double lat1, double long1,
                                     double lat2, double long2)
    {
        /*
        double lat1r = Math.toRadians(lat1);
        double long1r = Math.toRadians(long1);
        double lat2r = Math.toRadians(lat2);
        double long2r = Math.toRadians(long2);
        double longAbs = Math.toRadians(Math.abs(long1 - long2));

        return Math.acos((Math.sin(lat1r) * Math.sin(lat2r)) +
                         (Math.cos(lat1r) * Math.cos(lat2r) *
                          Math.cos(longAbs))) * RADIUS;
                          */
        return 5.0;
    }

    public String retrieveRouteData(String url) throws IOException
    {
        String line;
        String data = "";
        BufferedReader read = null;

        try
        {
            read = new BufferedReader(new FileReader(url));

            while ((line = read.readLine()) != null)
            {
                data += line + "\n";
            }

            data = data.trim();
        }
        catch (IOException e)
        {
            throw new IOException("Error reading data: " + e.getMessage());
        }
        finally
        {
            try
            {
                if (read != null)
                {
                    read.close();
                }
            }
            catch (IOException ex)
            {
            }
        }

        return data;
    }
}
