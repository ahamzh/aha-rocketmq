package halo.rq.boot.consumer;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author shoufeng
 */
//@Component
@RocketMQMessageListener(topic = "halo_boot_sync_topic", consumerGroup = "halo_boot_consumer_02")
public class BootConsumer02 implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("BootConsumer02接收到消息: " + message);
    }
}
