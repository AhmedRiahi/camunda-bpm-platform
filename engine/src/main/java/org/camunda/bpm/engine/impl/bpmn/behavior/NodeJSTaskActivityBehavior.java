package org.camunda.bpm.engine.impl.bpmn.behavior;

import com.eclipsesource.v8.NodeJS;
import org.camunda.bpm.engine.component.ComponentMeta;
import org.camunda.bpm.engine.component.NodeJSComponentsContext;
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
        ResultResolver resultResolver = new ResultResolver(activityExecution);
        nodeJS.getRuntime().registerJavaMethod(resultResolver,"resolve", "resolve",new Class[]{String.class});
        File nodeScript = new File(componentMeta.getScriptPath());

        nodeJS.exec(nodeScript);

        while(nodeJS.isRunning()) {
            nodeJS.handleMessage();
        }
        nodeJS.release();
    }


    public class ResultResolver{

        private ActivityExecution activityExecution;

        public ResultResolver(ActivityExecution activityExecution){
            this.activityExecution = activityExecution;
        }

        public void resolve(String result){
            System.out.println("Got result from nodeJs script");
            System.out.println(result);
            this.activityExecution.setVariable("output",result);
        }
    }

}
