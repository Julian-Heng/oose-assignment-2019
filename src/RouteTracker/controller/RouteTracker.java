import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;
// import RouteTracker.view.*;

public class RouteTracker
{
    public static void main(String[] args)
    {
        RouteParser parser;
        Map<String,Route> routes;

        if (args.length > 0)
        {
            parser = new RouteParser(args[0]);

            try
            {
                parser.readFile();
                routes = parser.parseRoutes();

                for (Map.Entry<String,Route> r: routes.entrySet())
                {
                    System.out.println(r.getValue().toString() + "\n");
                }
            }
            catch (RouteParserException e)
            {
                System.out.println(e.getMessage());
            }
        }
    }
}
