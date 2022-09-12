package com.example.springbootproject;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Slf4j
public class mqtest {
    /**
     * 生产者
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("GROUP_DEMO");
        producer.setNamesrvAddr("127.0.0.1:9876");
        producer.setSendLatencyFaultEnable(true);

        producer.start();
        for (int i = 0; i < 10; i++) {
            Message msg = new Message("TOPIC_TEST",
                    "TAGE_TEST",
                    ("ROCKETMQ000" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
            //发送核心方法
            //同步
            SendResult send = producer.send(msg);
            //异步
            producer.send(msg, new SendCallback() {
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("===" + sendResult);
                }

                @Override
                public void onException(Throwable throwable) {
                    System.out.println("e:" + throwable);
                }
            });
            //单向
            producer.sendOneway(msg);

            System.out.println("send:%s%n" + send);
        }
        //producer.shutdown();
    }

    /**
     * 消费者
     * @throws Exception
     */
    @Test
    public void consume() throws Exception {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group_consumer");
        consumer.setNamesrvAddr("127.0.0.1:9876");
        //订阅Topic，去消费生产者产生的消息。
        consumer.subscribe("TOPIC_TEST", "*");//tag tagA|tagB|tagC
        consumer.setMessageModel(MessageModel.CLUSTERING);

        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext context) {
                try {
                    for (MessageExt item : list) {
                        String topic = item.getTopic();
                        String tags = item.getTags();
                        String msgBody = new String(item.getBody(), "utf-8");
                        System.out.println("收到消息：topic:" + topic + ",tags:" + tags + ",msg:" + msgBody);

                    }
                } catch (Exception e) {
                    log.error("e:", e);
                    return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动消费者
        consumer.start();
        System.out.println("consumer start");
        BeanFactory
    }


}
