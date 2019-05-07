import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
// import RouteTracker.view.*;

public class RouteTracker
{
    public static void main(String[] args)
    {
        GeoUtils util;
        RouteParser parser;

        RouteFactory routeMaker;
        PointFactory pointMaker;

        Map<String,Route> routes;
        List<String> contents;

        if (args.length > 0)
        {
            util = new GeoUtils();
            parser = new RouteParser(util);

            try
            {
                parser.readData(args[0]);
                pointMaker = new PointFactory(parser,
                                              util,
                                              parser.getContents());

                routeMaker = new RouteFactory(parser,
                                              parser.getContents(),
                                              pointMaker);

                routes = routeMaker.makeRoutes();

                for (Map.Entry<String,Route> r: routes.entrySet())
                {
                    System.out.println(r.getValue().toString() + "\n");
                }
            }
            catch (RouteParserException | RouteFactoryException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
