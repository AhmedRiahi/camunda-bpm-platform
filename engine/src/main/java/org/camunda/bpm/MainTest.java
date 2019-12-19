package org.camunda.bpm;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngineConfiguration;

public class MainTest {


    public static void main(String[] args) {

        ProcessEngine processEngine = createProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addInputStream("process.bpmn", MainTest.class.getClassLoader().getResourceAsStream("process.bpmn"))
                .name("component-process")
                .tenantId("component-process")
                .deploy();

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
