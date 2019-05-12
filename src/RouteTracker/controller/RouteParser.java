package RouteTracker.controller;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;

import RouteTracker.model.*;
import RouteTracker.model.exception.*;

/**
 * RouteParser class for parsing the data from GeoUtils
 * @author Julian Heng (19473701)
 **/
public class RouteParser
{
    private GeoUtils utils;
    private List<String> contents;
    private Map<List<String>,List<List<String>>> routeTable;
    private Map<String,List<String>> routeNameTable;

    // Regex patterns

    /**
     * Input: mainRoute Since I was young
     *
     * Group 1: mainRoute Since I was young
     * Group 2: mainRoute
     * Group 3: Since I was young
     **/

    private static
    Pattern routeMatch = Pattern.compile(
        "(?=^[a-z]+[A-Z][A-Za-z]*)((^\\w+)\\s+([^\\s]+.*))"
    );

    /**
     * The worst regex string you've ever seen
     *
     * Input: -31.95,115.77,44.8,*theStroll
     *
     * Group 1: -31.95
     * Group 2: 31.
     * Group 3: 115.77
     * Group 4: 115.
     * Group 5: 44.8
     * Group 6: 44.
     * Group 7: ,*theStroll
     * Group 8: theStroll
     */

    private static
    Pattern pointMatch = Pattern.compile(
        "([-]?(\\d+.)?\\d+),\\s*" + // Latitude
        "([-]?(\\d+.)?\\d+),\\s*" + // Longitude
        "([-]?(\\d+.)?\\d+)" +      // Altitude
        "(,\\s*((\\*?.*)))?"        // Description
    );

    public RouteParser(GeoUtils utils)
    {
        this.utils = utils;
        contents = new ArrayList<>();
        routeTable = new HashMap<>();
        routeNameTable = new HashMap<>();
    }

    /**
     * Convert the String input from GeoUtils to a list of Strings
     * @throws RouteParserException If there's an error in GeoUtils, throw
     *                              it to calling method
     **/
    public void readData() throws RouteParserException
    {
        try
        {
            contents = new ArrayList<>(
                Arrays.asList(utils.retrieveRouteData().split("\n"))
            );
        }
        catch (IOException e)
        {
            throw new RouteParserException(e.getMessage());
        }
    }

    /**
     * Makes a map of route and it's points and sub-routes declaration
     * so that we can have O(1) access to the entire route
     *
     * @throws RouteParserException If detects duplicate route declarations
     *                              and missing sub-route declarations Does not
     *                              detect if the coordinates are valid, that
     *                              is handled in the factories
     **/
    public void makeIndex() throws RouteParserException
    {
        Set<String> routeSet, subRouteSet;
        Iterator<String> iter = contents.iterator();
        String line;

        // Empty the routeTable
        routeTable = new HashMap<>();
        routeNameTable = new HashMap<>();

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
                        // We should not be expecting a route declaration
                        // Hence missing terminating line
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
                                subRouteSet.add(
                                    pointEntry.get(3).substring(1)
                                );
                            }
                        }
                        // Anything that did not match
                        else
                        {
                            throw new RouteParserException(
                                "Invalid line: " + line
                            );
                        }

                        // If for some reason the point parser returns an empty
                        // list
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

                // Check if there's a missing terminating line
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
                errMsg += routeSet.contains(name) ? "" : "\n    " + name;
            }

            throw new RouteParserException(
                "Sub-route(s) declaration not found:" + errMsg
            );
        }

        // Begin making the routeNameTable for looking up route's
        // description using the route name
        //
        // Useful for finding routes an their description for sub-routes
        routeTable.forEach((k, v)->routeNameTable.put(k.get(0), k));
    }

    public Map<List<String>,List<List<String>>> getRouteTable()
    {
        return routeTable;
    }

    public Map<String,List<String>> getRouteNameTable()
    {
        return routeNameTable;
    }

    public List<String> parseRoute(String line) throws RouteParserException
    {
        String[] result = {
            parseRouteName(line), parseRouteDescription(line)
        };

        return Arrays.asList(result);
    }

    public List<String> parsePoint(String line) throws RouteParserException
    {
        String[] result = {
            parsePointLatitude(line), parsePointLongitude(line),
            parsePointAltitude(line), parsePointDescription(line)
        };

        return Arrays.asList(result);
    }

    public String parseRouteName(String line) throws RouteParserException
    {
        return getRegexGroup(routeMatch, line, "route name", 2);
    }

    public String parseRouteDescription(String line) throws
                                                     RouteParserException
    {
        return getRegexGroup(routeMatch, line, "route description", 3);
    }

    public String parsePointLatitude(String line) throws RouteParserException
    {
        return getRegexGroup(pointMatch, line, "point latitude", 1);
    }

    public String parsePointLongitude(String line) throws RouteParserException
    {
        return getRegexGroup(pointMatch, line, "point longitude", 3);
    }

    public String parsePointAltitude(String line) throws RouteParserException
    {
        return getRegexGroup(pointMatch, line, "point altitude", 5);
    }

    public String parsePointDescription(String line) throws
                                                     RouteParserException
    {
        String result;
        result = getRegexGroup(pointMatch, line, "point description", 8);
        return result != null ? result : "";
    }

    /**
     * Return the regex group of a String using a pattern
     * @param pat        The Pattern object that contains a compiled regex
     * @param line       The line to match the regex to
     * @param identifier A String used for the error message to identify
     *                   which group we're extracting
     * @param i          The group number in the regex
     * @return The string within the regex group
     **/
    public String getRegexGroup(Pattern pat, String line,
                                String identifier,
                                int i) throws RouteParserException
    {
        Matcher match = pat.matcher(line.trim());
        String result;

        if (match.find())
        {
            result = match.group(i);
        }
        else
        {
            throw new RouteParserException(
                "Error while parsing line, can't parse " + identifier
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

    public List<String> getContents()
    {
        return new ArrayList<String>(this.contents);
    }
}
