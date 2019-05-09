package RouteTracker.controller;

import java.lang.Math;
import java.io.*;

import RouteTracker.model.*;

public class GeoUtils
{
    private static final int RADIUS = 6371000;

    public double calcMetresDistance(double lat1, double long1,
                                     double lat2, double long2)
    {
        double lat1r = Math.toRadians(lat1);
        double lat2r = Math.toRadians(lat2);
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(long2 - long1);

        double a = Math.pow(Math.sin(dLat / 2), 2) +
                   Math.cos(lat1r) * Math.cos(lat2r) *
                   Math.pow(Math.sin(dLong / 2), 2);
        return RADIUS * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
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
