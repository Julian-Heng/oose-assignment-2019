package RouteTracker.controller.option;

import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public interface Option
{
    String getMenuString();
    String getPrompt();
    boolean getRequireInput();
    public String doOption(String s) throws OptionException;
}
