package RouteTracker.model.exception;

/**
 * RouteParserException class thrown by RouteParser class
 * @author Julian Heng (19473701)
 **/
public class RouteParserException extends Exception
{
    private static String msg;
    private static final long serialVersionUID = 42L;

    public RouteParserException(String errMsg)
    {
        msg = errMsg;
    }

    @Override public String getMessage() { return msg; }
}
