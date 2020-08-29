package aha.rq.cloud;

import aha.rq.cloud.consumer.RqCloudDemoMessageConsumer;
import aha.rq.cloud.producer.RqCloudDemoMessageProducer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author shoufeng
 */
@SpringBootApplication
@EnableBinding(value = {RqCloudDemoMessageProducer.class, RqCloudDemoMessageConsumer.class})
public class RqCloudApp {

    public static void main(String[] args) {
        SpringApplication.run(RqCloudApp.class, args);
    }
}
