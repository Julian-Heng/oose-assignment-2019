package RouteTracker.model;
import java.util.List;

/**
 * PointNode interface for storing anything related to a route
 * @author Julian Heng (19473701)
 **/
public interface PointNode
{
    String getName();
    String getDescription();

    double getLatitude();
    double getLongitude();
    double getAltitude();

    double getDistance();
    double getPositiveAltitude();
    double getNegativeAltitude();
    double getDeltaAltitude();

    PointNode getStartNode();
    PointNode getEndNode();
    Point getStartPoint();
    Point getEndPoint();
    List<Point> getAllPoints();
}
