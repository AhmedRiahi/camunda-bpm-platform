package org.camunda.bpm.engine.impl.bpmn.behavior;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8Function;
import org.camunda.bpm.engine.component.ComponentInputResolver;
import org.camunda.bpm.engine.component.ComponentOutputResolver;
import org.camunda.bpm.engine.component.meta.ComponentMeta;
import org.camunda.bpm.engine.component.context.NodeJSComponentsContext;
import org.camunda.bpm.engine.exception.ComponentNotFoundException;
import org.camunda.bpm.engine.impl.core.variable.mapping.value.ParameterValueProvider;
import org.camunda.bpm.engine.impl.persistence.entity.ExecutionEntity;
import org.camunda.bpm.engine.impl.pvm.delegate.ActivityExecution;

import java.io.File;

public class NodeJSTaskActivityBehavior extends AbstractBpmnActivityBehavior {

    private ParameterValueProvider nodeJSComponentName;

    public NodeJSTaskActivityBehavior(ParameterValueProvider nodeJSComponentName){
        this.nodeJSComponentName = nodeJSComponentName;
    }

    @Override
    public void execute(ActivityExecution activityExecution) throws Exception {
        System.out.println("Nodejs Task execution goes here");
        ExecutionEntity executionEntity = (ExecutionEntity) activityExecution;
        String componentName = (String) this.nodeJSComponentName.getValue(executionEntity);
        ComponentMeta componentMeta = NodeJSComponentsContext.INSTANCE.getComponentByName(componentName).orElseThrow(() -> new ComponentNotFoundException(componentName));

        final NodeJS nodeJS = NodeJS.createNodeJS();
        ComponentOutputResolver outputResolver = new ComponentOutputResolver(activityExecution,componentMeta);
        nodeJS.getRuntime().registerJavaMethod(outputResolver,"resolve", "resolve",new Class[]{String.class});
        V8Function mainFunction = (V8Function) nodeJS.require(new File(componentMeta.getScriptPath())).get("main");
        mainFunction.registerJavaMethod(outputResolver,"resolve", "resolve",new Class[]{String.class});

        ComponentInputResolver componentInputResolver = new ComponentInputResolver(nodeJS.getRuntime(),componentMeta,activityExecution);
        mainFunction.call(null,componentInputResolver.resolve());
        while(nodeJS.isRunning()) {
            nodeJS.handleMessage();
        }
        mainFunction.release();
        nodeJS.release();

    }


}
