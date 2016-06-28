package info.yangguo.dragon.storage.mysql;

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

import org.springframework.dao.DuplicateKeyException;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class InsertMysqlServiceImpl implements InsertService {
    private ArrayBlockingQueue<List<SpanDto>> queue;
    private static final int taskCount = Runtime.getRuntime().availableProcessors();
    private static final ExecutorService executors = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    private static final ConcurrentHashMap<String, Integer> serviceMap = new ConcurrentHashMap<>();

    private AnnotationMapper annotationMapper;
    private ServiceMapper serviceMapper;
    private SpanMapper spanMapper;
    private TraceMapper traceMapper;


    private InsertMysqlServiceImpl() {
    }

    public InsertMysqlServiceImpl(Configuration configuration) {
        int queueSize = configuration.getQueueSize() == 0 ? 2048 : configuration.getQueueSize();
        queue = new ArrayBlockingQueue<List<SpanDto>>(queueSize);
        for (int i = 0; i < taskCount; i++) {
            executors.execute(new InsertTask());
        }
    }

    @Override
    public void insert(List<SpanDto> spans) {
        queue.add(spans);
    }

    private class InsertTask implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    List<SpanDto> spanDtos = queue.take();
                    if (spanDtos != null) {
                        for (SpanDto spanDto : spanDtos) {
                            addAnnotation(spanDto);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
        TracePojo tracePojo = new TracePojo();
        tracePojo.setServiceId(serviceId);
        tracePojo.setTraceId(traceId);
        tracePojo.setTime(time);

        traceMapper.addTrace(tracePojo);
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
}