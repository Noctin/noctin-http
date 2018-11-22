package io.noctin.http.internal;

import io.noctin.http.api.Controller;
import io.noctin.http.exceptions.InvalidCatchException;

import java.lang.reflect.Method;

public final class Catch extends Stage {
    public Catch(Controller controller, Method method) throws InvalidCatchException {
        super(controller, method);

        io.noctin.http.api.Catch caughtAnnotation = method.getAnnotation(io.noctin.http.api.Catch.class);

        if (caughtAnnotation == null) {
            throw new InvalidCatchException(this);
        }
    }
}
