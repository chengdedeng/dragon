package info.yangguo.dragon.storage.mysql;

import info.yangguo.dragon.common.dto.SpanDto;
import info.yangguo.dragon.storage.InsertService;
import info.yangguo.dragon.storage.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author:杨果
 * @date:16/9/3 上午9:16
 *
 * Description:
 *
 */
@Component("producer")
public class Producer implements InsertService {
    private static Logger logger = LoggerFactory.getLogger(Producer.class);

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private Consumer consumer;


    public void handleMessage(String trace) {
        consumer.execute(trace);
    }

    @Override
    public void insert(List<SpanDto> spans) {
        String spansJson = JsonUtil.toJson(spans, true);
        rabbitTemplate.convertAndSend("LogExchange", "trace", spansJson);
    }
}
