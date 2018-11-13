package io.noctin.http;

import io.noctin.http.api.Controller;

import java.util.LinkedList;

public final class Router {
    private LinkedList<Route> routes = new LinkedList<>();

    public Route buildRoute(Class<? extends Controller> controller){
        return null;
    }
}
