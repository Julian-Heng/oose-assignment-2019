package RouteTracker.view;

import java.util.*;

public class Menu
{
    private Map<Integer,Object> options;
    private int exit;
    private UserInterface ui;

    public Menu(UserInterface ui)
    {
        options = new TreeMap<>();
        exit = options.size() + 1;
        this.ui = ui;
    }

    public void addOption(Object o)
    {
        options.put(exit, o);
        exit++;
    }

    public String toString()
    {
        String out = "Select an option:\n";
        String fmt = "    (%d) %s\n";
        int count = 1;

        for (Object o : options.values())
        {
            out += String.format(fmt, count++, o.toString());
        }

        out += String.format(fmt, count, "Quit\n");
        return out;
    }
}
