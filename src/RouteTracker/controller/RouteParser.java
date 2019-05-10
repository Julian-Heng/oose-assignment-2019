package RouteTracker.controller;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;

import RouteTracker.model.*;
import RouteTracker.model.exception.*;

public class RouteParser
{
    private GeoUtils util;
    private List<String> contents;
    private Map<List<String>,List<List<String>>> routeTable;

    // Regex patterns

    // mainRoute Since I was young
    // Group 1: mainRoute Since I was young
    // Group 2: mainRoute
    // Group 3: Since I was young

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
        contents = new ArrayList<>();
        routeTable = new HashMap<>();
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

    // Makes a map of route and it's points and sub-routes declaration
    // so that we can have O(1) access to the entire route
    //
    // Throws RouteParserException when detects duplicate route declarations
    // and missing sub-route declarations
    //
    // Does not detect if the coordinates are valid, that is handled in the
    // factories
    public void makeIndex() throws RouteParserException
    {
        Set<String> routeSet, subRouteSet;
        Iterator<String> iter = contents.iterator();
        String line;

        // Empty the routeTable
        routeTable = new HashMap<>();

        // Sets for validation
        routeSet = new HashSet<>();
        subRouteSet = new HashSet<>();

        while (iter.hasNext())
        {
            line = iter.next();

            if (line.trim().isEmpty())
            {
                // Skip empty/newline lines
            }
            else if (isRoute(line))
            {
                boolean isEnd = false;
                List<String> routeInfo = parseRoute(line);
                List<List<String>> routeEntry = new ArrayList<>();

                if (routeTable.containsKey(routeInfo))
                {
                    throw new RouteParserException(
                        "Invalid line: " + line +
                        "\nDuplicate route name"
                    );
                }

                while (iter.hasNext() && ! isEnd)
                {
                    List<String> pointEntry = new ArrayList<>();
                    line = iter.next();
                    isEnd = isEndPoint(line);

                    if (! line.trim().isEmpty())
                    {
                        if (isRoute(line))
                        {
                            throw new RouteParserException(
                                "Invalid line: " + line +
                                "\nMissing terminating line"
                            );
                        }
                        else if (isPoint(line))
                        {
                            pointEntry = parsePoint(line);
                            if (isSubRoute(line))
                            {
                                String subName = pointEntry.get(3).substring(1);
                                subRouteSet.add(subName);
                            }
                        }
                        else
                        {
                            throw new RouteParserException(
                                "Invalid line: " + line
                            );
                        }

                        if (pointEntry.isEmpty())
                        {
                            throw new RouteParserException(
                                "Invalid line: " + line +
                                "\nNo points in route"
                            );
                        }

                        routeEntry.add(pointEntry);
                    }
                }

                if (! isEnd)
                {
                    throw new RouteParserException(
                        "Invalid line: " + line +
                        "\nMissing terminating line"
                    );
                }

                routeTable.put(routeInfo, routeEntry);
            }
            else
            {
                throw new RouteParserException(
                    "Invalid line: " + line
                );
            }
        }

        // Get all the names of the routes into a set
        routeTable.forEach((k, v)->routeSet.add(k.get(0)));

        // routeSet is a super-set of subRouteSet Therefore, all of
        // subRouteSet should be in routeSet If not, then there's a route with
        // a sub-route that does not exist
        if (! routeSet.containsAll(subRouteSet))
        {
            Set<String> missingSet = new HashSet<>(subRouteSet);
            String errMsg = "";

            for (String name : missingSet)
            {
                errMsg += ! routeSet.contains(name) ? "\n    " + name : "";
            }

            throw new RouteParserException(
                "Sub-route(s) declaration not found:" + errMsg
            );
        }
    }

    public List<String> parseRoute(String line) throws RouteParserException
    {
        String[] result = {
            parseRouteName(line),
            parseRouteDescription(line)
        };

        return Arrays.asList(result);
    }

    public List<String> parsePoint(String line) throws RouteParserException
    {
        String[] result = {
            parsePointLatitude(line),
            parsePointLongitude(line),
            parsePointAltitude(line),
            parsePointDescription(line)
        };

        return Arrays.asList(result);
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
