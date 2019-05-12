package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class GetRouteData extends Option
{
    private GeoUtils utils;

    public GetRouteData(GeoUtils utils, Map<String,Route> routes)
    {
        super("Get route data", "Enter url", true, routes);
        this.utils = utils;
    }

    // This option does not ouput anything
    @Override
    public String doOption(String s) throws OptionException
    {
        Map<String,Route> routes = super.getRoutes();
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

        return "";
    }
}
