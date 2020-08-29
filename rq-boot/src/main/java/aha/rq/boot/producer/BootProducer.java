package aha.rq.boot.producer;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import lombok.SneakyThrows;
import org.apache.rocketmq.acl.common.AclClientRPCHook;
import org.apache.rocketmq.acl.common.SessionCredentials;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.RPCHook;
import org.apache.rocketmq.remoting.protocol.RemotingCommand;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author shoufeng
 */
//@Component
public class BootProducer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

//    @PostConstruct
    public void syncSend() {
        System.out.println(rocketMQTemplate.syncSend("aha_boot_sync_topic", MessageBuilder.withPayload("张三sync" + DateUtil.format(new Date(), DatePattern.ISO8601_PATTERN)).build()));
    }

//    @PostConstruct
    public void asyncSend() {
        rocketMQTemplate.asyncSend("aha_boot_async_topic", MessageBuilder.withPayload("张三async" + DateUtil.format(new Date(), DatePattern.ISO8601_PATTERN)).build(), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("异步发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("异步发送异常");
            }
        });
    }

//    @PostConstruct
    public void sendOneWay() {
        rocketMQTemplate.sendOneWay("aha_boot_oneway_topic", MessageBuilder.withPayload("张三sendOneWay" + DateUtil.format(new Date(), DatePattern.ISO8601_PATTERN)).build());
    }

//    @PostConstruct
    public void sendOrder() {
        rocketMQTemplate.setMessageQueueSelector(new MessageQueueSelector() {
            @Override
            public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                return list.get(Integer.parseInt(o.toString()));
            }
        });
        rocketMQTemplate.sendOneWayOrderly("aha_oneway_order_topic" + ":tag_a", "队列1", "0");
        rocketMQTemplate.sendOneWayOrderly("aha_oneway_order_topic" + ":tag_b", "队列2", "1");
    }

    @PostConstruct
    public void sendDelay(){
        rocketMQTemplate.syncSend("aha_delay_topic",MessageBuilder.withPayload("延迟消息").build(),60*1000,1);
    }

    @PostConstruct
    public void sendTransaction() {
        AtomicInteger checkTimes = new AtomicInteger(0);

        rocketMQTemplate.createAndStartTransactionMQProducer("aha_tx_producer", new RocketMQLocalTransactionListener() {
            @SneakyThrows
            @Override
            public RocketMQLocalTransactionState executeLocalTransaction(org.springframework.messaging.Message msg, Object arg) {
                System.out.println("执行本地事务,msg: " + JSON.toJSONString(msg));
                System.out.println("执行本地事务,arg: " + JSON.toJSONString(arg));
                int times = checkTimes.incrementAndGet();
                if (times != 10){
                    Thread.sleep(10000L);
//                    return RocketMQLocalTransactionState.UNKNOWN;
                }
                return RocketMQLocalTransactionState.COMMIT;
            }

            @SneakyThrows
            @Override
            public RocketMQLocalTransactionState checkLocalTransaction(org.springframework.messaging.Message msg) {
                System.out.println("检查本地事务,msg: " + JSON.toJSONString(msg));
                if (checkTimes.get() != 10){
                    return RocketMQLocalTransactionState.UNKNOWN;
                }
                return RocketMQLocalTransactionState.COMMIT;
            }
        }, Executors.newCachedThreadPool(), RocketMQUtil.getRPCHookByAkSk(null,null,null));

        rocketMQTemplate.sendMessageInTransaction("aha_tx_producer","aha_tx_topic",MessageBuilder.withPayload("消息message").build(),"kkk传入的args参数");
    }

}
