package RouteTracker.controller;

import java.util.*;

import RouteTracker.model.*;
import RouteTracker.controller.*;

public class PointFactory
{
    private RouteParser parser;
    private GeoUtils util;
    private List<String> contents;

    public PointFactory(RouteParser parser,
                        GeoUtils util,
                        List<String> contents)
    {
        this.parser = parser;
        this.util = util;
        this.contents = contents;
    }

    public Point makePoint(String line) throws RouteParserException
    {
        String[] info;
        Point newPoint = null;
        double latitude, longitude, altitude;

        info = parser.parsePoint(line);

        try
        {
            latitude = Double.parseDouble(info[0]);
            longitude = Double.parseDouble(info[1]);
            altitude = Double.parseDouble(info[2]);

            if (! parser.doubleRange(latitude, -90.0, 90.0) ||
                ! parser.doubleRange(longitude, -180.0, 180.0))
            {
                String errMsg = "Invalid coordinates, out of range: " +
                                    info[0] + ", " +
                                    info[1] + ", " + 
                                    info[2];

                throw new RouteParserException(errMsg);
            }

            newPoint = new Point(latitude, longitude, altitude);
            if (! parser.isEndPoint(line))
            {
                newPoint.setDescription(info[3]);
            }
        }
        catch (NumberFormatException e)
        {
            String errMsg = "Invalid coordinates, format error: " +
                                info[0] + ", " +
                                info[1] + ", " +
                                info[2];

            throw new RouteParserException(errMsg);
        }
        catch (Exception e)
        {
            throw new RouteParserException("Unknown error: " + e.getMessage());
        }

        return newPoint;
    }
}
