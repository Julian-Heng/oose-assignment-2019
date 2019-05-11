package RouteTracker.model.exception;

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
