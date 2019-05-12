package RouteTracker.view;

/**
 * UserInterface interface for classes to implement, whether it's a GUI or CLI
 * @author Julian Heng (19473701)
 **/
public interface UserInterface
{
    void print(String fmt, Object... args);
    void printError(String fmt, Object... args);
    int readInteger(String fmt, Object... args);
    String readString(String fmt, Object... args);
}
