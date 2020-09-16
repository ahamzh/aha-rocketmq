package halo.rq.cloud.controller;

import halo.rq.cloud.producer.RqCloudDemoMessageProducer;
import halo.rq.cloud.producer.message.RqCloudDemoMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author shoufeng
 */
@RestController
@RequestMapping("/demo")
public class RqCloudDemoController {

    @Autowired
    private RqCloudDemoMessageProducer rqCloudDemoMessageProducer;

    @GetMapping("/produce")
    public Boolean produce(){
        return rqCloudDemoMessageProducer.haloDemoOutPut().send(MessageBuilder.withPayload(new RqCloudDemoMessage(12345L,"halo测试消息","haloTEST消息")).build());
    }
}
