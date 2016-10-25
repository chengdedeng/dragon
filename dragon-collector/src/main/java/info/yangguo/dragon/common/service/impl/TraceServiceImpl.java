package info.yangguo.dragon.common.service.impl;


import com.codahale.metrics.annotation.Timed;

import info.yangguo.dragon.common.dto.SpanDto;
import info.yangguo.dragon.common.service.TraceService;
import info.yangguo.dragon.storage.InsertService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author:杨果
 * @date:16/6/2 下午6:33
 *
 * Description:
 *
 */
@Component("traceServiceImpl")
public class TraceServiceImpl implements TraceService {
    private static Logger logger = LoggerFactory.getLogger(TraceServiceImpl.class);

    private InsertService insertService;

    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }

    @Override
    @Timed
    public void sendSpan(List<SpanDto> spanDtos) {
        insertService.insert(spanDtos);
        logger.info(spanDtos.toString());
    }
}
