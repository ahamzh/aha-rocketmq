package aha.rq.cloud.producer;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @author shoufeng
 */
public interface RqCloudDemoMessageProducer {

    @Output(value = "aha-demo-output")
    MessageChannel ahaDemoOutPut();
}
