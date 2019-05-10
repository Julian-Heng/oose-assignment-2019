package RouteTracker.controller;

import java.util.*;
import RouteTracker.model.*;

public class PointFactory
{
    private RouteParser parser;
    private GeoUtils utils;

    public PointFactory(RouteParser parser, GeoUtils utils)
    {
        this.parser = parser;
        this.utils = utils;
    }

    public Point make(List<String> pointInfo) throws PointFactoryException
    {
        Point p;

        try
        {
            double latitude = Double.parseDouble(pointInfo.get(0));
            double longitude = Double.parseDouble(pointInfo.get(1));
            double altitude = Double.parseDouble(pointInfo.get(2));

            if (! doubleRange(latitude, -90.0, 90.0) ||
                ! doubleRange(longitude, -180.0, 180.0))
            {
                String errMsg = "Invalid coordinates, out of range: ";
                errMsg += pointInfo.get(0);
                errMsg += ", " + pointInfo.get(1);
                errMsg += ", " + pointInfo.get(2);
                throw new PointFactoryException(errMsg);
            }

            p = new Point(latitude, longitude, altitude);
        }
        catch (NumberFormatException e)
        {
            String errMsg = "Invalid coordinates, format error: ";
            errMsg += pointInfo.get(0);
            errMsg += ", " + pointInfo.get(1);
            errMsg += ", " + pointInfo.get(2);
            throw new PointFactoryException(errMsg);
        }

        return p;
    }

    private boolean doubleRange(double num, double low, double high)
    {
        return Double.compare(num, low) >= 0 &&
               Double.compare(high, num) >= 0;
    }
}
