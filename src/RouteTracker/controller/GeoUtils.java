package RouteTracker.controller;

import java.lang.Math;
import java.io.*;

/**
 * GeoUtils class acting as a stub
 * @author Julian Heng (19473701)
 **/
public class GeoUtils
{
    private String url;
    private static final int RADIUS = 6371000;

    // Testing purposes
    public void setUrl(String s) { url = s; }

    /**
     * Calculates the horizontal distances between two coordinates
     *
     * @param lat1  the latitude of the first point
     * @param long1 the longitude of the first point
     * @param lat2  the latitude of the second point
     * @param long2 the longitude of the second point
     *
     * @return      the horizontal distance
     **/
    public double calcMetresDistance(double lat1, double long1,
                                     double lat2, double long2)
    {
        double result;

        if (Double.compare(lat1, lat2) == 0 &&
            Double.compare(long1, long2) == 0)
        {
            result = 0.0;
        }
        else
        {
            // Haversine formula
            // Source: https://www.movable-type.co.uk/scripts/latlong.html
            double lat1r = Math.toRadians(lat1);
            double lat2r = Math.toRadians(lat2);
            double deltaLat = Math.toRadians(lat2 - lat1);
            double deltaLong = Math.toRadians(long2 - long1);

            double a = Math.pow(Math.sin(deltaLat / 2), 2) +
                       Math.cos(lat1r) * Math.cos(lat2r) *
                       Math.pow(Math.sin(deltaLong / 2), 2);
            result = (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))) * RADIUS;
        }

        return result;
    }

    /**
     * Gets the route data from the source url
     * @return A string representation of the data
     * @throws IOException If there is an error reading the url
     **/
    public String retrieveRouteData() throws IOException
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
