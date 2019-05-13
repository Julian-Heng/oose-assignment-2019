package RouteTracker.controller.option;

import java.io.*;
import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.controller.factory.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

/**
 * GetRouteData option that fetches the data using GeoUtils and set up
 * the route map for the program
 * @author Julian Heng (19473701)
 **/
public class GetRouteData extends Option
{
    // This option requires GeoUtils
    private GeoUtils utils;

    public GetRouteData(GeoUtils utils, RouteTracker app)
    {
        super("Get route data", "Enter url", true, app);
        this.utils = utils;
    }

    // This option does not ouput anything
    @Override
    public String doOption(String s) throws OptionException
    {
        RouteParser parser = new RouteParser(utils);
        PointFactory pointMaker;
        RouteFactory routeMaker;

        // Testing purposes
        utils.setUrl(s);

        // Construct routes
        try
        {
            parser.readData();
            parser.makeIndex();

            pointMaker = new PointFactory(parser, utils);
            routeMaker = new RouteFactory(pointMaker, parser, utils);

            super.getApp().setRoutes(routeMaker.make());
        }
        catch (RouteParserException | RouteFactoryException e)
        {
            throw new OptionException(e.getMessage());
        }

        return "";
    }
}
