package RouteTracker.view;
import java.util.*;

public class ConsoleUI implements UserInterface
{
    private Scanner in = new Scanner(System.in);

    @Override
    public void print(String fmt, Object... args)
    {
        System.out.printf(fmt, args);
    }

    @Override
    public void printError(String fmt, Object... args)
    {
        System.err.printf(fmt, args);
    }

    @Override
    public int readInteger(String fmt, Object... args)
    {
        String strInput = "";
        int userInput = -1;
        boolean inputValid = false;

        while (! inputValid)
        {
            try
            {
                strInput = readString(fmt, args);
                userInput = Integer.parseInt(strInput);
                inputValid = true;
            }
            catch (NumberFormatException e)
            {
                if (! strInput.isEmpty())
                {
                    printError("Enter a number\n");
                }
            }
        }

        return userInput;
    }

    @Override
    public int readIntegerRange(int low, int high,
                                String fmt, Object... args)
    {
        int userInput = -1;
        do
        {
            userInput = readInteger(fmt, args);
        } while (userInput < low || high < userInput);
        return userInput;
    }

    @Override
    public String readString(String fmt, Object... args)
    {
        print(fmt, args);
        return in.nextLine();
    }
}
