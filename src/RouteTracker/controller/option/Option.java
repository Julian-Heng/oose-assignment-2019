package RouteTracker.controller.option;

import RouteTracker.controller.RouteTracker;
import RouteTracker.model.exception.OptionException;

/**
 * Option abstract class to perform actions on routes
 * @author Julian Heng (19473701)
 **/
public abstract class Option
{
    private String name;
    private String prompt;
    private boolean requireInput;
    private RouteTracker app;

    public Option(String name,
                  String prompt,
                  boolean requireInput,
                  RouteTracker app)
    {
        this.name = name;
        this.prompt = prompt;
        this.requireInput = requireInput;
        this.app = app;
    }

    public String getMenuString() { return name; }
    public String getPrompt() { return prompt; }
    public boolean getRequireInput() { return requireInput; }
    public RouteTracker getApp() { return app; }

    // To be implemented by other classes
    public abstract String doOption(String s) throws OptionException;
}
