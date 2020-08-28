package aha.rq.client.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * 顺序消息生产
 * 发送消息到指定topic中的指定queue
 *
 * @author shoufeng
 */
public class SelectorProducer {
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("aha_select_producer");
        defaultMQProducer.setSendMsgTimeout(60000);
        defaultMQProducer.setNamesrvAddr("10.162.12.175:9876");
        defaultMQProducer.start();
        for (int i = 0; i < 10; i++) {
            Message message = new Message();
            message.setTags("shunxu");
            message.setBody(("顺序消息00" + i).getBytes());
            message.setTopic("aha_order_topic");
            defaultMQProducer.send(message, new MessageQueueSelector() {
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    if ("shunxu".equals(arg)) {
                        for (MessageQueue mq : mqs) {
                            if (mq.getQueueId() == 0L) {
                                return mq;
                            }
                        }
                    }
                    return null;
                }
            }, message.getTags());
        }
        defaultMQProducer.shutdown();

    }
}
