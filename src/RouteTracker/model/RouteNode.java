package RouteTracker.model;

public interface RouteNode
{
    Point getStart();
    Point getEnd();
    String getName();
    String getDescription();
    int getSize();
}
