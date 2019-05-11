package RouteTracker.view;

import java.util.*;
import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class Menu
{
    private Map<Integer,Option> options;
    private int exit;
    private UserInterface ui;

    public Menu(UserInterface ui)
    {
        options = new TreeMap<>();
        exit = options.size() + 1;
        this.ui = ui;
    }

    public void addOption(Option o)
    {
        options.put(o.getLabel(), o);
        exit++;
    }

    public boolean executeOption(int opt)
    {
        Option o;
        String input = "";

        if (opt == exit)
        {
            return false;
        }

        if ((o = options.get(opt)) != null)
        {
            if (o.getRequireInput())
            {
                input = ui.readString("%s: ", o.getPrompt());
            }

            try
            {
                o.doOption(input);
            }
            catch (NullPointerException | OptionException e)
            {
                ui.printError("%s\n\n", e.getMessage());
            }
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

        for (Option o : options.values())
        {
            out += String.format(fmt, count++, o.getMenuString());
        }

        out += String.format(fmt, count, "Quit\n");
        return out;
    }
}
