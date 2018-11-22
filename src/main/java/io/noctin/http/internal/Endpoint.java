package io.noctin.http.internal;

import io.noctin.http.Path;
import io.noctin.http.api.Controller;
import io.noctin.http.api.HttpMethod;
import io.noctin.http.exceptions.InvalidEndpointException;

import java.lang.reflect.Method;

public final class Endpoint extends Stage {

    private final Path path;
    private final HttpMethod httpMethod;

    public Endpoint(Controller controller, Method method) throws InvalidEndpointException {
        super(controller, method);

        if (this.method.getReturnType().isPrimitive()) throw new InvalidEndpointException(this);

        io.noctin.http.api.Endpoint endpointAnnotation = this.method.getAnnotation(io.noctin.http.api.Endpoint.class);

        if (endpointAnnotation != null) {
            String rawPath = endpointAnnotation.path();

            this.path = new Path(rawPath);

            this.httpMethod = endpointAnnotation.method();
        } else {
            throw new InvalidEndpointException(this);
        }
    }

    public Path getPath() {
        return path;
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }
}
