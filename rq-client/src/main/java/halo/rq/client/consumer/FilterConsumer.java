package halo.rq.client.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 过滤消息
 * 在大多数情况下，TAG是一个简单而有用的设计，其可以来选择您想要的消息。
 *
 * @author shoufeng
 */
public class FilterConsumer {
    public static void main(String[] args) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("halo_filter_consumer");
        defaultMQPushConsumer.setNamesrvAddr("10.162.12.175:9876");
        /**
         * RocketMQ只定义了一些基本语法来支持这个特性。你也可以很容易地扩展它。
         *
         * 数值比较，比如：>，>=，<，<=，BETWEEN，=；
         * 字符比较，比如：=，<>，IN；
         * IS NULL 或者 IS NOT NULL；
         * 逻辑符号 AND，OR，NOT；
         * 常量支持类型为：
         *
         * 数值，比如：123，3.1415；
         * 字符，比如：'abc'，必须用单引号包裹起来；
         * NULL，特殊的常量
         * 布尔值，TRUE 或 FALSE
         * 只有使用push模式的消费者才能用使用SQL92标准的sql语句，接口如下：
         *
         * public void subscribe(finalString topic, final MessageSelector messageSelector)
         */
        //tag_c不接受
        defaultMQPushConsumer.subscribe("halo_filter_topic", "tag_a||tag_b");
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println(new String(msg.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        defaultMQPushConsumer.start();
    }
}
