package ru.boraldan.temporal.config;


public class TemporalConfig {


    // ручная настройка бинов,
    // вместо @WorkflowImpl(taskQueues = "workflow-tq")  на классе
    // и  в application.yaml
    // workers-auto-discovery:
    //   packages: ru.boraldan.temporal

//    @Bean
//    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
//        return WorkerFactory.newInstance(workflowClient);
//    }
//
//    // Регистрируем Worker Workflow
//    @Bean
//    public Worker workflowWorker(WorkerFactory workerFactory) {
//        Worker worker = workerFactory.newWorker("workflow-tq");
//        worker.registerWorkflowImplementationTypes(OrderWorkflow_v1.class);
//        return worker;
//    }
//
//    @Bean
//    public CommandLineRunner startWorker(WorkerFactory workerFactory) {
//        return args -> workerFactory.start();
//    }
}



