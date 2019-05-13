package RouteTracker.controller.factory;

import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

/**
 * PointFactory class that creates Point objects
 * @author Julian Heng (19473701)
 **/
public class PointFactory
{
    private RouteParser parser;
    private GeoUtils utils;

    public PointFactory(RouteParser parser, GeoUtils utils)
    {
        this.parser = parser;
        this.utils = utils;
    }

    /**
     * Makes a Point object
     * @param pointInfo A list containing a point declaration
     * @return A new Point object
     * @throws PointFactoryException Thrown when there's a problem with
     *                               the point declaration, such as
     *                               invalid coordinates
     **/
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

    /**
     * Check if a double is within range of two doubles
     * @param num The number to check
     * @param low The lower boundary
     * @param high The upper boundary
     * @return True or False
     **/
    private boolean doubleRange(double num, double low, double high)
    {
        return Double.compare(num, low) >= 0 &&
               Double.compare(high, num) >= 0;
    }
}
