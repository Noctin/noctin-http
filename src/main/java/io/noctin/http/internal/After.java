package io.noctin.http.internal;

import io.noctin.http.api.Controller;
import io.noctin.http.exceptions.InvalidAfterException;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;

public final class After extends Stage implements Identifier {

    private final LinkedList<String> afterIds;

    public After(Controller controller, Method method) throws InvalidAfterException {
        super(controller, method);

        io.noctin.http.api.After afterAnnotation = this.method.getAnnotation(io.noctin.http.api.After.class);

        if (afterAnnotation != null) {
            this.afterIds = new LinkedList<>(Arrays.asList(afterAnnotation.value()));
        } else {
            throw new InvalidAfterException(this);
        }
    }

    public LinkedList<String> getAfterIds() {
        return afterIds;
    }

    @Override
    public boolean identify(Identified identified) {
        for (String id : identified.getIdentifiers()) {
            if (this.afterIds.contains(id)) return true;
        }

        return false;
    }
}
