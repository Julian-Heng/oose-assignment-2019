package RouteTracker.controller;

import java.util.*;
import RouteTracker.model.*;

/**
 * RouteTracker class to hold the routes in the running app
 * @author Julian Heng (19473701)
 **/
public class RouteTracker
{
    private Map<String,Route> routes;
    public RouteTracker() { routes = new HashMap<>(); }
    public Map<String,Route> getRoutes() { return routes; }
    public void setRoutes(Map<String,Route> routes) { this.routes = routes; }
}
