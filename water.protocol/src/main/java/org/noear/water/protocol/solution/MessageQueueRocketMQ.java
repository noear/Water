package org.noear.water.protocol.solution;

import org.apache.rocketmq.client.consumer.DefaultLitePullConsumer;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.noear.water.protocol.MessageQueue;
import org.noear.water.utils.ext.Act1;

import java.util.List;
import java.util.Properties;
import java.util.function.Consumer;


/**
 *
 * https://www.cnblogs.com/enenen/p/12773099.html
 *
 * https://www.cnblogs.com/markLogZhu/p/12545597.html
 *
 * https://segmentfault.com/a/1190000021240352?utm_source=tag-newest
 *
 * */
public class MessageQueueRocketMQ implements MessageQueue {
    String _queue_name;
    final String group_name = "water_message";
    final String server;

    DefaultMQProducer producer;
    DefaultLitePullConsumer consumer;

    public MessageQueueRocketMQ(String name, Properties prop) {
        _queue_name = name;
        server = prop.getProperty("server");
    }

    private void initProducer() {
        if (producer != null) {
            return;
        }

        synchronized (group_name) {
            if (producer != null) {
                return;
            }

            producer = new DefaultMQProducer(group_name);
            producer.setNamesrvAddr(server);
            //发送超时时间，默认3000 单位ms
            producer.setSendMsgTimeout(3000);

            try {
                producer.start();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private void initConsumer(){
        if(consumer != null){
            return;
        }

        synchronized (group_name) {
            if(consumer != null){
                return;
            }

            consumer = new DefaultLitePullConsumer(group_name);

            consumer.setNamesrvAddr(server);
            //一次最大消费的条数
            consumer.setPullBatchSize(1);
            //无消息时，最大阻塞时间。默认5000 单位ms
            consumer.setPollTimeoutMillis(3000);
            try {
                //要消费的topic，可使用tag进行简单过滤
                consumer.subscribe(_queue_name, "*");
                consumer.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException(ex);
            }
        }

    }

    @Override
    public boolean push(String msg_id) {
        initProducer();

        try {
            Message msgX = new Message(_queue_name, msg_id.getBytes());
            msgX.setKeys(msg_id);
            SendResult send = producer.send(msgX);

            if (send.getSendStatus().equals(SendStatus.SEND_OK)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }



    @Override
    public String poll() {
        initConsumer();

        try {
            //拉取消息，无消息时会阻塞
            List<MessageExt> msgs = consumer.poll();

            if (msgs.size() == 0) {
                return null;
            }

            //同步消费位置。不执行该方法，应用重启会存在重复消费。
            consumer.commitSync();

            //返回消息
            return new String(msgs.get(0).getBody());

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void pollGet(Consumer<String> callback) {
        initConsumer();

        try {
            while (true) {
                //拉取消息，无消息时会阻塞
                List<MessageExt> msgs = consumer.poll();

                if (msgs.size() == 0) {
                   break;
                }

                //同步消费位置。不执行该方法，应用重启会存在重复消费。
                consumer.commitSync();

                //返回消息
                callback.accept(new String(msgs.get(0).getBody()));
            }

        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void close() throws Exception {
        if (producer != null) {
            producer.shutdown();
        }

        if(consumer != null){
            consumer.shutdown();
        }
    }
}
