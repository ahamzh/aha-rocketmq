package halo.rq.client.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 并发消费者
 *
 * @author shoufeng
 */
public class ConcurrentlyConsumer01 {
    public static void main(String[] args) throws MQClientException {
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(new Runnable() {
            @Override
            @lombok.SneakyThrows
            public void run() {
                consume("第一个消费者", countDownLatch);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            @lombok.SneakyThrows
            public void run() {
//                consume("第二个消费者", countDownLatch);
            }
        }).start();
    }

    public static void consume(final String name, CountDownLatch countDownLatch) throws MQClientException, InterruptedException {
        Thread.sleep(5000L);
        // 实例化消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("halo_con_comsumer_01");

        // 设置NameServer的地址
        consumer.setNamesrvAddr("10.162.12.175:9876");

        // 订阅一个或者多个Topic，以及Tag来过滤需要消费的消息
        consumer.subscribe("halo_test_topic", "*");
        // 注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
//                System.out.printf(name +  " %s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
                for (MessageExt msg : msgs) {
                    System.out.println(msg);
                    System.out.println(new String(msg.getBody()));
                }
                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

//        countDownLatch.countDown();
//        countDownLatch.await();
        // 启动消费者实例
        consumer.start();
        System.out.printf("Consumer Started.%n");
    }
}
