package RouteTracker.model;

import java.util.*;
import RouteTracker.model.*;

public class Point implements PointNode
{
    private Map<String,Double> coords;

    public Point(double latitude, double longitude, double altitude)
    {
        coords = new HashMap<>();
        coords.put("Lat", new Double(latitude));
        coords.put("Long", new Double(longitude));
        coords.put("Alt", new Double(altitude));
    }
}
