package io.noctin.http.exceptions;

import io.noctin.http.internal.Stage;

public class InvalidCatchException extends InvalidStageException {
    public InvalidCatchException(Stage stage) {
        super(stage);
    }
}
