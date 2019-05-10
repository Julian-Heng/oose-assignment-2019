package RouteTracker.model;

public interface PointNode
{
    String getName();
    String getDescription();

    double getLatitude();
    double getLongitude();
    double getAltitude();

    PointNode getStartNode();
    PointNode getEndNode();
    Point getStartPoint();
    Point getEndPoint();
}
