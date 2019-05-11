package RouteTracker.controller;

import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public interface Option
{
    int getLabel();
    String getMenuString();
    String getPrompt();
    boolean getRequireInput();
    public String doOption(String s) throws OptionException;
}
