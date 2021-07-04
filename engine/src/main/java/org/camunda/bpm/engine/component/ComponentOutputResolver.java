package org.camunda.bpm.engine.component;

import lombok.AllArgsConstructor;
import org.camunda.bpm.engine.component.meta.ComponentMeta;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

@AllArgsConstructor
public class ComponentOutputResolver {

    private ActivityExecution activityExecution;
    private ComponentMeta componentMeta;


    public void resolve(String result){
        System.out.println("Got result from nodeJs script");
        System.out.println(result);
        this.activityExecution.setVariable(this.componentMeta.getOutputParameterMeta().getName(),result);
    }
}
