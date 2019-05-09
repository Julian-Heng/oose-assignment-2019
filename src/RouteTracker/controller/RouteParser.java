package RouteTracker.controller;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;

import RouteTracker.model.*;

public class RouteParser
{
    private GeoUtils util;
    private List<String> contents;

    // Regex patterns
    private static
    Pattern routeMatch = Pattern.compile(
        "(?=^[a-z]+[A-Z][A-Za-z]*)((^\\w+)\\s+([^\\s]+.*))"
    );

    // The worst regex string you've ever seen
    //
    // -31.95,115.77,44.8,*theStroll
    // Group 1: -31.95
    // Group 2: 31.
    // Group 3: 115.77
    // Group 4: 115.
    // Group 5: 44.8
    // Group 6: 44.
    // Group 7: ,*theStroll
    // Group 8: theStroll

    private static
    Pattern pointMatch = Pattern.compile(
        "([-]?(\\d+.)?\\d+),\\s*" + // Latitude
        "([-]?(\\d+.)?\\d+),\\s*" + // Longitude
        "([-]?(\\d+.)?\\d+)" +      // Altitude
        "(,\\s*((\\*?.*)))?"        // Description
    );

    public RouteParser(GeoUtils util)
    {
        this.util = util;
        this.contents = new ArrayList<>();
    }

    public void readData(String url) throws RouteParserException
    {
        try
        {
            contents = new ArrayList<>(
                Arrays.asList(util.retrieveRouteData(url).split("\n"))
            );
        }
        catch (IOException e)
        {
            throw new RouteParserException(e.getMessage());
        }
    }

    /*
    // Makes a map of route and sub-route declaration along with the
    // location in the file contents so that we can have O(1) access
    // to the location of the declaration
    //
    // Throws RouteParserException when detects duplicate route declarations
    // and missing sub-route declarations
    public Map<String,Integer> makeIndex() throws RouteParserException
    {
        Map<String,Integer> routeTable = new HashMap<>();
        Set<String> subRouteSet = new HashSet<>();
        Set<String> nameSet;
        int count = 0;

        for (String line : contents)
        {
            if (line.trim().isEmpty())
            {
            }
            else if (isRoute(line))
            {
                String[] routeInfo = parseRoute(line);

                if (routeTable.containsKey(routeInfo[0]))
                {
                    throw new RouteParserException(
                        "Duplicate route: " + routeInfo[0]
                    );
                }
                else if (routeInfo[1].isEmpty())
                {
                    throw new RouteParserException(
                        "Error while parsing route " + routeInfo[0] +
                        "\n" + (count + 1) + ": " + line +
                        "\nMissing description for route"
                    );
                }

                routeTable.put(routeInfo[0], new Integer(count));
            }
            else if (isPoint(line))
            {
                if (isSubRoute(line))
                {
                    String[] pointInfo = parsePoint(line);
                    subRouteSet.add(pointInfo[3].substring(1));
                }
            }
            else
            {
                throw new RouteParserException(
                    "Syntax error on line " + (count + 1) +
                    ": " + line
                );
            }

            count++;
        }

        nameSet = new HashSet<>(routeTable.keySet());

        if (! nameSet.containsAll(subRouteSet))
        {
            Set<String> missingSet = new HashSet<>(subRouteSet);
            String errMsg = "";

            for (String name : missingSet)
            {
                if (! nameSet.contains(name))
                {
                    errMsg += "\n    " + name;
                }
            }

            throw new RouteParserException(
                "Sub-route(s) declaration not found:" + errMsg
            );
        }

        return routeTable;
    }
    */

    public String[] parseRoute(String line) throws RouteParserException
    {
        String[] result = {
            parseRouteName(line),
            parseRouteDescription(line)
        };

        return result;
    }

    public String parseRouteName(String line) throws RouteParserException
    {
        Matcher match = routeMatch.matcher(line.trim());
        String result;

        if (match.find())
        {
            result = match.group(2);
        }
        else
        {
            throw new RouteParserException(
                "Error while parsing line, can't parse route name"
            );
        }

        return result;
    }

    public String parseRouteDescription(String line) throws RouteParserException
    {
        Matcher match = routeMatch.matcher(line.trim());
        String result;

        if (match.find())
        {
            result = match.group(3);
        }
        else
        {
            throw new RouteParserException(
                "Error while parsing line, can't parse route description"
            );
        }

        return result;
    }

    public String[] parsePoint(String line) throws RouteParserException
    {
        String[] result = {
            parsePointLatitude(line),
            parsePointLongitude(line),
            parsePointAltitude(line),
            parsePointDescription(line)
        };

        return result;
    }

    public String parsePointLatitude(String line) throws RouteParserException
    {
        Matcher match = pointMatch.matcher(line.trim());
        String result;

        if (match.find())
        {
            result = match.group(1);
        }
        else
        {
            throw new RouteParserException(
                "Error while parsing line, can't parse point latitude"
            );
        }

        return result;
    }

    public String parsePointLongitude(String line) throws RouteParserException
    {
        Matcher match = pointMatch.matcher(line.trim());
        String result;

        if (match.find())
        {
            result = match.group(3);
        }
        else
        {
            throw new RouteParserException(
                "Error while parsing line, can't parse point longitude"
            );
        }

        return result;
    }

    public String parsePointAltitude(String line) throws RouteParserException
    {
        Matcher match = pointMatch.matcher(line.trim());
        String result;

        if (match.find())
        {
            result = match.group(5);
        }
        else
        {
            throw new RouteParserException(
                "Error while parsing line, can't parse point altitude"
            );
        }

        return result;
    }

    public String parsePointDescription(String line) throws RouteParserException
    {
        Matcher match = pointMatch.matcher(line.trim());
        String result;

        if (match.find())
        {
            result = match.group(8);
        }
        else
        {
            throw new RouteParserException(
                "Error while parsing line, can't parse point description"
            );
        }

        return result != null ? result : "";
    }

    public boolean isRoute(String line)
    {
        return routeMatch.matcher(line.trim()).find();
    }

    public boolean isPoint(String line)
    {
        return pointMatch.matcher(line.trim()).find();
    }

    public boolean isSubRoute(String line)
    {
        Matcher match = pointMatch.matcher(line.trim());
        return match.find() &&
               match.group(8) != null &&
               match.group(8).charAt(0) == '*';
    }

    public boolean isEndPoint(String line)
    {
        Matcher match = pointMatch.matcher(line.trim());
        return match.find() && match.group(7) == null;
    }

    /*
    public double getDistance(Point p1, Point p2)
    {
        return util.calcMetresDistance(p1.getLatitude(),
                                       p1.getLongitude(),
                                       p2.getLatitude(),
                                       p2.getLongitude());
    }

    public boolean validateDistance(Point p1, Point p2)
    {
        return Double.compare(getDistance(p1, p2), 10) <= 0;
    }
    */

    public boolean doubleRange(double num, double low, double high)
    {
        return Double.compare(num, low) >= 0 &&
               Double.compare(high, num) >= 0;
    }

    public List<String> getContents()
    {
        return new ArrayList<String>(this.contents);
    }
}
