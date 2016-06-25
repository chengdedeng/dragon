package info.yangguo.dragon.client;


import info.yangguo.dragon.common.dto.AnnotationDto;
import info.yangguo.dragon.common.dto.AnnotationType;
import info.yangguo.dragon.common.dto.SpanDto;

import java.util.UUID;

/**
 * @author:杨果
 * @date:16/6/1 下午10:20
 *
 * Description:
 * Trace工具类,主要是用来生成Span的
 */
public class Tracer {
    private static final ThreadLocal<SpanDto> currentSpanThreadLocal = new ThreadLocal<>();
    //主要是为了root span的异常捕获
    private static final ThreadLocal<SpanDto> rootSpanThreadLocal = new ThreadLocal<>();
    private Transfer transfer;

    public void setTransfer(Transfer transfer) {
        this.transfer = transfer;
    }

    private Tracer() {
    }

    private static class TraceHolder {
        static Tracer instance = new Tracer();
    }

    public static Tracer getTracer() {
        return TraceHolder.instance;
    }

    public SpanDto getCurrentSpan() {
        return currentSpanThreadLocal.get();
    }

    public SpanDto genSpan(String traceId, String pid, String id, String service) {
        SpanDto spanDto = new SpanDto();
        spanDto.setTraceId(traceId);
        spanDto.setSpanId(id);
        spanDto.setParentId(pid);
        spanDto.setServiceName(service);
        return spanDto;
    }

    /**
     * 只有consumer会生成RootSpan
     */
    public SpanDto genRootSpan(String service) {
        SpanDto spanDto = new SpanDto();
        spanDto.setTraceId(getId());
        spanDto.setSpanId(getId());
        spanDto.setServiceName(service);
        rootSpanThreadLocal.set(spanDto);
        return spanDto;
    }

    //de annotation
    public void exceptionRecord(AnnotationDto annotationDto) {
        if (currentSpanThreadLocal.get() != null) {
            currentSpanThreadLocal.get().addAnnotation(annotationDto);
        } else if (rootSpanThreadLocal.get() != null) {
            rootSpanThreadLocal.get().addAnnotation(annotationDto);
        }
    }

    //cs annotation
    public void clientSendRecord(SpanDto spanDto, String ip, int port, long start, String app) {
        AnnotationDto annotationDto = new AnnotationDto();
        annotationDto.setType(AnnotationType.ClientSend);
        annotationDto.setTime(start);
        annotationDto.setIp(ip);
        annotationDto.setPort(port);
        annotationDto.setApp(app);
        spanDto.addAnnotation(annotationDto);
    }


    //cr annotation
    public void clientReceiveRecord(SpanDto spanDto, String ip, int port, long end, String app) {
        AnnotationDto annotationDto = new AnnotationDto();
        annotationDto.setType(AnnotationType.ClientRecevie);
        annotationDto.setIp(ip);
        annotationDto.setPort(port);
        annotationDto.setTime(end);
        annotationDto.setApp(app);
        spanDto.addAnnotation(annotationDto);
        rootSpanThreadLocal.remove();
        transfer.send(spanDto);
    }

    //sr annotation
    public void serverReceiveRecord(SpanDto spanDto, String ip, int port, long start, String app) {
        AnnotationDto annotationDto = new AnnotationDto();
        annotationDto.setType(AnnotationType.ServerReceive);
        annotationDto.setIp(ip);
        annotationDto.setPort(port);
        annotationDto.setTime(start);
        annotationDto.setApp(app);
        spanDto.addAnnotation(annotationDto);
        currentSpanThreadLocal.set(spanDto);
    }

    //ss annotation
    public void serverSendRecord(SpanDto spanDto, String ip, int port, long end, String app) {
        AnnotationDto annotationDto = new AnnotationDto();
        annotationDto.setType(AnnotationType.ServerSend);
        annotationDto.setTime(end);
        annotationDto.setIp(ip);
        annotationDto.setPort(port);
        annotationDto.setApp(app);
        spanDto.addAnnotation(annotationDto);
        currentSpanThreadLocal.remove();
        transfer.send(spanDto);
    }


    public static String getId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}

