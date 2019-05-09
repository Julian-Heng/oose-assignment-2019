import java.util.*;

import RouteTracker.controller.*;
import RouteTracker.model.*;

public class RouteTracker
{
    public static void main(String[] args)
    {
        if (args.length > 0)
        {
            RouteParser parser = new RouteParser(new GeoUtils());

            try
            {
                parser.readData(args[0]);
                for (String line : parser.getContents())
                {
                    if (parser.isRoute(line))
                    {
                        System.out.println(Arrays.toString(parser.parseRoute(line)));
                    }
                    else if (parser.isPoint(line))
                    {
                        System.out.println(Arrays.toString(parser.parsePoint(line)));
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
