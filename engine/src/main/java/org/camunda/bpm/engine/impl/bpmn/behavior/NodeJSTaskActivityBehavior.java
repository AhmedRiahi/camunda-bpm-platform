package org.camunda.bpm.engine.impl.bpmn.behavior;

import com.eclipsesource.v8.NodeJS;
import com.eclipsesource.v8.V8Array;
import com.eclipsesource.v8.V8Function;
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
        V8Function mainFunction = (V8Function) nodeJS.require(new File(componentMeta.getScriptPath())).get("main");
        mainFunction.registerJavaMethod(resultResolver,"resolve", "resolve",new Class[]{String.class});

        V8Array inputs = new V8Array(mainFunction.getRuntime());
        inputs.push("foo.com");
        mainFunction.call(null,inputs);
        while(nodeJS.isRunning()) {
            nodeJS.handleMessage();
        }
        mainFunction.release();
        nodeJS.getRuntime().release();
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
