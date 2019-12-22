package org.camunda.bpm.engine.component;

import com.eclipsesource.v8.V8;
import com.eclipsesource.v8.V8Array;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.component.meta.ComponentMeta;
import org.camunda.bpm.engine.component.meta.ComponentParameterMeta;
import org.camunda.bpm.engine.impl.json.JsonObjectConverter;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;


@AllArgsConstructor
public class ComponentInputResolver {

    private V8 nodeJsRuntime;
    private ComponentMeta componentMeta;
    private ActivityExecution activityExecution;


    public V8Array resolve(){
        ObjectMapper objectMapper = new ObjectMapper();
        V8Array inputs = new V8Array(this.nodeJsRuntime);
        this.componentMeta.getInputParameterMetas().stream()
                .map(ComponentParameterMeta::getName)
                .map(this.activityExecution::getVariable)
                .map(parameter -> {
                    try {
                        return objectMapper.writeValueAsString(parameter);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
        .forEach(inputs::push);

        return inputs;
    }
}
