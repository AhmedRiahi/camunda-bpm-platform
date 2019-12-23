package org.camunda.bpm.engine.component.context.scanner;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.camunda.bpm.engine.component.context.NodeJSComponentsContext;

public class ComponentsScanner {

    private CamelContext camelContext = new DefaultCamelContext();


    public void startScanner() throws Exception {
        this.camelContext.addRoutes(new RouteBuilder() {
            @Override
            public void configure(){
                from("file://" + NodeJSComponentsContext.COMPONENTS_FOLDER  +"?antInclude=**/src/*.js&recursive=true&noop=true" ).process(
                        exchange -> {
                            String originalFileName = (String) exchange.getIn().getHeader(Exchange.FILE_NAME, String.class);
                            System.out.println("got component "+originalFileName);
                        }
                );
            }
        });
        camelContext.start();
    }
}
