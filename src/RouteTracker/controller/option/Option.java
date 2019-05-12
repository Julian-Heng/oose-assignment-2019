package RouteTracker.controller.option;

import java.util.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public abstract class Option
{
    private String name;
    private String prompt;
    private boolean requireInput;
    private Map<String,Route> routes;

    public Option(String name,
                  String prompt,
                  boolean requireInput,
                  Map<String,Route> routes)
    {
        this.name = name;
        this.prompt = prompt;
        this.requireInput = requireInput;
        this.routes = routes;
    }

    public String getMenuString() { return name; }
    public String getPrompt() { return prompt; }
    public boolean getRequireInput() { return requireInput; }
    public Map<String,Route> getRoutes() { return routes; }

    public abstract String doOption(String s) throws OptionException;
}
