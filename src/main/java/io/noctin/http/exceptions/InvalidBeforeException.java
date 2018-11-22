package io.noctin.http.exceptions;

import io.noctin.http.internal.Stage;

public class InvalidBeforeException extends InvalidStageException {
    public InvalidBeforeException(Stage stage) {
        super(stage);
    }
}
