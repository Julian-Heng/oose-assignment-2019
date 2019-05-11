package RouteTracker.model.exception;

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
