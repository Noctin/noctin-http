package io.noctin.http.exceptions;

import io.noctin.http.internal.Stage;

public class InvalidAfterException extends InvalidStageException {
    public InvalidAfterException(Stage stage) {
        super(stage);
    }
}
