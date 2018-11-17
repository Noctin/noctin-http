package io.noctin.http.exceptions;

import io.noctin.http.api.Stage;

public class InvalidStageException extends HttpServerException {

    private final static String MESSAGE_HEAD = "Invalid stage: ";

    public InvalidStageException(Stage stage) {
        super(MESSAGE_HEAD + stage);
    }

    public InvalidStageException(String message){
        super(MESSAGE_HEAD + message);
    }

    public InvalidStageException(Stage stage, String message) {
        super(MESSAGE_HEAD + String.format("Stage %s is invalid: %s", stage, message));
    }
}
