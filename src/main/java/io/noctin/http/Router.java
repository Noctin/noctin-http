package io.noctin.http;

import io.noctin.http.api.*;
import io.noctin.http.exceptions.InvalidStageException;
import io.noctin.http.exceptions.NotAStageException;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public final class Router {
    private LinkedList<Circuit> circuits = new LinkedList<>();

    public <T extends Controller> Circuit buildRoute(T controller){

        Class controllerClass = controller.getClass();
        LinkedList<Stage> stages = new LinkedList<>();

        for (Method method : controllerClass.getDeclaredMethods()) {
            try {
                Stage stage = new Stage(controller, method);

                stages.add(stage);
            } catch (NotAStageException e){
                System.out.println(e.getMessage());
            } catch (InvalidStageException e){
                e.printStackTrace();
            }
        }

        List<Stage> endpoints = stages.stream().filter(stage -> stage.getPurposes().contains(StageType.ENDPOINT)).collect(Collectors.toList());
        List<Stage> exceptionCaughts = stages.stream().filter(stage -> stage.getPurposes().contains(StageType.ENDPOINT)).collect(Collectors.toList());



        return null;
    }

    private class CircuitBuilder {
        private final Stage primary;

        public CircuitBuilder(Stage primary) {
            this.primary = primary;
        }

        public LinkedList<Stage> build(List<Stage> stages){



            return null;
        }
    }

}
