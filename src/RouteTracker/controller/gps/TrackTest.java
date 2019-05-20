package RouteTracker.controller.gps;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.controller.gps.*;
import RouteTracker.model.*;
import RouteTracker.view.*;

/**
 * TrackTest class to test all the trackers in the gps folder
 * @author Julian Heng (19473701)
 **/
public class TrackTest
{
    private TrackData data;

    public TrackTest(TrackData data)
    {
        this.data = data;
    }

    public void run()
    {
        GeoUtils utils = data.getUtils();
        UserInterface ui = data.getUI();
        Route route = data.getRoute();
        List<Point> points = route.getAllPoints();
        List<GpsLocator> trackers = new ArrayList<>();

        boolean first = true;
        double prevLat, prevLong, prevAlt;
        double nextLat, nextLong, nextAlt;
        double deltaLat, deltaLong, deltaAlt;

        prevLat = prevLong = prevAlt = -Double.MAX_VALUE;
        nextLat = nextLong = nextAlt = -Double.MAX_VALUE;

        trackers.add(new DistanceShow(data));
        trackers.add(new GpsShow(data));
        trackers.add(new WaypointShow(data));
        // Will not be adding manually selecting which waypoint user has been
        // as that is invoked when the user manually selects it whereas this
        // is completely automated

        for (Point p : points)
        {
            if (first)
            {
                prevLat = p.getLatitude();
                prevLong = p.getLongitude();
                prevAlt = p.getAltitude();
                first = false;
                continue;
            }

            nextLat = p.getLatitude();
            nextLong = p.getLongitude();
            nextAlt = p.getAltitude();

            deltaLat = (nextLat - prevLat) / 5;
            deltaLong = (nextLong - prevLong) / 5;
            deltaAlt = (nextAlt - prevAlt) / 5;

            for (int i = 0; i < 6; i++)
            {
                // Begin calling the trackers using the provided
                // coordinates
                for (GpsLocator tracker : trackers)
                {
                    ui.print("========================================\n");
                    ui.print((Object)tracker.getClass().getName() + "\n");
                    ui.print("========================================\n");
                    tracker.locationReceived(prevLat,
                                             prevLong,
                                             prevAlt);
                    ui.print("\n");
                }

                prevLat += deltaLat;
                prevLong += deltaLong;
                prevAlt += deltaAlt;
            }

            prevLat = nextLat;
            prevLong = nextLong;
            prevAlt = nextAlt;

            ui.print("\n\n");
        }
    }
}
