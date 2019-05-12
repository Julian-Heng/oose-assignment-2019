package RouteTracker.model.exception;

/**
 * OptionException class thrown by Option objects
 * @author Julian Heng (19473701)
 **/
public class OptionException extends Exception
{
    private static String msg;
    private static final long serialVersionUID = 42L;

    public OptionException(String errMsg)
    {
        msg = errMsg;
    }

    @Override public String getMessage() { return msg; }
}
