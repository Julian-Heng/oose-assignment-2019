package RouteTracker.model;

public class RouteParserException extends Exception
{
    private static String msg;
    private static final long serialVersionUID = 42L;

    public RouteParserException(String errMsg)
    {
        msg = errMsg;
    }

    @Override
    public String getMessage()
    {
        return msg;
    }
}
