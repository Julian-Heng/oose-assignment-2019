package RouteTracker.model;

/**
 * PointNode interface for storing anything related to a route
 * @author Julian Heng (19473701)
 **/
public interface PointNode
{
    public String getName();
    public String getDescription();

    public double getLatitude();
    public double getLongitude();
    public double getAltitude();
    public double getDistance();
    public double getDeltaAltitude();

    public PointNode getStartNode();
    public PointNode getEndNode();
    public Point getStartPoint();
    public Point getEndPoint();
}
