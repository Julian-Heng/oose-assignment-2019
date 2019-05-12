package RouteTracker.model.exception;

/**
 * RouteFactoryException class thrown by the RouteFactory class
 * @author Julian Heng (19473701)
 **/
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
