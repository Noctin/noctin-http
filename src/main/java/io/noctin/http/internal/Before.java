package io.noctin.http.internal;

import io.noctin.http.api.Controller;
import io.noctin.http.exceptions.InvalidBeforeException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;

public final class Before extends Stage implements Identifier {

    private final LinkedList<String> beforeIds;

    public Before(Controller controller, Method method) throws InvalidBeforeException {
        super(controller, method);

        io.noctin.http.api.Before beforeAnnotation = this.method.getAnnotation(io.noctin.http.api.Before.class);

        if (beforeAnnotation != null) {
            this.beforeIds = new LinkedList<>(Arrays.asList(beforeAnnotation.value()));
        } else {
            throw new InvalidBeforeException(this);
        }
    }

    public LinkedList<String> getBeforeIds() {
        return beforeIds;
    }

    @Override
    public boolean identify(Identified identified) {
        for (String id : identified.getIdentifiers()) {
            if (this.beforeIds.contains(id)) return true;
        }

        return false;
    }
}
