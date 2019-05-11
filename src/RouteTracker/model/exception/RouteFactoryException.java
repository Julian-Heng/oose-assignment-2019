package RouteTracker.model.exception;

public class RouteFactoryException extends Exception
{
    private static String msg;
    private static final long serialVersionUID = 42L;

    public RouteFactoryException(String errMsg)
    {
        msg = errMsg;
    }

    @Override public String getMessage() { return msg; }
}
