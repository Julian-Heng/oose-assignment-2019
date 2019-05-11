package RouteTracker.controller;

import java.io.*;
import java.util.*;

import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class GetRouteData implements Option
{
    // Required Option classfields
    private int label;
    private String name;
    private boolean requireInput;

    // Auxilary classfields
    private GeoUtils utils;
    private Map<String,Route> routes;

    public GetRouteData(int label, GeoUtils utils, Map<String,Route> routes)
    {
        this.label = label;
        name = "Get route data";
        requireInput = true;

        this.utils = utils;
        this.routes = routes;
    }

    @Override public int getLabel() { return label; }
    @Override public String getMenuString() { return name; }
    @Override public String getPrompt() { return "Enter url"; }
    @Override public boolean getRequireInput() { return requireInput; }

    @Override
    public String doOption(String s) throws OptionException
    {
        RouteParser parser = new RouteParser(utils);
        PointFactory pointMaker;
        RouteFactory routeMaker;

        // Testing purposes
        utils.setUrl(s);

        try
        {
            parser.readData();
            parser.makeIndex();

            pointMaker = new PointFactory(parser, utils);
            routeMaker = new RouteFactory(pointMaker, parser, utils);

            routeMaker.make(routes);
        }
        catch (RouteParserException | RouteFactoryException e)
        {
            throw new OptionException(e.getMessage());
        }

        // This option does not ouput anything
        return "";
    }
}
