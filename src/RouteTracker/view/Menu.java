package RouteTracker.view;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.controller.option.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

/**
 * Menu class for printing Option classes and inputting and ouputting user
 * actions
 * @author Julian Heng (19473701)
 **/
public class Menu
{
    //private Map<Integer,Option> options;
    private List<Option> options;
    private int exit;
    private UserInterface ui;

    public Menu(UserInterface ui)
    {
        options = new ArrayList<>();
        exit = 1;
        this.ui = ui;
    }

    public void addOption(Option o)
    {
        options.add(o);
        exit++;
    }

    /**
     * Runs the doOption() method in the Option class within the options list
     * @param opt The integer of which the Option class is stored
     * @return Whether or not the exit option is selected
     **/
    public boolean executeOption(int opt)
    {
        Option o;
        String input = "";

        // Return false immediately because user selects exit
        if (opt == exit)
        {
            return false;
        }

        // Decrement because of array index
        opt--;

        if ((o = options.get(opt)) != null)
        {
            long start, end;

            if (o.getRequireInput())
            {
                input = ui.readString("%s: ", o.getPrompt());
            }

            // Time execution
            start = System.nanoTime();
            try
            {
                ui.print(o.doOption(input));
            }
            catch (NullPointerException | OptionException e)
            {
                ui.printError("%s\n\n", e.getMessage());
            }
            end = System.nanoTime();

            ui.print("Took %1.3fms\n", (double)(end - start) / 1000000);
        }
        else
        {
            ui.printError("Option not found\n");
        }

        return true;
    }

    public String toString()
    {
        String out = "Select an option:\n";
        String fmt = "    (%d) %s\n";
        int count = 1;

        for (Option o : options)
        {
            out += String.format(fmt, count++, o.getMenuString());
        }

        out += String.format(fmt, count, "Quit\n");
        return out;
    }
}
