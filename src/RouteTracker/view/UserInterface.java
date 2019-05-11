package RouteTracker.view;

public interface UserInterface
{
    void print(String fmt, Object... args);
    void printError(String fmt, Object... args);
    int readInteger(String fmt, Object... args);
    int readIntegerRange(int low, int high, String fmt, Object... args);
    String readString(String fmt, Object... args);
}
