package RouteTracker.controller;

import java.io.*;
import java.util.*;

import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class PrintRoute implements Option
{
    // Required Option classfields
    private int label;
    private String name;
    private boolean requireInput;

    // Auxilary classfields
    private Map<String,Route> routes;

    public PrintRoute(int label, Map<String,Route> routes)
    {
        this.label = label;
        name = "Print route";
        requireInput = false;

        this.routes = routes;
    }

    @Override public int getLabel() { return label; }
    @Override public String getMenuString() { return name; }
    @Override public String getPrompt() { return ""; }
    @Override public boolean getRequireInput() { return requireInput; }

    @Override
    public void doOption(String s) throws OptionException
    {
        if (routes.isEmpty())
        {
            throw new OptionException("No routes created");
        }
        else
        {
            int[] count = {1};
            routes.forEach((k, v)->{
                System.out.println("Route #" + count[0]++ + ":");
                System.out.println(v + "\n");
            });
        }
    }
}
