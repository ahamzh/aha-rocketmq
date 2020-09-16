package halo.rq.client.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 顺序消息消费
 *
 * @author shoufeng
 */
public class OrderConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("halo_order_consumer");
        defaultMQPushConsumer.setNamesrvAddr("10.162.12.175:9876");
        defaultMQPushConsumer.subscribe("halo_order_topic", "*");
        defaultMQPushConsumer.setMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                for (MessageExt msg : msgs) {
                    if ("shunxu".equals(msg.getTags())) {
                        System.out.println(new String(msg.getBody()));
                        return ConsumeOrderlyStatus.SUCCESS;
                    }
                }
                return ConsumeOrderlyStatus.SUSPEND_CURRENT_QUEUE_A_MOMENT;
            }
        });
        defaultMQPushConsumer.start();
    }
}
