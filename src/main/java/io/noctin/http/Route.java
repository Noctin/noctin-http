package io.noctin.http;

import io.noctin.http.api.Stage;

import java.util.LinkedList;

public final class Route {
    private final String path;

    private final LinkedList<Stage> exceptionStages;
    private final LinkedList<Stage> commonStages;

    public Route(String path, LinkedList<Stage> exceptionStages, LinkedList<Stage> commonStages) {
        this.path = path;
        this.exceptionStages = exceptionStages;
        this.commonStages = commonStages;
    }

    public Route(String path) {
        this.path = path;
        this.exceptionStages = new LinkedList<>();
        this.commonStages = new LinkedList<>();
    }

    public void putExceptionStages(LinkedList<Stage> stages){
        this.exceptionStages.addAll(stages);
    }

    public void putCommonStages(LinkedList<Stage> stages){
        this.commonStages.addAll(stages);
    }
}
