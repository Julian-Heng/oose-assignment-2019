package RouteTracker.model;

public interface RouteNode
{
    Point getStart();
    Point getEnd();
    String getDescription();
    int getSize();
}
