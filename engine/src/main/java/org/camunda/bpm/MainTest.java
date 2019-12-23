package org.camunda.bpm;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;
import org.camunda.bpm.engine.component.context.scanner.ComponentsScanner;

public class MainTest {


    public static void main(String[] args) throws Exception {

        ComponentsScanner componentsScanner = new ComponentsScanner();
        componentsScanner.startScanner();
        Thread.sleep(1000*20);

    }

    public static ProcessEngine createProcessEngine(){
        return ProcessEngineConfiguration.createStandaloneInMemProcessEngineConfiguration()
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_FALSE)
                .setJdbcDriver("org.postgresql.Driver")
                .setJdbcUrl("jdbc:postgresql://127.0.0.1:5432/camunda")
                .setJdbcUsername("postgres")
                .setJdbcPassword("root")
                .setJobExecutorActivate(false)
                .setDatabaseSchemaUpdate("true")
                .buildProcessEngine();
    }
}
