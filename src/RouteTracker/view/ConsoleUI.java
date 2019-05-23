package RouteTracker.view;

import java.util.Scanner;

/**
 * ConsoleUI class that implements a UserInterface for outputting and
 * inputter from the command line
 * @author Julian Heng (19473701)
 **/
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

    /**
     * Reads an integer from the command line
     * @param fmt   The string format akin to calling String.format()
     * @param args  varargs of Strings
     * @return User inputted integer
     **/
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
                // If user simply press enter, do nothing else print error
                // message
                if (! strInput.isEmpty())
                {
                    printError("Enter a number\n");
                }
            }
        }

        return userInput;
    }

    /**
     * Reads a string from the command line
     * @param fmt   The string format akin to calling String.format()
     * @param args  varargs of Strings
     * @return User inputted integer
     *
     * Bug: Crashes the program when user presses ctrl-d
     *      (NoSuchElementException) and due to Menu implementation, program
     *      loops when trying to handle the exception
     **/
    @Override
    public String readString(String fmt, Object... args)
    {
        print(fmt, args);
        return in.nextLine();
    }
}
