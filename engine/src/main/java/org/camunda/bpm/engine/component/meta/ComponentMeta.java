package org.camunda.bpm.engine.component.meta;

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
    private ComponentParameterMeta outputParameterMeta;

    public void addInputParameterMeta(ComponentParameterMeta parameterMeta){
        this.inputParameterMetas.add(parameterMeta);
    }

}
