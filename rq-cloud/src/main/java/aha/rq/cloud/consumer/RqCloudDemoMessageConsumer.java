package aha.rq.cloud.consumer;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author shoufeng
 */
public interface RqCloudDemoMessageConsumer {

    @Input(value = "aha-demo-input")
    SubscribableChannel ahaDemoInPut();
}
