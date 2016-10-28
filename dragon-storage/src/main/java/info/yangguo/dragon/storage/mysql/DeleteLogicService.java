package info.yangguo.dragon.storage.mysql;

import com.codahale.metrics.annotation.Timed;

import info.yangguo.dragon.storage.mysql.dao.AnnotationMapper;
import info.yangguo.dragon.storage.mysql.dao.ServiceMapper;
import info.yangguo.dragon.storage.mysql.dao.SpanMapper;
import info.yangguo.dragon.storage.mysql.dao.TraceMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.SpanPojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashSet;
import java.util.List;

/**
 * @author:杨果
 * @date:16/6/13 上午9:40
 *
 * Description:
 *
 */
public class DeleteLogicService {
    private static Logger logger = LoggerFactory.getLogger(DeleteLogicService.class);
    //每次查询的trace条数
    private static int limit = 1000;
    private Configuration configuration;
    @Autowired
    private AnnotationMapper annotationMapper;
    @Autowired
    private ServiceMapper serviceMapper;
    @Autowired
    private SpanMapper spanMapper;
    @Autowired
    private TraceMapper traceMapper;


    public DeleteLogicService(Configuration configuration) {
        this.configuration = configuration;
    }


    public void delete(String traceId, List<SpanPojo> spanPojos) {
        //由于没有事务,逻辑的设计严重依赖来于顺序
        for (SpanPojo spanPojo : spanPojos) {
            annotationMapper.deleteAnnotation(spanPojo.getSpanId(), traceId);
            spanMapper.deleteSpan(spanPojo.getSpanId(), traceId);
        }
        traceMapper.deleteTrace(traceId);
    }

    @Timed
    public void process() {
        logger.info("Trace开始回收");
        long begin = new Date().getTime();
        int count = 0;
        while (true) {
            List<String> traceId1 = traceMapper.getTraceId(new Date().getTime() - configuration.getLifeTime() * 24 * 60 * 60 * 1000, limit);
            HashSet<String> traceIds2 = new HashSet<>();
            for (String traceId : traceId1) {
                traceIds2.add(traceId);
            }
            count += traceIds2.size();
            for (String traceId : traceIds2) {
                List<SpanPojo> spanPojos = spanMapper.getSpanByTraceId(traceId);
                delete(traceId, spanPojos);
            }

            if (traceId1.size() < limit) {
                break;
            }
        }
        long end = new Date().getTime();
        logger.info("本次回收的Trace条数为:{},耗时:{}", count, end - begin);
    }
}
