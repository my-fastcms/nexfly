package com.nexfly.rocketmq.canal;

import cn.throwx.canal.gule.CanalGlue;
import com.alibaba.fastjson.JSON;
import com.nexfly.rocketmq.common.MqConstants;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author wangjun
 * @Date 2024/10/13
 **/
@Component
@RocketMQMessageListener(topic = MqConstants.CANAL_TOPIC, consumerGroup = MqConstants.CANAL_TOPIC)
public class NexflyCanalListener implements RocketMQListener<String> {

    private static final Logger log = LoggerFactory.getLogger(NexflyCanalListener.class);

    @Autowired
    private CanalGlue canalGlue;

    @Override
    public void onMessage(String message) {
        Map map = JSON.parseObject(message, Map.class);
        String table = map.get("table").toString();
        String database = map.get("database").toString();
        log.info("canal-database: {}, table: {}, mq message:{}", database, table, message);
        canalGlue.process(message);
    }

}
