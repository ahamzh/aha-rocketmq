package aha.rq.client.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

/**
 * 发送延时消息
 *
 * @author shoufeng
 */
public class DelayProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException, MQBrokerException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("aha_delay_producer");
        defaultMQProducer.setNamesrvAddr("10.162.12.175:9876");
        defaultMQProducer.setSendMsgTimeout(60000);
        defaultMQProducer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setTopic("aha_delay_topic");
            message.setBody(("延时消息00" + i).getBytes());
            message.setTags("delay_tag");
            // 设置延时等级3,这个消息将在10s之后发送(现在只支持固定的几个时间,详看delayTimeLevel)
            // private String messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h";
            message.setDelayTimeLevel(1);
            defaultMQProducer.send(message);
        }
        defaultMQProducer.shutdown();

    }
}
