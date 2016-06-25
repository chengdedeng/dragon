package info.yangguo.dragon.common.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpanDto implements Serializable {
    private static final long serialVersionUID = -8494927317993745408L;
    private String spanId;
    private String traceId;
    private String parentId;
    private String serviceName;
    private List<AnnotationDto> annotationEntities;

    public SpanDto() {
        annotationEntities = new ArrayList<AnnotationDto>();
    }

    public void addAnnotation(AnnotationDto a) {
        annotationEntities.add(a);
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<AnnotationDto> getAnnotationEntities() {
        return annotationEntities;
    }

    public void setAnnotationEntities(List<AnnotationDto> annotationEntities) {
        this.annotationEntities = annotationEntities;
    }

    @Override
    public String toString() {
        return "SpanDto{" +
                "spanId='" + spanId + '\'' +
                ", traceId='" + traceId + '\'' +
                ", parentId='" + parentId + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", annotationEntities=" + annotationEntities +
                '}';
    }
}
