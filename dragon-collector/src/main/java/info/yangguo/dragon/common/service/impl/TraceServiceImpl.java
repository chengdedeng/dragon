package info.yangguo.dragon.common.service.impl;


import info.yangguo.dragon.common.dto.SpanDto;
import info.yangguo.dragon.common.service.TraceService;
import info.yangguo.dragon.storage.InsertService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


/**
 * @author:杨果
 * @date:16/6/2 下午6:33
 *
 * Description:
 *
 */
public class TraceServiceImpl implements TraceService {
    private static Logger logger = LoggerFactory.getLogger(TraceServiceImpl.class);

    private InsertService insertService;

    public void setInsertService(InsertService insertService) {
        this.insertService = insertService;
    }

    @Override
    public void sendSpan(List<SpanDto> spanDtos) {
        insertService.insert(spanDtos);
        logger.info(spanDtos.toString());
    }
}
