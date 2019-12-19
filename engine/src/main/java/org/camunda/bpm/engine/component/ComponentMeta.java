package org.camunda.bpm.engine.component;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ComponentMeta {

    private String domain;
    private String name;
    private String scriptPath;
    private List<ComponentParameterMeta> inputParameterMetas = new ArrayList<>();
    private List<ComponentParameterMeta> outputParameterMetas = new ArrayList<>();

    public void addInputParameterMeta(ComponentParameterMeta parameterMeta){
        this.inputParameterMetas.add(parameterMeta);
    }

    public void addOutputParameterMeta(ComponentParameterMeta parameterMeta){
        this.outputParameterMetas.add(parameterMeta);
    }
}
