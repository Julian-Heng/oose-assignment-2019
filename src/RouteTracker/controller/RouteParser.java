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
    private static final
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

    private static final
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
                String[] split = parseRoute(line);

                if (routeTable.containsKey(split[0]))
                {
                    throw new RouteParserException(
                        "Duplicate route: " + split[0]
                    );
                }
                else if (split[1].isEmpty())
                {
                    throw new RouteParserException(
                        "Error while parsing route " + split[0] +
                        "\n" + (count + 1) + ": " + line +
                        "\nMissing description for route"
                    );
                }

                routeTable.put(split[0], new Integer(count));
            }
            else if (isPoint(line))
            {
                if (isSubRoute(line))
                {
                    String[] split = parsePoint(line);
                    subRouteSet.add(split[3].substring(1));
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

    public String[] parseRoute(String line) throws RouteParserException
    {
        String[] result = {parseRouteName(line), parseRouteDescription(line)};
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
                "Error while parsing line, can't parse route name:" +
                "\n" + formattedLineNo(line)
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
                "Error while parsing line, can't parse route description:" +
                "\n" + formattedLineNo(line)
            );
        }

        return result;
    }

    public String[] parsePoint(String line) throws RouteParserException
    {
        Matcher match = pointMatch.matcher(line.trim());
        String[] result = new String[4];

        if (match.find())
        {
            result[0] = match.group(1);
            result[1] = match.group(3);
            result[2] = match.group(5);
            result[3] = match.group(8);
        }
        else
        {
            throw new RouteParserException(
                "Error while parsing line, can't parse point:" +
                "\n" + formattedLineNo(line)
            );
        }

        return result;
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

    public boolean validateDistance(Point p1, Point p2) throws
                                                         RouteParserException
    {
        double distance = util.calcMetresDistance(p1.getLatitude(),
                                                  p1.getLongitude(),
                                                  p2.getLatitude(),
                                                  p2.getLongitude());

        return distance < 10 || Math.abs(distance - 10) < 0.0000001;
    }

    public boolean doubleRange(double num, double low, double high)
    {
        return ((num > low)  || Math.abs(num - low) < 0.0000001) &&
               ((high > num) || Math.abs(high - num) < 0.0000001);
    }

    public String formattedLineNo(String line)
    {
        return String.format("%d: %s", contents.indexOf(line) + 1, line);
    }

    public List<String> getContents()
    {
        return new ArrayList<String>(this.contents);
    }
}
