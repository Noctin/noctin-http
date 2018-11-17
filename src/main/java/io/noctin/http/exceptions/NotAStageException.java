package io.noctin.http.exceptions;

import io.noctin.http.api.Stage;

public class NotAStageException extends InvalidStageException {
    public NotAStageException(Stage stage) {
        super(stage, "not a stage");
    }
}
