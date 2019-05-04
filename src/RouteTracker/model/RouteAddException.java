package RouteTracker.model;

public class RouteAddException extends Exception
{
    private static String msg;
    private static final long serialVersionUID = 42L;

    public RouteAddException(String msg)
    {
        msg = msg;
    }

    @Override
    public String getMessage()
    {
        return msg;
    }
}
