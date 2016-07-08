package info.yangguo.dragon.storage.mysql;

import info.yangguo.dragon.storage.mysql.dao.AnnotationMapper;
import info.yangguo.dragon.storage.mysql.dao.ServiceMapper;
import info.yangguo.dragon.storage.mysql.dao.SpanMapper;
import info.yangguo.dragon.storage.mysql.dao.TraceMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.SpanPojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author:杨果
 * @date:16/6/13 上午9:40
 *
 * Description:
 *
 */
public class ScheduleDeleteService {
    private static Logger logger = LoggerFactory.getLogger(ScheduleDeleteService.class);
    //系统启动多久之后开始回收日志
    private static int initDelay = 1;
    //每次查询的trace条数
    private static int limit = 1000;
    private Configuration configuration;
    private AnnotationMapper annotationMapper;
    private ServiceMapper serviceMapper;
    private SpanMapper spanMapper;
    private TraceMapper traceMapper;


    private ScheduleDeleteService() {
    }

    public ScheduleDeleteService(Configuration configuration) {
        this.configuration = configuration;
        ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(1);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new DeleteTask(), initDelay, configuration.getDeleteInterval(), TimeUnit.MINUTES);
        logger.info("Mysql定时删除任务开启");
    }

    public void delete(String traceId, List<SpanPojo> spanPojos) {
        //由于没有事务,逻辑的设计严重依赖来于顺序
        for (SpanPojo spanPojo : spanPojos) {
            annotationMapper.deleteAnnotation(spanPojo.getSpanId(), traceId);
            spanMapper.deleteSpan(spanPojo.getSpanId(), traceId);
        }
        traceMapper.deleteTrace(traceId);
    }

    private void process() {
        int count = 0;
        while (true) {
            List<String> traceId1 = traceMapper.getTraceId(new Date().getTime() - configuration.getLifeTime() * 24 * 60 * 60 * 1000, limit);
            HashSet<String> traceIds2=new HashSet<>();
            for(String traceId:traceId1){
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
        logger.warn("本次回收的Trace条数为:{}", count);
    }

    private class DeleteTask implements Runnable {

        @Override
        public void run() {
            process();
        }
    }


    public AnnotationMapper getAnnotationMapper() {
        return annotationMapper;
    }

    public void setAnnotationMapper(AnnotationMapper annotationMapper) {
        this.annotationMapper = annotationMapper;
    }

    public ServiceMapper getServiceMapper() {
        return serviceMapper;
    }

    public void setServiceMapper(ServiceMapper serviceMapper) {
        this.serviceMapper = serviceMapper;
    }

    public SpanMapper getSpanMapper() {
        return spanMapper;
    }

    public void setSpanMapper(SpanMapper spanMapper) {
        this.spanMapper = spanMapper;
    }

    public TraceMapper getTraceMapper() {
        return traceMapper;
    }

    public void setTraceMapper(TraceMapper traceMapper) {
        this.traceMapper = traceMapper;
    }
}
