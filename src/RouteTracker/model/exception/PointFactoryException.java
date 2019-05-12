package RouteTracker.model.exception;

/**
 * PointFactoryException class thrown by PointFactory class
 * @author Julian Heng (19473701)
 **/
public class PointFactoryException extends Exception
{
    private static String msg;
    private static final long serialVersionUID = 42L;

    public PointFactoryException(String errMsg)
    {
        msg = errMsg;
    }

    @Override public String getMessage() { return msg; }
}
