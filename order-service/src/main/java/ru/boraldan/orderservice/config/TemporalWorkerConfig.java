package ru.boraldan.orderservice.config;





public class TemporalWorkerConfig {


    // ручная настройка бинов,
    // вместо @ActivityImpl(taskQueues = "order-tq")  на классе
    // и  в application.yaml
    // workers-auto-discovery:
    //   packages: ru.boraldan.temporal

//    @Bean
//    public WorkerFactory workerFactory(WorkflowClient workflowClient) {
//        return WorkerFactory.newInstance(workflowClient);
//    }
//
//    // Регистрируем Worker  Activity
//    @Bean
//    public Worker orderActivityWorker(WorkerFactory workerFactory, OrderService orderService) {
//        Worker worker = workerFactory.newWorker("order-tq"); // activity queue
//        worker.registerActivitiesImplementations(new OrderActivities_v1(orderService));
//        return worker;
//    }
//
//    @Bean
//    public CommandLineRunner startWorker(WorkerFactory workerFactory) {
//        return args -> workerFactory.start();
//    }

}