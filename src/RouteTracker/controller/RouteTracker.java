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
                parser.makeIndex();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
