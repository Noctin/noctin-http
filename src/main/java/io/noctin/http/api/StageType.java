package io.noctin.http.api;

import java.lang.annotation.Annotation;

public enum StageType {
    ENDPOINT(Endpoint.class),
    EXCEPTION_CAUGHT(ExceptionCaught.class),
    BEFORE(Before.class),
    AFTER(After.class);

    private Class<? extends Annotation> annotation;

    StageType(Class<? extends Annotation> annotation) {
        this.annotation = annotation;
    }

    public Class<? extends Annotation> getAnnotation() {
        return annotation;
    }

    public static StageType forAnnotation(Class<?> clazz){
        StageType type = null;

        for (StageType stageType : StageType.values()) {
            if (stageType.getAnnotation().equals(clazz)){
                type = stageType;
                break;
            }
        }

        return type;
    }
}
