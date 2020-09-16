package halo.rq.cloud.consumer;

import halo.rq.cloud.producer.message.RqCloudDemoMessage;
import com.alibaba.fastjson.JSON;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

/**
 * @author shoufeng
 */
@Service
public class RqCloudDemoMessageService {

    @StreamListener("halo-demo-input")
    public void messageConsumer(@Payload RqCloudDemoMessage message) {
        System.out.println("收到消息: " + JSON.toJSONString(message));
    }
}
