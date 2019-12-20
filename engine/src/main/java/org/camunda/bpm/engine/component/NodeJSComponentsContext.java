package org.camunda.bpm.engine.component;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


public class NodeJSComponentsContext {


    public static final NodeJSComponentsContext INSTANCE = new NodeJSComponentsContext();

    private List<ComponentMeta> componentMetaList =  new ArrayList<>();

    private NodeJSComponentsContext(){
        this.initComponents();
    }

    private void initComponents(){
        this.addComponentMeta(
                ComponentMeta.builder()
                        .domain("opensrs")
                        .name("domain_lookup")
                        .inputParameterMetas(Arrays.asList(ComponentParameterMeta.builder().name("domain_name").build()))
                        .outputParameterMetas(Arrays.asList(ComponentParameterMeta.builder().name("domain_exists").build()))
                        .scriptPath("/Users/e10114553/Documents/camunda-nodejs-components/opensrs/src/test.js")
                        .build()
        );
    }

    public void addComponentMeta(ComponentMeta componentMeta){
        this.componentMetaList.add(componentMeta);
    }

    public Optional<ComponentMeta> getComponentByName(String name){
        return this.componentMetaList.stream().filter(component -> component.getName().equalsIgnoreCase(name)).findAny();
    }
}
