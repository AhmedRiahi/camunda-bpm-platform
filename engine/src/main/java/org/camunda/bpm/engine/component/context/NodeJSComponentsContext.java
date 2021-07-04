package org.camunda.bpm.engine.component.context;



import lombok.Data;
import org.camunda.bpm.engine.component.context.scanner.ComponentsScanner;
import org.camunda.bpm.engine.component.meta.ComponentMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class NodeJSComponentsContext {

    public static final NodeJSComponentsContext INSTANCE = new NodeJSComponentsContext();
    public static final String COMPONENTS_FOLDER = "/Users/e10114553/Documents/camunda-nodejs-components/";
    private ComponentsScanner componentsScanner = new ComponentsScanner();

    private List<ComponentMeta> componentMetaList =  new ArrayList<>();

    private NodeJSComponentsContext(){
        this.initComponents();

    }

    private void initComponents(){
        /*this.addComponentMeta(
                ComponentMeta.builder()
                        .domain("opensrs")
                        .name("domain_lookup")
                        .inputParameterMetas(Arrays.asList(ComponentParameterMeta.builder().name("domain_name").build()))
                        .outputParameterMeta(ComponentParameterMeta.builder().name("domain_exists").build())
                        .scriptPath(COMPONENTS_FOLDER+"/opensrs/src/test.js")
                        .build()
        );*/
        try {
            this.componentsScanner.startScanner();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addComponentMeta(ComponentMeta componentMeta){
        this.componentMetaList.add(componentMeta);
    }

    public Optional<ComponentMeta> getComponentByName(String name){
        return this.componentMetaList.stream().filter(component -> component.getName().equalsIgnoreCase(name)).findAny();
    }
}
