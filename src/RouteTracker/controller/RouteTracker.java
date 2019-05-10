import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class RouteTracker
{
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            GeoUtils utils = new GeoUtils();
            RouteParser parser = new RouteParser(utils);

            PointFactory pointMaker;
            RouteFactory routeMaker;

            Map<String,Route> routes;

            try
            {
                parser.readData(args[0]);
                parser.makeIndex();

                pointMaker = new PointFactory(parser, utils);
                routeMaker = new RouteFactory(pointMaker, parser, utils);

                routes = routeMaker.make();

                routes.forEach((k, v)->{
                    System.out.println(v.toString() + "\n");
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
