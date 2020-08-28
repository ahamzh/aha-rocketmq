package aha.rq.client.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.ArrayList;
import java.util.List;

/**
 * 批量消息
 * 批量发送消息能显著提高传递小消息的性能。
 * 限制是这些批量消息应该有相同的topic，相同的waitStoreMsgOK，而且不能是延时消息。此外，这一批消息的总大小不应超过4MB。
 *
 * @author shoufeng
 */
public class BatchProducer {
    public static void main(String[] args) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("aha_batch_producer");
        defaultMQProducer.setNamesrvAddr("10.162.12.175:9876");
        defaultMQProducer.setSendMsgTimeout(60000);
        defaultMQProducer.start();
        List<Message> messageList = new ArrayList<>();
        for (int i = 10; i > 0; i--) {
            Message message = new Message();
            message.setTopic("aha_batch_topic");
            message.setTags("batch_tag");
            message.setKeys("batch_key" + i);
            message.setBody(("batch消息00" + i).getBytes());
            messageList.add(message);
        }
        defaultMQProducer.send(messageList);
        defaultMQProducer.shutdown();
    }
}
