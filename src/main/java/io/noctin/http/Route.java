package io.noctin.http;

import io.noctin.http.api.Stage;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public final class Route {
    private final Path path;

    private final LinkedList<Stage> exceptionStages;
    private final LinkedList<Stage> commonStages;

    public Route(Path path, LinkedList<Stage> exceptionStages, LinkedList<Stage> commonStages) {
        this.path = path;
        this.exceptionStages = exceptionStages;
        this.commonStages = commonStages;
    }

    public Route(Path path) {
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

    public List<Stage> getExceptionStages() {
        return Collections.unmodifiableList(this.exceptionStages);
    }

    public List<Stage> getCommonStages() {
        return Collections.unmodifiableList(this.commonStages);
    }

    public Path getPath() {
        return path;
    }
}
