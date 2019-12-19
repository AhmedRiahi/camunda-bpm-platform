package org.camunda.bpm.engine.exception;

public class ComponentNotFoundException extends RuntimeException{


    private String componentName;

    public ComponentNotFoundException(String componentName){
        this.componentName = componentName;
    }

    @Override
    public String getMessage() {
        return "Component Not Found : "+this.componentName;
    }
}
