package info.yangguo.dragon.storage.mysql;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import info.yangguo.dragon.common.dto.AnnotationDto;
import info.yangguo.dragon.common.dto.AnnotationType;
import info.yangguo.dragon.common.dto.SpanDto;
import info.yangguo.dragon.storage.InsertService;
import info.yangguo.dragon.storage.mysql.dao.AnnotationMapper;
import info.yangguo.dragon.storage.mysql.dao.ServiceMapper;
import info.yangguo.dragon.storage.mysql.dao.SpanMapper;
import info.yangguo.dragon.storage.mysql.dao.TraceMapper;
import info.yangguo.dragon.storage.mysql.dao.pojo.AnnotationPojo;
import info.yangguo.dragon.storage.mysql.dao.pojo.ServicePojo;
import info.yangguo.dragon.storage.mysql.dao.pojo.SpanPojo;
import info.yangguo.dragon.storage.mysql.dao.pojo.TracePojo;
import info.yangguo.dragon.storage.utils.JsonUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * @author:杨果
 * @date:16/9/3 上午9:16
 *
 * Description:
 *
 */
public class InsertMysqlServiceImpl2 implements InsertService {
    private static Logger logger = LoggerFactory.getLogger(InsertMysqlServiceImpl1.class);
    private static final ConcurrentHashMap<String, Integer> serviceMap = new ConcurrentHashMap<>();
    private static Cache<String, Boolean> cache = CacheBuilder.newBuilder()
            .maximumSize(100000)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build();


    private AnnotationMapper annotationMapper;
    private ServiceMapper serviceMapper;
    private SpanMapper spanMapper;
    private TraceMapper traceMapper;
    private RabbitTemplate rabbitTemplate;


    public void handleMessage(String trace) {
        try {
            List<SpanDto> spanDtoList = (List<SpanDto>) JsonUtil.fromJson(trace);
            if (spanDtoList != null) {
                for (SpanDto spanDto : spanDtoList) {
                    addAnnotation(spanDto);
                }
            }
        } catch (Exception e) {
            logger.warn("{}", e.getMessage());
        }
    }

    @Override
    public void insert(List<SpanDto> spans) {
        String spansJson = JsonUtil.toJson(spans, true);
        rabbitTemplate.convertAndSend("LogExchange", "trace", spansJson);
    }


    private int getServiceId(SpanDto spanDto) {
        String serviceName = spanDto.getServiceName();
        Integer serviceId = serviceMap.get(serviceName);
        if (serviceId == null) {
            ServicePojo servicePojo = new ServicePojo();
            servicePojo.setServiceName(serviceName);
            ServicePojo servicePojoTmp = serviceMapper.getServiceByName(serviceName);
            if (servicePojoTmp != null) {
                serviceId = servicePojoTmp.getServiceId();
            }
            if (serviceId == null) {
                try {
                    serviceId = serviceMapper.addService(servicePojo);
                } catch (DuplicateKeyException e) {
                    serviceId = serviceMapper.getServiceByName(serviceName).getServiceId();
                }
            }
        }
        if (serviceId != null) {
            serviceMap.put(serviceName, serviceId);
        }
        return serviceId;
    }

    /**
     * 只有CS Annotation的时候才插入
     */
    private void addTrace(int serviceId, String traceId, long time) {
        StringBuilder key = new StringBuilder().append(serviceId).append("-").append(traceId);
        TracePojo tracePojo = new TracePojo();
        tracePojo.setServiceId(serviceId);
        tracePojo.setTraceId(traceId);
        tracePojo.setTime(time);
        if (!cache.asMap().containsKey(key.toString())) {
            try {
                cache.put(key.toString(), true);
                traceMapper.addTrace(tracePojo);
            } catch (Exception e) {
                //在client超时重试的时候,可能会出现Duplicate entry for key 'PRIMARY'异常
                logger.warn("{}", e.getMessage());
            }
        }
    }

    private void addSpan(SpanDto spanDto) {
        SpanPojo spanPojo = new SpanPojo();
        spanPojo.setSpanId(spanDto.getSpanId());
        spanPojo.setTraceId(spanDto.getTraceId());
        spanPojo.setParentId(spanDto.getParentId());
        spanPojo.setServiceId(getServiceId(spanDto));

        spanMapper.addSpan(spanPojo);
    }

    private void addAnnotation(SpanDto spanDto) {
        for (AnnotationDto annotationDto : spanDto.getAnnotationEntities()) {
            AnnotationPojo annotationPojo = new AnnotationPojo();
            annotationPojo.setSpanId(spanDto.getSpanId());
            annotationPojo.setIp(annotationDto.getIp());
            annotationPojo.setPort(annotationDto.getPort());
            annotationPojo.setType(annotationDto.getType());
            annotationPojo.setApp(annotationDto.getApp());
            if (annotationDto.getTime() != null) {
                annotationPojo.setTime(annotationDto.getTime());
            }
            annotationPojo.setTraceId(spanDto.getTraceId());
            annotationPojo.setValue(annotationDto.getValue());
            //由于没有事务,逻辑的设计严重依赖来于顺序
            if (annotationDto.getType().equals(AnnotationType.ClientSend)) {
                addTrace(getServiceId(spanDto), spanDto.getTraceId(), annotationDto.getTime());
                addSpan(spanDto);
            }
            annotationMapper.addAnnotation(annotationPojo);
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

    public RabbitTemplate getRabbitTemplate() {
        return rabbitTemplate;
    }

    public void setRabbitTemplate(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
}
