package io.noctin.http.api;

import io.noctin.http.exceptions.InvalidStageException;
import io.noctin.http.exceptions.NotAStageException;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class Stage {

    public static final String PROPERTY_HTTPMETHOD = "http_method";
    public static final String PROPERTY_AFTER_IDS = "after_id";
    public static final String PROPERTY_BEFORE_IDS = "before_id";
    public static final String PROPERTY_ENDPOINT_RAWPATH = "endpoint_path";

    private final Controller controller;
    private final Method method;

    private final LinkedList<String> identifiers = new LinkedList<>();
    private final LinkedHashMap<String, Class<?>> parameters = new LinkedHashMap<>();

    private final LinkedList<StageType> purposes = new LinkedList<>();
    private final LinkedHashMap<String, Object> properties = new LinkedHashMap<>();

    public Stage(Controller controller, Method method) throws InvalidStageException {
        this.controller = controller;
        this.method = method;

        this.build();
    }

    private void build() throws InvalidStageException {
        if (!this.method.getDeclaringClass().equals(controller.getClass())){
            throw new InvalidStageException(this, "method and declaring class does not match");
        }

        if (!isStage(method)) throw new NotAStageException(this);

        if (this.method.getReturnType().isPrimitive()) throw new InvalidStageException(this, "the return type must not be primitive");

        for (Annotation annotation : this.method.getDeclaredAnnotations()) {
            StageType type = StageType.forAnnotation(annotation.annotationType());

            if (type != null) purposes.add(type);
        }

        // If the method is annotated as an endpoint and as an exception caught
        if (this.purposes.contains(StageType.ENDPOINT) && purposes.contains(StageType.EXCEPTION_CAUGHT)) {
            throw new InvalidStageException(this, "incompatible types EndPoint and ExceptionCaught");
        }

        if (this.purposes.contains(StageType.ENDPOINT)){

            Endpoint endpoint = this.method.getDeclaredAnnotation(Endpoint.class);

            this.properties.put(PROPERTY_ENDPOINT_RAWPATH, endpoint.value());

            HttpMethod httpMethod = this.method.getDeclaredAnnotation(HttpMethod.class);

            if (httpMethod != null){
                this.properties.put(PROPERTY_HTTPMETHOD, httpMethod.value());
            } else {
                this.properties.put(PROPERTY_HTTPMETHOD, HttpMethodName.GET);
            }
        }

        if (this.purposes.contains(StageType.BEFORE)){
            Before before = this.method.getDeclaredAnnotation(Before.class);

            this.properties.put(PROPERTY_BEFORE_IDS, Arrays.asList(before.value()));
        }

        if (this.purposes.contains(StageType.AFTER)){
            After after = this.method.getDeclaredAnnotation(After.class);

            this.properties.put(PROPERTY_AFTER_IDS, Arrays.asList(after.value()));
        }

        this.identifiers.add(this.method.getName());

        Id id = this.method.getDeclaredAnnotation(Id.class);

        if (id != null){
            for (String identifier : id.value()) {
                if (!this.identifiers.contains(identifier)) this.identifiers.add(identifier);
            }
        }

        for (Parameter parameter : this.method.getParameters()) {
            Param paramAnnotation = parameter.getAnnotation(Param.class);

            if (paramAnnotation != null){
                parameters.put(paramAnnotation.value(), parameter.getType());
            }
        }
    }

    private boolean isStage(Method method){
        for (Annotation annotation : method.getDeclaredAnnotations()) {
            if (StageType.forAnnotation(annotation.annotationType()) != null) return true;
        }
        return false;
    }

    public Controller getController() {
        return controller;
    }

    public Method getMethod() {
        return this.method;
    }

    public List<String> getIdentifiers() {
        return Collections.unmodifiableList(this.identifiers);
    }

    public Map<String, Class<?>> getParameters() {
        return Collections.unmodifiableMap(this.parameters);
    }

    public Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

    public List<StageType> getPurposes() {
        return Collections.unmodifiableList(this.purposes);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", controller.getClass().getName(), method.getName());
    }
}
