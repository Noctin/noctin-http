package io.noctin.http.exceptions;

import io.noctin.http.internal.Stage;

public class InvalidEndpointException extends InvalidStageException {
    public InvalidEndpointException(Stage stage) {
        super(stage);
    }
}
