package RouteTracker.model;

public interface PointNode
{
    String getName();
    String getDescription();
    PointNode getStartNode();
    PointNode getEndNode();
    Point getStartPoint();
    Point getEndPoint();
}
