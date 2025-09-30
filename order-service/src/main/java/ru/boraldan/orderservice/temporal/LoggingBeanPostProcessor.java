package ru.boraldan.orderservice.temporal;


import io.temporal.worker.Worker;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class LoggingBeanPostProcessor implements BeanPostProcessor {

    // работает только, если мы явно в конфиге создаем бины, для аннотаций @ActivityImpl не работает

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        if (bean instanceof Worker) {
            System.out.println("Worker бин зарегистрирован: " + beanName + ", " + bean);
        }
        return bean;
    }
}
