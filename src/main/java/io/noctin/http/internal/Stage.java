package io.noctin.http.internal;

import io.noctin.http.api.Controller;
import io.noctin.http.api.Id;
import io.noctin.http.api.Param;
import io.noctin.http.api.Priority;
import io.noctin.http.exceptions.InvalidStageException;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public abstract class Stage implements Identified, Prioritized, Comparable<Stage> {

    protected final Controller controller;
    protected final Method method;

    protected final LinkedList<String> identifiers = new LinkedList<>();
    protected final LinkedHashMap<String, Class<?>> parameters = new LinkedHashMap<>();

    protected final long priority;

    public Stage(Controller controller, Method method) throws InvalidStageException {
        this.controller = controller;
        this.method = method;

        if (!this.method.getDeclaringClass().equals(controller.getClass())){
            throw new InvalidStageException(this, "method and declaring class does not match");
        }

        this.identifiers.add(this.method.getName());

        Id id = this.method.getDeclaredAnnotation(Id.class);

        if (id != null){
            for (String identifier : id.value()) {
                if (!this.identifiers.contains(identifier)) this.identifiers.add(identifier);
            }
        }

        Priority priority = method.getAnnotation(Priority.class);

        if (priority != null) {
            this.priority = priority.value();
        } else {
            this.priority = 0;
        }

        for (Parameter parameter : this.method.getParameters()) {
            Param paramAnnotation = parameter.getAnnotation(Param.class);

            if (paramAnnotation != null){
                parameters.put(paramAnnotation.value(), parameter.getType());
            }
        }
    }

    public final Controller getController() {
        return controller;
    }

    public final Method getMethod() {
        return this.method;
    }

    public final Map<String, Class<?>> getParameters() {
        return Collections.unmodifiableMap(this.parameters);
    }

    @Override
    public List<String> getIdentifiers() {
        return Collections.unmodifiableList(identifiers);
    }

    @Override
    public long getPriority() {
        return priority;
    }

    // Compare with priority
    @Override
    public int compareTo(Stage o) {
        return Long.compare(o.getPriority(), this.getPriority());
    }

    @Override
    public String toString() {
        return String.format("%s:%s", controller.getClass().getName(), method.getName());
    }
}
